package Neo4jAlgorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.management.ObjectName;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.jmx.JmxUtils;

import KGMLFunctions.Neo4j.RelTypes;


public class Neo4JShortestPath {

	
	private GraphDatabaseService graphDb;
	public static enum RelTypes implements RelationshipType
	{
	        CONNECTED
	}
	public Neo4JShortestPath(GraphDatabaseService graphDb )
	{
		this.graphDb=graphDb;
	}
/*--------------------------------------------------------------------------
 * Shortest Path Function - Finds the shortest path between two nodes and fills a csv file with related data
 * Input: Reference File - csv file that contains the Nodes that will be used for calculations
 * Output: OutputFile - csv file that the information will be printed on
 *--------------------------------------------------------------------------*/
public void ShortestPath(String ReferenceFile, String OutputFile) throws IOException {
	try ( Transaction tx = graphDb.beginTx() )
	{
		FileWriter writer = new FileWriter(OutputFile); //OutputFile
		writer.append("q1, q2,Path_size, Path_weight, 1KPages\n"); //Header for OutputFile
		
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
		      System.out.println("Source " + s1 + " Destination "+ s2);
		      Node Source;
			  Node Destination; 
		      //if array nodes is filled no need to query DB.	
		      Label myLabel = DynamicLabel.label("Nodes");	
		      Source =graphDb.findNodes(myLabel, "name", s1).next();
		      Destination =graphDb.findNodes(myLabel, "name", s2).next();

		      //Setup for Shortest Path Neo4j function
		      PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(
		    		    PathExpanders.forTypeAndDirection( RelTypes.CONNECTED, Direction.BOTH ), "Weight" );
		      
		      //Setup for Shortest Path Neo4j function
		      /*PathFinder<org.neo4j.graphdb.Path> finder = GraphAlgoFactory.shortestPath(
	          PathExpanders.forTypeAndDirection( RelTypes.CONNECTED, Direction.INCOMING ), 20 );*/ 
		      
		      //count pages before running query
		      Long BeforePages = getFromManagementBean("Page cache", "Faults");
		      WeightedPath paths = finder.findSinglePath( Source, Destination ); //Call Neo4j shortest path function		      
		      Long AterPages = getFromManagementBean("Page cache", "Faults");
		      

		      Long TotalPages = AterPages-BeforePages;
		      //System.out.println("before " + BeforePages + " "+ AterPages + " " + TotalPages);
		      
		      int PathSize = 0;    //Number of different pathways crossed for shortest pathway
	          double PathWeight = 0;
		      
		      if (paths!=null)
		      {
		    	  PathSize = paths.length();
		    	  PathWeight= paths.weight();
		    	  
		    	  writer.append("," + PathSize );
			      writer.append("," + PathWeight +",");

			      for ( Node node : paths.nodes()) {
	 	             writer.append("(" + node.getProperty("name") + ") " );
	 	          }
		      }
		      else
		      {
		    	  writer.append("," + PathSize );
		    	  writer.append("," + PathWeight +",");
		      }
		     
		 
	          writer.append("," + TotalPages + "\n");
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
private Long getFromManagementBean(String Object, String Attribuite )
{
    ObjectName objectName = JmxUtils.getObjectName( graphDb, Object);
    Long value = JmxUtils.getAttribute( objectName, Attribuite );
    
    return value;
}
}