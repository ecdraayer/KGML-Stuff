/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Subtype class - Stores the information of the subtype of a given pathway (part of a relation edge)
 *                   
 *-----------------------------------------------------------------------------*/
package KGMLFunctions;

public class Subtype {
   String name;
   String value;
   
   //constructor
   public Subtype(String name, String value) {
		this.name = name;
		this.value = value;
	}
   
    //getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	//toString
	@Override
	public String toString(){
		String result = "";
		result += "name=" + name + " value=" + value;
		return result;
	}
   
    

}
