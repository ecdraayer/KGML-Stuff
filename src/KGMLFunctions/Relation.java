package KGMLFunctions;
import java.util.ArrayList;


public class Relation {
    String entry1;
    String entry2;
    String type;
    ArrayList<Subtype> subtypes;
    
    public Relation(){
    	subtypes = new ArrayList<Subtype>();
    	entry1 = null;
    	entry2 = null;
    	type = null;
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
	public String toString(){
		String result = "";
		result += "relation :: [entry1=" + entry1 + " entry2=" + entry2 + "type=" + type + "]";
	       for (Subtype tmpS : subtypes){
	    	   result += "\n-subtype :: [" + tmpS + "]";
	       }
		return result;
	}

   
}
