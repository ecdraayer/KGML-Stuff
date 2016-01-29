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
		String input = "";
		String output = "";
    	int j=0;

    	try {	
    		if (args.length >= 5)
    		{
    			while (j < args.length)
    			{
    				if (args[j].equals("-f"))
    				{
    					if (args[j+1].contains("-")==false)
    						input = args[j+1];
    				}
    				
    				if (args[j].equals("-o"))
    				{
    					if (args[j+1].contains("-")==false){
    						output = args[j+1];
    					}
    				}
    				
    				j++;
    			}
    		}
    		
    		PathwayMap Organism = new PathwayMap(input);
            //Generate CSVFiles of nodes and edges
    		if(args[0].equals("GenerateCSVFiles")) {
    	        CreateCSV.generateCSVNode(Organism, output);
    	        CreateCSV.generateEdges(Organism, output);
    	    }
    		//Generates Stats of Pathways such as number of nodes and edges
    		else if(args[0].equals("GenerateStats")) {
    	        Statistics.GetStats(Organism, output);
    	    } 
    		//Generates the inverted list
    		else if (args[0].equals("InvertList")) {
    			ListGenes.Listgenes(Organism.pathways, output);
    		} 
    		//Finds out which pathways are connected with eac hother.
    		else if (args[0].equals("isConnected")) {
    			ListGenes.Listgenes(Organism.pathways, output); //HashTable needed for isConnected to calculate
    			Organism.isConnected(output);
    		}
    	} catch(Exception e){
    		System.err.println("Error with command line");
    	}
    }
	
}
