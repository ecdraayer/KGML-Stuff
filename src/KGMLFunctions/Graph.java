/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Graph class - Stores the information of the Graph Information of a entry (Node in KGML file)
 *                   
 *-----------------------------------------------------------------------------*/
package KGMLFunctions;

public class Graph {
    String name;
    String fgcolor; //foreground color
    String bgcolor; //background color
    String type;
    String x;
    String y;
    String width;
    String height;
    
    //constructor
    public Graph(String name, String fgcolor, String bgcolor, String type, String x, String y, String width, String height){
    	if (x == null)
    		x="0";
    	if (y == null)
    		y="0";
    	this.name = name;
    	this.fgcolor = fgcolor;
    	this.bgcolor = bgcolor;
    	this.type = type;
    
    	this.x = x;
    	this.y = y;
    	this.width = width;
    	this.height = height;
    }
    
    //getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFgcolor() {
		return fgcolor;
	}
	public void setFgcolor(String fgcolor) {
		this.fgcolor = fgcolor;
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	
	//toString
    @Override
	public String toString(){
    	String result = "";
    	result += "name=" + name + " fgcolor=" + fgcolor + " bgcolor=" + bgcolor + " type=" + type + " x=" + x + " y=" + y + " width=" + width + " height=" + height;
    	return result;
    }
   
}
