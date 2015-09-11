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

package KGMLParser;/* 
 * @Erick Draayer
 * 07/21/15
 */

import java.util.ArrayList;
import java.util.List;

public class Pathway {
	String name;
	Boolean visited;  /*New for DFS */
	String org;
	String number;
	String title;
	String image;
	String link;
    List<Kegg_Entry> entryL;
    List<Relation> relationL;
    List<Reaction> reactionL;
	int compoundNum = 0;
	int geneNum = 0;
	int relationNum = 0;
	int reactionNum = 0;

	
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
	
	public int getCompoundNum(){return compoundNum;}
	public void setCompoundNum(int c){this.compoundNum = c;}
	public int getGenesNum(){return geneNum;}
	public void setGenesNum(int g) {this.geneNum = g;}
	public int getRelationNum(){return relationNum;}
	public void setRelationNum(int r){this.relationNum = r;}
	public int getReactionNum(){return reactionNum;}
	public void setReactionNum(int re){this.reactionNum = re;}
	
	public Boolean getVisited(){return visited;}
	public void setVisited(Boolean visited){this.visited = visited;}
	/* Getters and Setters for instance variables */
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
	
	/* Prints out Pathway */
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
	 public String GetNameFromId(String id){
	  	 String name = "";
	  	 for(Kegg_Entry k : entryL){
	  		 if(id.equals(k.getID()))
	  			name=k.getName();
	  	 }
	  	 return name;
     }
	 public String GetXvalue(String name){
	  	 String x = "";
	  	 for(Kegg_Entry k : entryL){
	  		 if(name.equals(k.getName()))
	  			x=k.graphInfo.x;
	  	 }
	  	 return x;
     }
	 
	 public void getStats(){
		 System.out.println(name + " Stats:");
		 System.out.println("Compounds: " + compoundNum);
		 System.out.println("Genes: " + geneNum);
		 System.out.println("Relations: " + relationNum);
		 System.out.println("Reactions: " + reactionNum);
	 }

}
