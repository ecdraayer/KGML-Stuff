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
	          
	          writer.append("name, id, type, reaction\n");
	          for ( Pathway P : Org.pathways ) {
	        	  for ( Kegg_Entry E : P.getEntryL() ) {
	                 writer.append(E.getName() + "," + E.getID() + "," + E.getType() + "," + E.getReaction() + "\n");
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
		        	  for ( Relation R : P.getRelationL() ) {
		                 writer.append(R.entry1 + "," + R.getEntry2() + "," + R.getType() + ", Relation\n");
		        	  }
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
}
