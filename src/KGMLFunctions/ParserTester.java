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
	public static void main(String[] args) throws IOException {
		String Output="";
		int j=0;
		
		/*try 
		{
		   
		} catch(IOException e)
		{
			System.out.println("**Error**" + e.toString() + "\n");
		}*/
    	PathwayMap Organism = new PathwayMap("/home/edraa/workspace/KGML/Data");
    	//Statistics.GetStats(Organism);
    	//ListGenes.Listgenes(Organism.pathways);
    	//Organism.isConnected();
    	CreateCSV.generateCSVNode(Organism);
    	CreateCSV.generateEdges(Organism);
    	//ListGenes.Listgenes(Organism.pathways);
    	//Organism.isConnected();
    }
	
}
