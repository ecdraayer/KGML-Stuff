/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * PathwayMap class - stores all pathways of a given organism in an ArrayList of Pathways
 * This class also holds basic functions to determine properties of the Pathways of the Organism
 *                  
 *  Modeled from: http://www.kegg.jp/kegg/xml/docs/
 *  Entries = nodes
 *  Reactions & Relations = edges
 *-----------------------------------------------------------------------------*/

package KGMLFunctions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import java.io.FileWriter;

/* Class to store all Pathways of a single Organism in an ArrayList of Pathways.*/
public class PathwayMap {
	public ArrayList<Pathway> pathways;        // Stores all the pathways that our organism has
	ArrayList<ArrayList<Pathway>> connections; // Stores the information on which pathways are connected to each other 
	ArrayList<Pathway> sublist;                // Sublist for the connections ArrayList, used for the DFS in isConnected Function below

	
    /*--------------------------------------------------------------------------
     * PathwayMap Constructor
     * Input - String path, The Folder name containing all the xml files that have the information of all the pathways of the Organism
     * Output - The Data structure of the Pathways information loaded into memory in the form of ArrayLists
     *--------------------------------------------------------------------------*/
	public PathwayMap(String path){
		
		//Get the pathway of the folder given (String path)
		final File folder = new File(path); 
		
		//return only XML files
		FileFilter xmlFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".xml");
			}
		}; 
		
		pathways = new ArrayList<Pathway>();                  /* Declare new ArrayList of pathways */
        connections = new ArrayList<ArrayList<Pathway>>();    /* Declare new ArrayList of connections */
		sublist = new ArrayList<Pathway>();                   /* Declare new ArrayList sublist */
        
		// for loop to go through all KGML files in folder(configured so that KGML files must be in same folder as java program 
    	for(final File fileEntry : folder.listFiles(xmlFilter)){  
    		Pathway pathway = new Pathway();
    		new MySaxParser(fileEntry.toString(), pathway); //Parse each KGML file
            pathways.add(pathway);   		                //Add to ArrayList of pathways
    	}  
	}
	
	
    // To String for PathwayMap 
	@Override
	public String toString(){
		String str = "";
		for(int i=0; i < pathways.size(); i++)
			str += pathways.get(i).toString() + "\n";
		return str;
		
	}
	
	/*--------------------------------------------------------------------------
     * isConnected Function, Tests if all the pathways of a given organism are connected with eachother
     * Input - String Output, the output file name
     * Output - txt file that tells which pathways are connected with eachother
     *--------------------------------------------------------------------------*/	
	public void isConnected(String Output) throws IOException{
		
		String content = "";  //output string
        FileWriter writer = new FileWriter(Output);
 	    BufferedWriter bw = new BufferedWriter(writer);

 	    //For all pathways that make up the organism
		for(Pathway ptmp : pathways){	
			//Create new sublist of pathways (pathways connected to each other)
			sublist = new ArrayList<Pathway>(); 
			
			//if pathway has not been visited, do a DFS from that node
			if(!ptmp.getVisited()){
				DFS(pathways,ptmp);
		        this.connections.add(sublist); //Add sublist to connections ArrayList
			}
		 }
		 
		 //if connections are greater than 1, then not all pathways are connected
	     if(connections.size() > 1){
	    	 content += "Not all pathways connected\nFirst Group of Connections:\n\n";
	    	 //Get all groups of connections to print for output
	    	 for(int i=0; i<connections.size(); i++){
			     for(int j=0; j<(connections.get(i)).size(); j++){
			    	 content += (connections.get(i)).get(j).getName() + " ";
			     }
			     content += "\n\nNext Group of Connections: \n\n";
	    	 }	 
	     //All pathways are connected
		 } else{
              content += "All pathways connected\nGroup of Connections:\n\n"; 
	    	  //Get list of all pathways connected
              for(int i=0; i<connections.size(); i++){
			      for(int j=0; j<(connections.get(i)).size(); j++){
			    	  content += (connections.get(i)).get(j).getName() + " ";
			      }
	    	  }
		 }
	     
	     //Write output file
    	 bw.write(content);
         bw.close();

	 }
	
	/*--------------------------------------------------------------------------
     * depth first search function
     * Input - ArrayList pathways, Pathway p
     * Output - none
     *--------------------------------------------------------------------------*/	
     public void DFS(ArrayList<Pathway> pathways, Pathway p){
         //if pathway has already been visited, return to caller
	  	 if(p.getVisited())
	  		 return;
	  	
	     this.sublist.add(p); //add pathway to sublist of connected pathways
	  	 p.visited = true;    //mark pathway as visited
	  	 
	  	 //loop through all entries in pathway
	  	 for(Kegg_Entry E : p.getEntryL()){
	  		 //if entry is not a compound, then continue doing DFS
	  		 if(!(/*E.graphInfo.bgcolor.equals("#FFFFFF") || */E.getType().equals("compound")) ){
	  			 //Loop through all pathways that are in inverted list found in ListGenes.java
	  			 for(String pathwayname : ListGenes.map.get(E.getName())){
	  				 DFS(pathways, pathways.get(getIndexof(pathwayname)));
	  			 }
	  		 }
	  	  }
     }
 
     /*public void printConnections(ArrayList<ArrayList<Pathway>> connections){
    	 for(int i=0; i<connections.size(); i++){
		     for(int j=0; j<(connections.get(i)).size(); j++){
		    	 System.out.print((connections.get(i)).get(j).getName() + " ");
		     }
		     System.out.println();
	     }
     }*/
     
     /*--------------------------------------------------------------------------
      * Function to get the index number of a given Pathway name from the ArrayList of Pathways
      * Input - String p, the name of the pathway
      * Output - int, the index number of where the pathway is stored in the ArrayList
      *--------------------------------------------------------------------------*/
	 public int getIndexof(String p){
	  	 int i = 0;
	  	 for(Pathway pway : pathways){
	  		 if(p.equals(pway.getName()))
	  			 break;
	  		 i++;
	  	 }
	  	 return i;
     }

}
