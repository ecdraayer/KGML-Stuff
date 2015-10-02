package KGMLParser;

public class Subtype {
   String name;
   String value;
   public Subtype(String name, String value) {
		this.name = name;
		this.value = value;
	}
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
	
	public String toString(){
		String result = "";
		result += "name=" + name + " value=" + value;
		return result;
	}
   
    

}
