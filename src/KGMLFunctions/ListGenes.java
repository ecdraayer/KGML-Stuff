/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            8/12/15
 * Last Updated:       8/12/15
 * 
 * ListGenes class, has 1 main function: Listgenes, which gives an inverted index of all the unique genes and lists 
 * which pathways they belong to.
 *-----------------------------------------------------------------------------*/

package KGMLFunctions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ListGenes {
	
	public static HashMap<String, ArrayList<String>> map; //Stores the inverted List
	
	public static void Listgenes(ArrayList<Pathway> pathways, String output) throws IOException{
		
		map = new HashMap<String, ArrayList<String>>();
		
		//For All pathways
		for(Pathway cpway : pathways){
		   //For each entry in a pathway
	       for(Kegg_Entry k : cpway.getEntryL()){
	    	  //Create new arraylist 
		   	  ArrayList<String> arraylist = new ArrayList<String>();
	     
		   	  if(map.get(k.getName()) != null && !(map.get(k.getName()).contains(cpway.getName()))){
		             arraylist = map.get(k.getName());	          }
	          
		   	  arraylist.add(cpway.getName());
	          map.put(k.getName(),arraylist);
	        }
	     }
	     printGenes(map, output);
	}
	   
   //Function to gather all information of inverted list and store it in a string for output file
   private static void printGenes(HashMap<String, ArrayList<String>> map, String output) throws IOException{
    	String result="";
  	    
    	Iterator<String> keySetIterator = map.keySet().iterator();
    	
    	while(keySetIterator.hasNext()){
    		String key = keySetIterator.next();
    		result += key + " " + map.get(key) + "\n";
    	}
    	Log(result, output);
    }
    
    //Function to print inverted list to file
    private static void Log(String message, String outputfile) throws IOException{
        PrintWriter out = new PrintWriter(new FileWriter(outputfile, true), true);
        out.write(message);
        out.close();
    }
}
