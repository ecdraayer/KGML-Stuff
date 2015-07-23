/**
 * Kegg XML entry to class to store information on each entry in the KGML files
 * @author edraa
 *
 */
public class Kegg_Entry {
	String id;
	String name;
	String type;
	String reaction;
	String link;
	//Graph graphInfo;
	//Getters and Setter
	public String getID(){return id;}
	public void setID(String id){this.id = id;}
	
	public String getName(){return name;}
    public void setName(String name){this.name = name;}
    
    public String getType(){return type;}
    public void setType(String type){this.type = type;}
    
    public String getReaction(){return reaction;}
    public void setReaction(String reaction){this.reaction = reaction;}
    
    public String getLink(){return link;}
    public void setLink(String link){this.link = link;}
    
    /*public void setGraph(String name, String fgcolor, String bgcolor, String type, String x, String y, String width, String height){
    	graphInfo = new Graph(name,fgcolor,bgcolor,type,x,y,width,height);
    }*/
    
    public String toString(){
    	String result = "";
    	result += "Entry :: ";
    	result += "[ID = " + id + " Name = " + name + " Type = " + type + " Reaction = " + reaction + " Link = " + link + "]";
    	return result;
    }
}
