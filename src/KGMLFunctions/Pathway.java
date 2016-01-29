/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Pathway class, stores data of the KGML file for easy traversal and comparison
 *  -Instance Vars: name, org, number, title, image, link
 *                  List of entries, List of relations, List of reactions
 *                  
 *  Modeled from: http://www.kegg.jp/kegg/xml/docs/
 *  Entries = nodes
 *  Reactions & Relations = edges
 *-----------------------------------------------------------------------------*/

/* 
 * @Erick Draayer
 * 07/21/15
 */

package KGMLFunctions;
import java.util.ArrayList;
import java.util.List;

public class Pathway {
	String name;      /* The Overall Name of the Pathway */
	Boolean visited;  /* New for DFS, used in PathwayMap isConnected Function */
	String org;       /* Organism name */
	String number;    /* Organism number */
	String title;     /* Title of Pathway */
	String image;     /* Link to image file of pathway */
	String link;      /* Link to pathway on KEGG website */
    List<Kegg_Entry> entryL;  /* List of entries in pathway (Nodes) */
    List<Relation> relationL; /* List of relations in pathway (edges) */
    List<Reaction> reactionL; /* List of reactions in pathway (edges) */

	/* constructor for Pathway */
	public Pathway(String name, String org, String number, String title,
	    String image, String link) {
		this.name = name;
		this.org = org;
		this.number = number;
		this.title = title;
		this.image = image;
		this.link = link;
		this.visited = false;
        entryL = new ArrayList<Kegg_Entry>();
        relationL = new ArrayList<Relation>();
        reactionL = new ArrayList<Reaction>();
	}

	/* No argument constructor */
	public Pathway() {
        entryL = new ArrayList<Kegg_Entry>();
        relationL = new ArrayList<Relation>();
        reactionL = new ArrayList<Reaction>();
	}
	
	/* Getters and Setters for instance variables */
	public Boolean getVisited(){return visited;}
	public void setVisited(Boolean visited){this.visited = visited;}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public String getOrg() {return org;}
	public void setOrg(String org) {this.org = org;}
	
	public String getNumber() {return number;}
	public void setNumber(String number) {this.number = number;}
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
	public String getImage() {return image;}
	public void setImage(String image) {this.image = image;}
	
	public String getLink() {return link;}
	public void setLink(String link) {this.link = link;}
	
	public List<Kegg_Entry> getEntryL() {return entryL;}
	public void setEntryL(List<Kegg_Entry> entryL) {this.entryL = entryL;}
	
	public List<Relation> getRelationL() {return relationL;}
	public void setRelationL(List<Relation> relationL) {this.relationL = relationL;}
	
	public List<Reaction> getReactionL() {return reactionL;}
	public void setReactionL(List<Reaction> reactionL) {this.reactionL = reactionL;}
	
	/* toString for Pathway */
	@Override
	public String toString(){
		String result = "";
		result += "Pathway :: [name=" + name + " org=" + org + " number=" + number + "\n";
	    result += "            title=" + title + "\n";
	    result += "            image=" + image + "\n";
	    result += "            link=" + link + "]\n";
   
        for (Kegg_Entry tmpA : entryL) {
            result += tmpA + "\n";
        }
        for (Relation tmpB : getRelationL()){
        	result += tmpB + "\n";
        }
        for (Reaction tmpC : getReactionL()){
        	result += tmpC + "\n";
        }
	    return result;
	}
	
	/* Function to get the name of an entry (node) from the ID number given
	 * Input: String ID - number of entry
	 * Output: String name - name of the entry
	 *  */
	public String GetNameFromId(String id){
	  	String name = "";
	  	/* Loop through all kegg entries from pathway and find matching ID to input */
	    for(Kegg_Entry k : entryL){
	       if(id.equals(k.getID()))
	  	      name=k.getName();
	  	}
	    return name;
    }
	
	/* Function to get the name of a Pathway from the given name of a Entry (node). This is to check in the kegg entry is in the pathway
	 * Input: String name - name of entry
	 * Output: String name - name of the Pathway
	 *  */	
	public String GetPathwayNameFromGene(String name){
	    String pathname = "";
	  	/* Loop through all kegg entries from pathway and find matching name to input */
	    for(Kegg_Entry k : entryL){
	       if(name.equals(k.getName()))
	  	   {
	  	      pathname=this.name;
	  		  break; 
	  	   }
	  	}
	    return pathname;
    }
	
	/*
    public Kegg_Entry GetPathwayFromGene(String name){
        Kegg_Entry entry = new Kegg_Entry(); 
	  	   for(Kegg_Entry k : entryL){
	  	      if(name.equals(k.getName()))
	  		  {
	  		     entry=k;
	  			 break; 
	  	 	  }
	  	   }
	  	return entry;
    }*/

}
