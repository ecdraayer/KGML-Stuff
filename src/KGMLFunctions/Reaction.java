/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Reaction class - Stores the information of the reactions of a given pathway  (edge)
 *                   
 *-----------------------------------------------------------------------------*/

package KGMLFunctions;
import java.util.ArrayList;

public class Reaction {
    String id;
    String name;
    String type;
    ArrayList<Substrate> substrate;
    ArrayList<Product> product;
    
    //constructor
    public Reaction(){
    	id = null;
    	name = null;
    	type = null;
    	substrate = new ArrayList<Substrate>();
    	product = new ArrayList<Product>();
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<Substrate> getSubstrate() {
		return substrate;
	}
	public void setSubstrate(ArrayList<Substrate> substrate) {
		this.substrate = substrate;
	}
	public ArrayList<Product> getProduct() {
		return product;
	}
	public void setProduct(ArrayList<Product> product) {
		this.product = product;
	}

	//toString
	@Override
	public String toString(){
		String result = "";
		result += "reaction :: [id=" + id + " name=" + name + " type=" + type + "]";
		for (Substrate tmpS : substrate){
			result += "\n-substrate :: [" + tmpS + "]";
		}
		for (Product tmpP : product){
			result += "\n-product :: [" + tmpP + "]";
		}
		return result;
	}
}
