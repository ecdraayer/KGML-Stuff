import java.util.ArrayList;
import java.util.List;

public class Pathway {
	String name;
	String org;
	String number;
	String title;
	String image;
	String link;
    List<Kegg_Entry> entryL;
    List<Relation> relationL;
    List<Reaction> reactionL;
	
	public Pathway(String name, String org, String number, String title,
			String image, String link) {
		this.name = name;
		this.org = org;
		this.number = number;
		this.title = title;
		this.image = image;
		this.link = link;
        entryL = new ArrayList<Kegg_Entry>();
        relationL = new ArrayList<Relation>();
        reactionL = new ArrayList<Reaction>();
	}

	public Pathway() {
        entryL = new ArrayList<Kegg_Entry>();
        relationL = new ArrayList<Relation>();
        reactionL = new ArrayList<Reaction>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
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

	public List<Kegg_Entry> getEntryL() {
		return entryL;
	}

	public void setEntryL(List<Kegg_Entry> entryL) {
		this.entryL = entryL;
	}

	public List<Relation> getRelationL() {
		return relationL;
	}

	public void setRelationL(List<Relation> relationL) {
		this.relationL = relationL;
	}

	public List<Reaction> getReactionL() {
		return reactionL;
	}

	public void setReactionL(List<Reaction> reactionL) {
		this.reactionL = reactionL;
	}


}
