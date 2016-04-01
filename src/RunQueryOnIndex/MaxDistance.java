package RunQueryOnIndex;

public class MaxDistance {
	
	private String Landmark;
	private double maxDistance;
	private double destinationDist;

	
	public MaxDistance()
	{
		this.setMaxDistance(0);
		this.setDestinationDist(0);
	}


	public double getMaxDistance() {
		return maxDistance;
	}


	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}


	public double getDestinationDist() {
		return destinationDist;
	}


	public void setDestinationDist(double destinationDist) {
		this.destinationDist = destinationDist;
	}


	public String getLandmark() {
		return Landmark;
	}


	public void setLandmark(String landmark) {
		Landmark = landmark;
	}
	
	public String toString()
	{
		return Landmark + "  max=" +maxDistance + " destination=" + destinationDist;
		
	}
}
