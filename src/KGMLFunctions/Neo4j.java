package KGMLFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

public class Neo4j
{
    private static final String DB_PATH = "neo4j/data/graph.db";
    
    // START SNIPPET: vars
    GraphDatabaseService graphDb;
    Node nodes[];
	Node firstNode;
    Node secondNode;
    Relationship relationship;
    // END SNIPPET: vars

    // START SNIPPET: createReltype
    private static enum RelTypes implements RelationshipType
    {
        CONNECTED
    }
    // END SNIPPET: createReltype

    /*--------------------------------------------------------------------------
     * Main function
     *--------------------------------------------------------------------------*/
    public static void main( final String[] args ) throws IOException
    {
    	String NodesFile = "";
    	String EdgesFile = "";
    	String OutputFile = "";
    	String ReferenceFile = "";
    	int j = 0;
    	
    	try {	
    		if (args.length >= 8)
    		{
    			while (j < args.length)
    			{
    				if (args[j].equals("-f"))
    				{
    					if (args[j+1].contains("-")==false)
    						NodesFile = args[j+1];
    					if (args[j+1].contains("-")==false)
    						EdgesFile = args[j+2];
    				}
    				
    				if (args[j].equals("-o"))
    				{
    					if (args[j+1].contains("-")==false){
    						OutputFile = args[j+1];
    					}
    				}
    				
    				if (args[j].equals("-g"))
    				{
    					if (args[j+1].contains("-")==false)
    						ReferenceFile = args[j+1];
    				}
    				
    				j++;
    			}
    		}
    		
            EdgesFile = "BacteriaEdges.csv";
            NodesFile = "BacteriaNodes.csv";
            OutputFile = "PathData.csv";
            ReferenceFile = "BacteriaOutputConnectedNeo4j-yifan.csv";
    		//Find shortest paths from database
    		//if (args[0].equals("FindShortestPath")) {
    			Neo4j hello = new Neo4j();
    	        hello.createDb(NodesFile, EdgesFile); //Create Database
    	        //hello.removeData(); (not working)
    	        /* Currently trying to find a way to not have to create the database every time we want to do a shortest path calculation */
    	        hello.ShortestPath(ReferenceFile, OutputFile); //Find shortestPaths, given a reference file
    	        hello.shutDown();
    		//}
    	} catch(Exception e){
    		System.err.println("Error with command line");
    	}
    }
    
