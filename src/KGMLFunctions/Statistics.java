package KGMLFunctions;

import java.util.Hashtable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Statistics {
	
   /* This function takes two inputs-the data structure that stores the organism and the name of the output file */
   public static void GetStats(PathwayMap Org, String output) throws IOException { 
	   
	   int Compounds = 0;   //Total Number of Compouds
	   int Genes = 0;       //Total Number of Genes
	   int TotalNodes = 0;  //Total Number of Nodes in Graph
       File file = new File(output); //Output File
	   String content = ""; //The content of the output file
       
	   FileWriter fw = new FileWriter(file.getAbsoluteFile());
	   BufferedWriter bw = new BufferedWriter(fw);
	   
	   
	   //This Hashtable is used to avoid counting repeats in the CSV File
       Hashtable<String, String> Entries = new Hashtable<String, String>();
       
       //Loop Through all Pathways in the given Organism, and loop through all Entries
       //Find Total Number of Nodes that will be in Graph
       for ( Pathway P : Org.pathways ) {
          for ( Kegg_Entry E : P.getEntryL() ) {

          	 String name = E.getName();    //Name of Entry

     		 //If Entry Name is undefined, concat with pathway Name to make it unique
     	     if ( name.equals("undefined") ) 
     		    name = name.concat( P.getName() );
     	        
     		 // If Entries does not exists in the Hashtable, insert to HashTable and count nodes
     		 if ( !Entries.contains(E.getName()) ) {	
     		    if ( E.getType().equals("compound") )
     		       Compounds++;
     		    if ( E.getType().equals("ortholog") )
     		       Genes++;
     		    
                TotalNodes++;
                Entries.put( name, name );
     		 }
     	  } 
       }
       content += "Total Compounds: " + Compounds + "\n";
       content += "Total Genes: " + Genes + "\n";
	   content += "Total Nodes: " + TotalNodes + "\n";     
       
	   int reactions = 0;
	   int relations = 0;
	   
       // Generate Header For CSV File
       for ( Pathway P : Org.pathways ) {
    	   
	      /* Count Edges from Relations */
     	  for ( Relation R : P.getRelationL() ) {
     		  relations++;
     	  }
     	  
     	  /* Count Edges from Reactions (Number of Substrates * Number of Products) for each reaction */
     	  for ( Reaction R : P.getReactionL() ) {
     		  for ( Substrate S : R.getSubstrate() ){
     			  for( Product Pro : R.getProduct() ){ 
     				  reactions++;
     			  }
     		  }
     	  }  	  
       } 
       
       content += "Total Reactions: " + reactions + "\n";
       content += "Total Relations: " + relations + "\n";
       content += "Total Edges: " + (reactions + relations) + "\n";
       
       bw.write(content);
       bw.close();
       
       
   }
}
