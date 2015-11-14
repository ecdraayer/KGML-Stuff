/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            8/12/15
 * Last Updated:       8/12/15
 * 
 * ListGenes class, has 2 main functions: Listgenes, which gives an inverted index of all the unique genes and lists 
 * which pathways they belong to.
 * Query: tells you if two entries are directly connected.
 *-----------------------------------------------------------------------------*/

package KGMLFunctions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ListGenes {
	
	public static HashMap<String, ArrayList<String>> map;
	
	public static void Listgenes(ArrayList<Pathway> pathways) throws IOException{
		map = new HashMap<String, ArrayList<String>>();
		for(Pathway cpway : pathways){
	       for(Kegg_Entry k : cpway.getEntryL()){
		   	  ArrayList<String> arraylist = new ArrayList<String>();
	          if(map.get(k.getName()) != null && !(map.get(k.getName()).contains(cpway.getName()))){
		             arraylist = map.get(k.getName());	          }
	          arraylist.add(cpway.getName());
	          map.put(k.getName(),arraylist);
	        }
	     }
	     printGenes(map);
	}
	   
   private static void printGenes(HashMap<String, ArrayList<String>> map) throws IOException{
    	String result="";
    	Iterator<String> keySetIterator = map.keySet().iterator();
    	
    	while(keySetIterator.hasNext()){
    		String key = keySetIterator.next();
    		result += key + " " + map.get(key) + "\n";
    	}
    	Log(result);
    }
    
    private static void Log(String message) throws IOException{
    	PrintWriter pw = new PrintWriter("InvertedList.txt");
    	pw.close();
        PrintWriter out = new PrintWriter(new FileWriter("InvertedList.txt", true), true);
        out.write(message);
        out.close();
    }
}
