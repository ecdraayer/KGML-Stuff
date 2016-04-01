package RunQueryOnIndex;

import java.io.IOException;
import org.neo4j.graphdb.GraphDatabaseService;
import KGMLFunctions.Neo4j;
import Neo4jAlgorithms.Neo4JShortestPath;

public class Neo4jQueryOnIndex  {
	
	public static void main(String[] args) {
		
		
		Neo4j NeoDb = new Neo4j();
		String OutputFile = "Neo4jPages.csv";
		String ReferenceFile = "Bacteria_879462.4.PATRIC/xmls/GenesToQuery.csv";
		try {
			GraphDatabaseService graphDb = NeoDb.startDb();
			
			Neo4JShortestPath ShortestPath = new Neo4JShortestPath(graphDb);
			ShortestPath.ShortestPath(ReferenceFile, OutputFile);
			
			
			NeoDb.shutDown();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("error " + e);
			NeoDb.shutDown();	
		}
		
		
		
	}
}
