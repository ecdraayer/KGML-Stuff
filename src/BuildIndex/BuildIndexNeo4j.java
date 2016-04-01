package BuildIndex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.tooling.GlobalGraphOperations;

import KGMLFunctions.Neo4j;
import Neo4jAlgorithms.Neo4JShortestPath.RelTypes;

public class BuildIndexNeo4j {
	public static void main(String[] args) {
		
		Neo4j NeoDb = new Neo4j();
		
		File file = new File("idx.csv");
		file.delete();
	
		try {
			GraphDatabaseService graphDb = NeoDb.startDb();
			
			ArrayList<String> LandMarks = new ArrayList<String> ();
			//LandMarks.add("ko:K09458");
			//LandMarks.add("ko:K11533");
			
			LandMarks.add("80");
			LandMarks.add("39");
			DistancesFromLandmark(graphDb, LandMarks);
	
			
			NeoDb.shutDown();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("error " + e);
			NeoDb.shutDown();	
		}
	}
	
	private static void DistancesFromLandmark(GraphDatabaseService graphDb, ArrayList<String> landMarks)
	{

		try ( Transaction tx = graphDb.beginTx() )
		{
			String[][] Distances  = new String[(IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllNodes())* landMarks.size()) ][3];
			 int j=0;
			for (int i=0; i < landMarks.size();i++)
			{
				Node Landmark;
			      //find neo4j id for landmark nodes
			    Label myLabel = DynamicLabel.label("Nodes");	
			    Landmark =graphDb.findNodes(myLabel, "name", landMarks.get(i)).next();

			    
			    //Setup for Shortest Path Neo4j function
			    PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(
			    		    PathExpanders.forTypeAndDirection( RelTypes.CONNECTED, Direction.BOTH ), "Weight" );
			    
			    ResourceIterator<Node> iter = GlobalGraphOperations.at(graphDb).getAllNodes().iterator();
			   
			
			    while (iter.hasNext())
			    {
			    	Node Destination = iter.next();
			    	


			    	WeightedPath paths = finder.findSinglePath( Landmark, Destination ); //Call Neo4j shortest path function

			    	
			    	Distances[j][0]=Landmark.toString();
					Distances[j][1]=Destination.toString();
					
					if (paths!=null)
						Distances[j][2]= Double.toString(paths.weight());
					else
						Distances[j][2] =Double.toString( Double.POSITIVE_INFINITY);
					
					if (paths!=null)
						System.out.println("From: " +Landmark.getProperties("name") + " to: " + Destination.getProperties("name") + " weight " +  paths.weight() + " " + paths.endNode());
			    	
			    	j++;
			  
			    }
			    
			    
			}
			
			WriteIndex write = new WriteIndex();
			write.WriteIdx(Distances);
		}
	
	
	}
}
