/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Wrapper to test the KGML parser and writer
 *-----------------------------------------------------------------------------*/
package KGMLParser;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class ParserTester {
	/* Main function to call parse and write for testing */
	public static void main(String[] args) throws IOException {
    	final File folder = new File("/home/edraa/Desktop/Writer_Input"); /*Destination of output file */
		ArrayList<Pathway> pathways = new ArrayList<Pathway>(); /*Creates 100 empty Pathways */

    	for(final File fileEntry : folder.listFiles()){ /*For loop to go through all KGML files in folder(configured so that KGML files must be in same folder as java program */
    		Pathway pathway = new Pathway();
    		new MySaxParser(fileEntry.toString(), pathway); /* Call parser */
            pathways.add(pathway);   		
            //new KGMLWrite(pathways[i]); /*Write the Pathway again */
    	}
    	ListGenes.Listgenes(pathways);   
    	ListGenes.Query("aml:100467132", "bye");
	}
	
	/* function to test that KGML files are being detected */
	/*public static void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
	    }
	}*/

}
