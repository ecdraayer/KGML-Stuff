/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Wrapper to test the KGML parser and writer
 *-----------------------------------------------------------------------------*/
package KGMLParser;

import java.io.IOException;

public class ParserTester {
	

	
	/* Main function to call parse and write for testing */
	public static void main(String[] args) throws IOException {
    	PathwayMap Organism = new PathwayMap();
    	ListGenes.Listgenes(Organism.pathways);
    	//Organism.isConnected();
    }
	
}
