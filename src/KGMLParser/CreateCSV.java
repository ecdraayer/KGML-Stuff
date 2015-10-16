/*
 * This class creates two CSV files for all the pathways gathered for an Organism.
 * The first CSV file generates all the nodes in the pathways. The second CSV file 
 * generates all the edges of the pathways. These CSV files are designed to be uploaded
 * to a neo4j database.
 *
 * Written by Erick Draayer
 * 09/26/15 
 */
package KGMLParser;

import java.io.FileWriter;
import java.io.IOException;

public class CreateCSV {
	
	   public static void generateCSVNode(PathwayMap Org)
	   {
	      try
	      {
	          FileWriter writer = new FileWriter("Nodes.csv");
	          
	          writer.append("name, type, reaction\n");
	          for ( Pathway P : Org.pathways ) {
	        	  for ( Kegg_Entry E : P.getEntryL() ) {
	                 writer.append(E.getName() + "," + E.getType() + "," + E.getReaction() + "\n");
	        	  }
	          }
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
		          
		          writer.append("source, target, type, edgetype\n");
		          for ( Pathway P : Org.pathways ) {
			          /* Generate Edges from Relations */
		        	  for ( Relation R : P.getRelationL() ) {
		                 writer.append(FindEntry(P, R.getEntry1()) + "," + FindEntry(P, R.getEntry2()) + "," + R.getType() + ", Relation\n");
		        	  }
		        	  /* Generate Edges from Reactions */
		        	  for ( Reaction R : P.getReactionL() ) {
		        		  for ( Substrate S : R.getSubstrate() ){
		        			  writer.append(S.getName() + " ");
		        		  }
		        		  writer.append(",");
		        		  for ( Product Pro : R.getProduct() ){
		        			  writer.append(Pro.getName() + " ");
		        		  }
		        		  writer.append("," + R.getType() + ", Reaction\n");
		        	  }
		          }
		          writer.flush();
		          writer.close();
		      } catch(IOException e) {
		 	     e.printStackTrace();
		 	  }
	   }
	   
	   /* Method to find the name of an entry given its ID number and pathway it belongs to */
	   private static String FindEntry(Pathway P, String id){
          for(Kegg_Entry K : P.getEntryL()){
        	  if(K.getID().equals(id)) 
                 return K.getName();
          }
          return "error";
	   }
}
