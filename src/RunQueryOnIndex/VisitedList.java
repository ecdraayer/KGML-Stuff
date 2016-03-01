package RunQueryOnIndex;

import java.util.ArrayList;

public class VisitedList extends ArrayList<VisitedList>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Name;
	private String Discoverer;
	private int DistancefromStart;
	private double DistaceToLandmark;

	public VisitedList(String Name, String Discoverer, int DistrancefromStart)
	{
		this.setName(Name);
		this.setDiscoverer(Discoverer);
		this.setDistancefromStart(DistrancefromStart);
		this.setDistaceToLandmark(DistaceToLandmark);
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
		result += "Name=" + Name + " Discoverer=" + Discoverer + " Distance=" + DistancefromStart + " distaceToLandmark=" + DistaceToLandmark; 
		return result;
	}

	public double getDistaceToLandmark() {
		return DistaceToLandmark;
	}

	public void setDistaceToLandmark(double distaceToLandmark2) {
		DistaceToLandmark = distaceToLandmark2;
	}
	
}
