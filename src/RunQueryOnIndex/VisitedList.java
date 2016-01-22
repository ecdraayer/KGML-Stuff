package RunQueryOnIndex;

public class VisitedList {

	private String Name;
	private String Discoverer;
	private int DistancefromStart;

	public VisitedList(String Name, String Discoverer, int DistrancefromStart)
	{
		this.setName(Name);
		this.setDiscoverer(Discoverer);
		this.setDistancefromStart(DistrancefromStart);

	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDiscoverer() {
		return Discoverer;
	}

	public void setDiscoverer(String discoverer) {
		Discoverer = discoverer;
	}

	public int getDistancefromStart() {
		return DistancefromStart;
	}

	public void setDistancefromStart(int distancefromStart) {
		DistancefromStart = distancefromStart;
	}
	public String toString(){
		String result = "\n";
		result += "Name=" + Name + " Discoverer=" + Discoverer + " Distance=" + DistancefromStart ;
		return result;
	}
}
