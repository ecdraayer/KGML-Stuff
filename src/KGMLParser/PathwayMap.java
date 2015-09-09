package KGMLParser;

import java.io.File;
import java.util.ArrayList;


public class PathwayMap {
	public ArrayList<Pathway> pathways; /*Stores all the pathways that our organism has*/
	ArrayList<ArrayList<Pathway>> connections; /*Stores the information on which pathways are connected to eachother */
	ArrayList<Pathway> sublist; /*Sublist for the connections instance variable */
	
	public PathwayMap(){
		final File folder = new File("C:\\Users\\Raul\\Desktop\\Project\\KGML-Stuff\\Bacteria - 879462.4.PATRIC\\xmls"); /*Destination of output file /home/edraa/Documents/Research/MapData/Bacteria_879462.4.PATRIC*/
		pathways = new ArrayList<Pathway>(); 
        connections = new ArrayList<ArrayList<Pathway>>();
		sublist = new ArrayList<Pathway>();
        
    	for(final File fileEntry : folder.listFiles()){ /*For loop to go through all KGML files in folder(configured so that KGML files must be in same folder as java program */
    		Pathway pathway = new Pathway();
    		new MySaxParser(fileEntry.toString(), pathway); /* Call parser */
            pathways.add(pathway);   		
    	}
	}
	
	public String toString(){
		String str = "";
		for(int i=0; i < pathways.size(); i++)
			str += pathways.get(i).toString() + "\n";
		return str;
		
	}
	
	public void isConnected(){
		for(Pathway ptmp : pathways){	
			sublist = new ArrayList<Pathway>();
			if(!ptmp.getVisited()){
				DFS(pathways,ptmp);
		        this.connections.add(sublist);
			}
		 }
		  
	     if(connections.size() > 1){
	    	 System.out.println("no");
	    	 printConnections(connections);
		 } else{
			 System.out.println("yes");
	         printConnections(connections);
		 }

	 }
     public void DFS(ArrayList<Pathway> pathways, Pathway p){
	  	 if(p.getVisited())
	  		 return;
	     this.sublist.add(p);
	  	 p.visited = true;
	  	 
	  	 for(Kegg_Entry E : p.getEntryL()){
	  		 if(!(E.graphInfo.bgcolor.equals("#FFFFFF")) ){
	  			 for(String pathwayname : ListGenes.map.get(E.getName())){
	  				 DFS(pathways, pathways.get(getIndexof(pathwayname)));
	  			 }
	  		 }
	  	  }
     }
 
     public void printConnections(ArrayList<ArrayList<Pathway>> connections){
    	 for(int i=0; i<connections.size(); i++){
		     for(int j=0; j<(connections.get(i)).size(); j++){
		    	 System.out.print((connections.get(i)).get(j).getName() + " ");
		     }
		     System.out.println();
	     }
     }

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