    /*--------------------------------------------------------------------------
     * Shortest Path Function - Finds the shortest path between two nodes and fills a csv file with related data
     * Input: Reference File - csv file that contains the Nodes that will be used for calculations
     * Output: OutputFile - csv file that the information will be printed on
     *--------------------------------------------------------------------------*/
    void ShortestPath(String ReferenceFile, String OutputFile) throws IOException {
    	try ( Transaction tx = graphDb.beginTx() )
    	{
    		FileWriter writer = new FileWriter(OutputFile); //OutputFile
    		writer.append("q1, q2, Number of Edges, patwayNumber, Path, Time\n"); //Header for OutputFile
    		
    		BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            try{
               br = new BufferedReader(new FileReader(ReferenceFile));
               line = br.readLine();
               
               while ((line = br.readLine()) != null ) {
               
            	  String[] names = line.split(cvsSplitBy);
                  String s1 = names[0];         //Get Name of first Node (KEGG ENTRY) from reference file
                  String s2 = names[1];         //Get Name of second Node (KEGG ENTRY) from reference file
    		      writer.append(s1 + "," + s2); //Write Nodes to output File

    	  	      int a = findIndex(s1); 
    		      int b = findIndex(s2);
    		   
    		      //This Hashtable is used to determine the number of unique pathways that were crossed in the shortest pathway algorithm
	              Hashtable<String, String> Pways = new Hashtable<String, String>();

	              //Setup for Shortest Path Neo4j function
    		      PathFinder<org.neo4j.graphdb.Path> finder = GraphAlgoFactory.shortestPath(
    	          PathExpanders.forTypeAndDirection( RelTypes.CONNECTED, Direction.OUTGOING ), 20 );        
    		     
    		      double startTime = System.nanoTime(); //Start Timer
    		      org.neo4j.graphdb.Path paths = finder.findSinglePath( nodes[a], nodes[b] ); //Call Neo4j shortest path function
    		      double endTime = System.nanoTime();   //End Timer
    		
    		      double duration = (endTime - startTime) / 1000000.0; //Find duration and conver to milliseconds
    		      int EdgeCount = 0; //Number of edges in pathway
    		      int PCount = 0;    //Number of different pathways crossed for shortest pathway
    	          
    		      //Go through calculated path
    		      for ( Node node : paths.nodes()) {
    	             EdgeCount++; //count edges
    	             //Put pathways into hashtable
    	             Pways.put((String)node.getProperty("PathwayID"), (String)node.getProperty("PathwayID"));
    	          }    	
    		      //The number of Unique pathways will be the number of entries in Hashtable
    	          PCount = Pways.size();
    	          
    	          //Write Data to Output File
    	          writer.append("," + (EdgeCount-1) + "," + PCount + ",");
    	          for ( Node node : paths.nodes()) {
    	             writer.append("(" + node.getProperty("name") + ") " );
    	          }
    	          writer.append("," + duration + "\n");
               } //end while
               
            //error stuff
            }catch (FileNotFoundException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	} finally {
        		writer.flush();
        		writer.close();
        		if (br != null) {
        			try {
        				br.close();
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        		}
        	}
    	    
    		tx.success();
    	}
    }
    
    /*--------------------------------------------------------------------------
     * The function creates the Neo4j database from two given csv files
     * Input: String NodesFile - csv file containing all the Nodes needed to make the database
     *        String EdgesFile - csv file containing all the edges information needed to make the database
     *--------------------------------------------------------------------------*/
    void createDb(String NodesFile, String EdgesFile) throws IOException
    {
        FileUtils.deleteRecursively( new File( DB_PATH ) );

        // START SNIPPET: startDb
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( "neo4j/data/graph.db" );
        registerShutdownHook( graphDb );
        // END SNIPPET: startDb
        
		// START SNIPPET: For reading the given csv file
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        br = new BufferedReader(new FileReader(NodesFile));
        line = br.readLine();
        int i = 0;      //The Index 
        nodes = new Node[count(NodesFile)];
        // END SNIPPET: For reading the given csv file 

        
        // START SNIPPET: transaction
        try ( Transaction tx = graphDb.beginTx() )
        {
			while ((line = br.readLine()) != null) {

        	   String[] Entry = line.split(cvsSplitBy);
        	   
               nodes[i] = graphDb.createNode();
               //properties of a node
               nodes[i].setProperty( "name", Entry[0] );     //Name of Kegg entry
               nodes[i].setProperty( "type", Entry[1] );     //Type of Kegg entry
               nodes[i].setProperty( "reaction", Entry[2]);  //Type of reaction Kegg entry is
               nodes[i].setProperty( "PathwayID", Entry[3]); //Pathway the Kegg entry belongs to
			   i++;
			}
            
			// START SNIPPET: For reading the given csv file
            br = null;
            line = "";
            cvsSplitBy = ",";
            br = new BufferedReader(new FileReader(EdgesFile));
            line = br.readLine();
            i = 0;
           // END SNIPPET: For reading the given csv file 
           
            while ((line = br.readLine()) != null) {
               String[] Edge = line.split(cvsSplitBy);
               //System.out.println(Edge[0] + " " + Edge[1]); //For Debugging
               int a = findIndex(Edge[0]); //Find the Index of the Source Node
               int b = findIndex(Edge[1]); //Find the Index of the target Node
               
               //If a or b = -1, that means the edge is making a reference to a Node that is not expressed as an Entry in the xml files and should therefore be ignored
               if ( a == -1 || b == -1)
                  System.out.println(Edge[0] + " " + a + " " + Edge[1] + " " + b + " " + Edge[4] );  //This prints out the Nodes that should be ignored
               
               if( a != -1 && b != -1) {
            	  //Create the Relationship between the nodes
                  relationship = nodes[a].createRelationshipTo( nodes[b], RelTypes.CONNECTED );
                  relationship.setProperty( "Type", Edge[2] );
                  relationship.setProperty( "EdgeType", Edge[3] );
               }
               // END SNIPPET: addData
            }
            tx.success();
        }
        // END SNIPPET: transaction
    }

    // *** NOT WORKING *** 
    //Function to remove the data from the neo4j database
    void removeData()
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            // START SNIPPET: removingData
            // let's remove the data
            firstNode.getSingleRelationship( RelTypes.CONNECTED, Direction.OUTGOING ).delete();
            firstNode.delete();
            secondNode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
    }

    void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        graphDb.shutdown();
        // END SNIPPET: shutdownServer
    }

    // START SNIPPET: shutdownHook
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
    // END SNIPPET: shutdownHook
    
    /* Method to find the index that an entry that exists in the Nodes Array */
    public int findIndex(String s){
       String a = "";
       for(int i=0; i<nodes.length; i++) {
          a = (String) nodes[i].getProperty("name");
          if( a.equalsIgnoreCase(s) )//Ignore Case and Spaces
        	  return i;
        }
       
        return -1; //Entry is not in the Nodes Array
    }

    /* Method to find the total about of Nodes in our Graph */
    private int count(String filename) throws IOException {
	   BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
       String input;
       int count = 0;
       while((input = bufferedReader.readLine()) != null)
       {
          count++;
       }
       System.out.println(count);
       return (count - 1); // Subtract 1 for Header File 
    }
}