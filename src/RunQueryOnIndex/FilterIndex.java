package RunQueryOnIndex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class FilterIndex {

	private TreeMap<String, TreeMap<String, ArrayList<String>>> indexContent;	
	private ReadIndex Index = new ReadIndex("_idx.csv");
	

	public ArrayList<ArrayList<String>> LoadIndex(String start, String end)
	{
		indexContent = Index.ReadRow();
		Iterator<String> iter = indexContent.keySet().iterator();
		ArrayList<ArrayList<String>> FilterContent= new ArrayList<ArrayList<String>>();
		while(iter.hasNext())
		{
			String key = iter.next();
			TreeMap<String, ArrayList<String>> Landmark = indexContent.get(key);

	
			ArrayList<String> startcontent = Landmark.get(start);
			FilterContent.add(startcontent);
			ArrayList<String> endcontent = Landmark.get(end);
			FilterContent.add(endcontent);

		}
		
		return FilterContent;
		
	}
	
	public double GetDistToLandmark(String CurrentVertex, String LandmarkToUse)
	{
		if (indexContent==null)
		{
			indexContent = Index.ReadRow();
			
		}
		double DistToLanmark=Double.POSITIVE_INFINITY;
		
		TreeMap<String, ArrayList<String>> Landmark= indexContent.get(LandmarkToUse);
		DistToLanmark = Double.parseDouble(Landmark.get(CurrentVertex).get(2));
		
	
		return DistToLanmark;
		
	}
	

	
	public MaxDistance getMaxDistance(ArrayList<ArrayList<String>> LandmarkIndex, String Destination)
	{
		MaxDistance maxDist = new MaxDistance();
		double lastDistance=Double.POSITIVE_INFINITY;
		double destinationDist=0;
		double maxDistance=0;

		String Landmark="";
		for(int i =0 ; i < LandmarkIndex.size(); i+=2)
		{

			maxDistance= Double.parseDouble(LandmarkIndex.get(i).get(2)) + Double.parseDouble(LandmarkIndex.get(i+1).get(2));
			if (maxDistance<=lastDistance)
			{
				lastDistance=maxDistance;
				Landmark=LandmarkIndex.get(i).get(0);
			}
			if (Destination.equals(LandmarkIndex.get(i).get(1) )  && LandmarkIndex.get(i).get(0).equals(Landmark) )
				destinationDist=Double.parseDouble(LandmarkIndex.get(i).get(2));
			else if (Destination.equals(LandmarkIndex.get(i+1).get(1) ) && LandmarkIndex.get(i).get(0).equals(Landmark) )
				destinationDist=Double.parseDouble(LandmarkIndex.get(i+1).get(2));
				
		}
		maxDist.setLandmark(Landmark);
		maxDist.setMaxDistance(lastDistance);
		maxDist.setDestinationDist(destinationDist);

		return maxDist;
	}
}
