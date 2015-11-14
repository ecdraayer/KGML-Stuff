/*
 * This class creates two CSV files for all the pathways gathered for an Organism.
 * The first CSV file generates all the nodes in the pathways without repeats. The second CSV file 
 * generates all the edges of the pathways. These CSV files are designed to be uploaded
 * to a neo4j database.
 *
 * Written by Erick Draayer
 * 09/26/15 
 */
package KGMLFunctions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class CreateCSV {
	   
	   public static void generateCSVNode(PathwayMap Org)
	   {
	      try
	      {
	    	 
	          FileWriter writer = new FileWriter("Nodes.csv");
	          
	          //This Hashtable is used to avoid inserting repeats into the CSV file key = Entry Name, Value stored is the Entry Name
	          Hashtable<String, String> Entries = new Hashtable<String, String>();
	          
	          writer.append("name, type, reaction, Pathway Number\n"); //HEADER for CSV File
	          
	          //Loop Through all Pathways in the given Organism, and loop through all Entries
	          for ( Pathway P : Org.pathways ) {
	        	  for ( Kegg_Entry E : P.getEntryL() ) {

	             	 String name = E.getName();    //Name of Entry
                    
	        		 //If Entry Name is undefined, concat with pathway Name to make it unique
	        	     if ( name.equals("undefined") ) 
	        		    name = name.concat( P.getName() );
	        	  
	        	     name = name.replace(',','-');
	        	   
	        		 // If Entries does not exists in the Hashtable, write to CSV file and insert to HashTable
	        		 if ( !Entries.contains(E.getName()) ) {	        			
	                    writer.append( name + "," 
	        		                 + E.getType() + "," 
	                    		     + E.getReaction() + ","
	                    		     + P.getNumber() + "\n");
	                    Entries.put( name, name );
	        		 }
	        	  }
	          }
	    
	          //Flush and Close Writer when done 
	          writer.flush();
	          writer.close();
	      } catch(IOException e) {
	 	     e.printStackTrace();
	 	  }
	   }
	   
	   public static void generateEdges(PathwayMap Org)
	   {
		   try
		      {
		          FileWriter writer = new FileWriter("Edges.csv");
		          
		          // Generate Header For CSV File
		          writer.append("source, target, type, edgetype, Pathway Number\n");
		          for ( Pathway P : Org.pathways ) {
			          /* Generate Edges from Relations */
		        	  for ( Relation R : P.getRelationL() ) {
		        		  
		        		 //Get Name of First Entry
		        	     String E1 = FindEntry(P, R.getEntry1());
		        	     
		        	     //If Entry is undefined, concat with Pathway Name
		        	     if ( E1.equals("undefined") )
                            E1 = E1.concat(P.getName());
		        	     E1 = E1.replace(',', '-');
		        	     
		        	     //Get Name of Second Entry
		        	     String E2 = FindEntry(P, R.getEntry2());
		        	     
		        	     //If Entry is undefined, concat with Pathway Name
		        	     if ( E2.equals("undefined") )
	                            E2 = E2.concat(P.getName());	
		        	     E2 = E2.replace(',', '-');
		        	     
		                 writer.append(E1 + "," + E2 + "," + R.getType() + ",Relation," + P.getNumber() + "\n");
		        	  }
		        	  /* Generate Edges from Reactions */
		        	  for ( Reaction R : P.getReactionL() ) {
		        		  for ( Substrate S : R.getSubstrate() ){
		        			  for( Product Pro : R.getProduct() ){ 
		        				  //Get Name of Substrate
		        				  String Sub = S.getName();
		        				  //If Substrate is undefined, concat with Pathway Name
		        				  if ( Sub.equals("undefined") )
		        				     Sub = Sub.concat(P.getName());
		        				  Sub = Sub.replace(',', '-');
		        				  //Get Name of Product
		        				  String Pr = Pro.getName();
		        				  //If Product is undefined, concat with Pathway Name
		        				  if ( Pr.equals("undefined") )
		        				     Pr.concat(P.getName());
		        				  Pr = Pr.replace(',', '-');
		        				  writer.append(Sub + "," + Pr + "," + R.getType() + ",Reaction," + P.getNumber() + "\n");
		        			  }
		        		  }
		        	  }
		          }
		          writer.flush();
		          writer.close();
		      } catch(IOException e) {
		 	     e.printStackTrace();
		 	  }
	   }
	   
	 
	   
	   /* Method to find the name of an entry given its ID number and pathway it belongs to 
	    * This is needed because Relations only give the ID number of an entry which is only relative
	    * to the pathway it belongs to.
	    */
	   private static String FindEntry(Pathway P, String id){
          for(Kegg_Entry K : P.getEntryL()){
        	  if(K.getID().equals(id)) 
                 return K.getName();
          }
          return "error";
	   }
}


