/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Wrapper to test the KGML parser and writer
 *-----------------------------------------------------------------------------*/
package KGMLFunctions;

import java.io.IOException;

public class ParserTester {
	
	/* Main function to call parse and write for testing */
	public static void main(String args[]) throws IOException {
		System.out.println(args[0]);
		String input = args[0];
    	PathwayMap Organism = new PathwayMap(input);
    	
    	if(args[1] == "GenerateCSVFiles") {
    	   String OutputCSV = args[3];
           CreateCSV.generateCSVNode(Organism, OutputCSV);
           CreateCSV.generateEdges(Organism, OutputCSV);
    	}
    	
    	if(args[1].equals("GenerateStats")) {
     	   Statistics.GetStats(Organism);
    	}
    	
    	//Generates Inverted List
    	//ListGenes.Listgenes(Organism.pathways);
    	
    	//Generates List to tell you which pathways are connected
    	//Organism.isConnected();
    }
	
}
