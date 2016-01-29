/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Substrate class - Stores the information of the substrate of a given pathway (part of a reaction edge)
 *                   
 *-----------------------------------------------------------------------------*/

package KGMLFunctions;

public class Substrate {
	String id;
	String name;
	
	//constructors
	public Substrate(){
		this.id = "";
		this.name = "";
	}
	public Substrate(String id, String name){
		this.id = id;
		this.name = name;
	}
	
	//getters and setters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//toString
	@Override
	public String toString(){
		String result = "";
		result += "id=" + id + " name=" + name;
		return result;
	}
}
