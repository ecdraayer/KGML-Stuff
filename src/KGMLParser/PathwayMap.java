package KGMLParser;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import org.apache.commons.io.filefilter.FileFileFilter;

import com.mxgraph.model.mxGraphModel.Filter;


public class PathwayMap {
	public ArrayList<Pathway> pathways; /*Stores all the pathways that our organism has*/
	ArrayList<ArrayList<Pathway>> connections; /*Stores the information on which pathways are connected to eachother */
	ArrayList<Pathway> sublist; /*Sublist for the connections instance variable */

	public PathwayMap(String path){
		final File folder = new File(path); /*Destination of output file /home/edraa/Documents/Research/MapData/Bacteria_879462.4.PATRIC*/
		
		//return only xml files
		FileFilter xmlFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".xml");
			}
		};
		pathways = new ArrayList<Pathway>(); 
        connections = new ArrayList<ArrayList<Pathway>>();
		sublist = new ArrayList<Pathway>();
        
    	for(final File fileEntry : folder.listFiles(xmlFilter)){ /*For loop to go through all KGML files in folder(configured so that KGML files must be in same folder as java program */
    	
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
	 
	 public void getTotalStats(){
		 int totalCompounds = 0;
		 int totalGenes = 0;
		 int totalReactions = 0;
		 int totalRelations = 0;
		 for(Pathway ptmp : pathways){	
			 totalCompounds += ptmp.compoundNum;
			 totalGenes += ptmp.geneNum;
			 totalReactions += ptmp.reactionNum;
			 totalRelations += ptmp.relationNum;
		 }		 
		 System.out.println("Total Compounds: " + totalCompounds);
		 System.out.println("Total Genes: " + totalGenes);
		 System.out.println("Total Nodes: " + (totalCompounds+ totalGenes));
		 System.out.println("Total Reactions: " + totalReactions);
		 System.out.println("Total Relations: " + totalRelations);
		 System.out.println("Total Edges: " + (totalReactions + totalRelations));
	 }

}
