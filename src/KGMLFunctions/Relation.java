/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Relation class - Stores the information of the relation of a given pathway (edge)
 *                   
 *-----------------------------------------------------------------------------*/

package KGMLFunctions;
import java.util.ArrayList;


public class Relation {
    String entry1;
    String entry2;
    String type;
    int weight;
    ArrayList<Subtype> subtypes;
    
    //constructor
    public Relation(){
    	subtypes = new ArrayList<Subtype>();
    	entry1 = null;
    	entry2 = null;
    	type = null;
    	weight=0;
    }
    
    //getters and setters
    public int getWeight() {
		return weight;
	}
	public String getEntry1() {
		return entry1;
	}
	public void setEntry1(String entry1) {
		this.entry1 = entry1;
	}
	public String getEntry2() {
		return entry2;
	}
	public void setEntry2(String entry2) {
		this.entry2 = entry2;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<Subtype> getSubtypes() {
		return subtypes;
	}
	public void setSubtypes(ArrayList<Subtype> subtypes) {
		this.subtypes = subtypes;
	}
	
	//toString
	@Override
	public String toString(){
		String result = "";
		result += "relation :: [entry1=" + entry1 + " entry2=" + entry2 + "type=" + type + "]";
	       for (Subtype tmpS : subtypes){
	    	   result += "\n-subtype :: [" + tmpS + "]";
	       }
		return result;
	}

   
}
