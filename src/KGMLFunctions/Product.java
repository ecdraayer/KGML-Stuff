/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Product class - Stores the information of the products of a KGML file (part of an edge)
 *                   
 *-----------------------------------------------------------------------------*/

package KGMLFunctions;
public class Product {
	String id;   //product number
	String name; //product name
	
	//constructor
	public Product(String id, String name){
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
