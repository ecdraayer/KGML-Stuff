package RunQueryOnIndex;

import KGMLFunctions.Neo4j;

public class Neo4jQueryOnIndex  {
	private static final String DB_PATH = "neo4j/data/graph.db";
	
	public static void main(String[] args) {
		
		
		Neo4j NeoDb = new Neo4j();
		NeoDb.deleteDB();
	}
}
