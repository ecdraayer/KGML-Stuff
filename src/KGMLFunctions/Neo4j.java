package KGMLFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
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

    public static void main( final String[] args ) throws IOException
    {
        Neo4j hello = new Neo4j();
        hello.createDb();
        //hello.removeData();
        
        hello.ShortestPath();
        hello.shutDown();
    }

    void ShortestPath() throws IOException {
    	try ( Transaction tx = graphDb.beginTx() )
    	{
    		FileWriter writer = new FileWriter("BacteriaOutputConnectedNeo4j.csv");
    		writer.append("q1, q2, edgeNum, patwayNumber, Time\n");
    		
    		String csvFile = "BacteriaoutputConnected.csv";
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            try{
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            while ((line = br.readLine()) != null ) {
            String[] names = line.split(cvsSplitBy);
            String s1 = names[0];
            String s2 = names[1];
    		writer.append(s1 + "," + s2);

    		int a = findIndex(s1);
    		int b = findIndex(s2);
    		
	        Hashtable<String, String> Pways = new Hashtable<String, String>();

    		PathFinder<org.neo4j.graphdb.Path> finder = GraphAlgoFactory.shortestPath(
    	      PathExpanders.forTypeAndDirection( RelTypes.CONNECTED, Direction.OUTGOING ), 20 );        
    		double startTime = System.nanoTime();
    		org.neo4j.graphdb.Path paths = finder.findSinglePath( nodes[a], nodes[b] );
    		double endTime = System.nanoTime();
    		
    		double duration = (endTime - startTime) / 1000000.0;
    		int EdgeCount = 0;
    		int PCount = 0;
    	    for ( Node node : paths.nodes()) {
    	       EdgeCount++;
    	       Pways.put((String)node.getProperty("PathwayID"), (String)node.getProperty("PathwayID"));
    	    }    	
    	    PCount = Pways.size();
    	    writer.append("," + (EdgeCount-1) + "," + PCount + ",");
    	    for ( Node node : paths.nodes()) {
    	       writer.append("(" + node.getProperty("name") + ") " );
    	    }
    	    writer.append("," + duration + "\n");
            }
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
    

    void createDb() throws IOException
    {
        FileUtils.deleteRecursively( new File( DB_PATH ) );

        // START SNIPPET: startDb
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( "neo4j/data/graph.db" );
        registerShutdownHook( graphDb );
        // END SNIPPET: startDb
        
        String csvFile = "Nodes.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        br = new BufferedReader(new FileReader(csvFile));
        line = br.readLine();
        int i = 0;                          //The Index 
        nodes = new Node[count(csvFile)];
        // START SNIPPET: transaction
        try ( Transaction tx = graphDb.beginTx() )
        {
        	//line = br.readLine();
			while ((line = br.readLine()) != null) {

        	   String[] Entry = line.split(cvsSplitBy);
               // Database operations go here
               // END SNIPPET: transaction
               // START SNIPPET: addData
        	   
               nodes[i] = graphDb.createNode();
               nodes[i].setProperty( "name", Entry[0] );
               nodes[i].setProperty( "type", Entry[1] );
               nodes[i].setProperty( "reaction", Entry[2]);
               nodes[i].setProperty( "PathwayID", Entry[3]);
			   i++;
			}
               
            csvFile = "Edges.csv";
            br = null;
            line = "";
            cvsSplitBy = ",";
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            i = 0;
            while ((line = br.readLine()) != null) {
               String[] Edge = line.split(cvsSplitBy);
               //System.out.println(Edge[0] + " " + Edge[1]); //For Debugging
               int a = findIndex(Edge[0]); //Find the Index of the Source Node
               int b = findIndex(Edge[1]); //Find the Index of the target Node
               if ( a == -1 || b == -1)
                  System.out.println(Edge[0] + " " + a + " " + Edge[1] + " " + b + " " + Edge[4] );
               //If a or b = -1, that means the edge is making a reference to a Node that is not expressed as an Entry in the xml files and should therefore be ignored
               if( a != -1 && b != -1) {
            	  //Create the Relationship between the nodes
                  relationship = nodes[a].createRelationshipTo( nodes[b], RelTypes.CONNECTED );
                  relationship.setProperty( "Type", Edge[2] );
                  relationship.setProperty( "EdgeType", Edge[3] );
               }
               // END SNIPPET: addData
            }

            // START SNIPPET: transaction
            tx.success();
        }
        // END SNIPPET: transaction
    }

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