package KGMLFunctions;

import java.util.Hashtable;

public class Statistics {
	
   public static void GetStats(PathwayMap Org) { 
	   int Compounds = 0;   //Total Number of Compouds
	   int Genes = 0;       //Total Number of Genes
	   int TotalNodes = 0;  //Total Number of Nodes in Graph
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
     	        
     		 // If Entries does not exists in the Hashtable, write to CSV file and insert to HashTable
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
       System.out.println("Total Compounds: " + Compounds);
	   System.out.println("Total Genes: " + Genes);
	   System.out.println("Total Nodes: " + TotalNodes);      
       
	   int reactions = 0;
	   int relations = 0;
	   
       // Generate Header For CSV File
       for ( Pathway P : Org.pathways ) {
	      /* Count Edges from Relations */
     	  for ( Relation R : P.getRelationL() ) {
     		  relations++;
     	  }
     	  
     	  /* Count Edges from Reactions */
     	  for ( Reaction R : P.getReactionL() ) {
     		  for ( Substrate S : R.getSubstrate() ){
     			  for( Product Pro : R.getProduct() ){ 
     				  reactions++;
     			  }
     		  }
     	  }  	  
       } 
       
       System.out.println("Total Reactions: " + reactions);
       System.out.println("Total Relations: " + relations);
       System.out.println("Total Edges: " + (reactions + relations));
       
   }
}
