package RunQueryOnIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.ListenableGraph;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.GraphPathImpl;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;
import org.jgrapht.graph.ListenableUndirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import BuildIndex.BuildIndex;
import JGraphTAlgorithms.ClosestFirstIterator;
import JGraphTAlgorithms.DijkstraShortestPath;
import KOExtraction.ExportCsv;
import KOSearch.ReadCSV;
import TransformToGraph.TransformToGraph;
import TransformToGraph.WeightedEdge;

public class RunQueryOnIndex {

	/**
	 * 
	 */

	static ReadIndex Index = new ReadIndex("_idx.csv");
	static TreeMap<String, TreeMap<String, ArrayList<String>>> indexContent;	
	
	
	static ArrayList<VisitedList> VisitedList;
	static int position = -1;
	static HashSet<String> seen;
	static HashSet<String> pruned;
	static int distance= 0;
	static String lastVisited="";
	static boolean visitedFound=true;
	static SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//call graph
		ListenableDirectedWeightedGraph<String, WeightedEdge> Dgraph = TransformToGraph.BuildGraph("","Bacteria_879462.4.PATRIC/csv", false);
		//ListenableDirectedWeightedGraph<String, WeightedEdge> Dgraph = TransformToGraph.BuildGraph("Bacteria_879462.4.PATRIC/xmls","", false);	
		graph=BuildIndex.DirectedtoUndirected(Dgraph);
		
		
		ArrayList<ArrayList<String>> VertexToQuery  = new ArrayList<ArrayList<String>>();
		//read from genetoquery.csv		
		ReadCSV Source= new ReadCSV("Bacteria_879462.4.PATRIC/csv/GenesToQuery.csv");
		//ReadCSV Source= new ReadCSV("GenesToQuery.csv");	
		VertexToQuery.add(Source.ReadCol(0));
		ReadCSV Destination= new ReadCSV("Bacteria_879462.4.PATRIC/csv/GenesToQuery.csv");
		//ReadCSV Destination= new ReadCSV("GenesToQuery.csv");
		VertexToQuery.add(Destination.ReadCol(1));
		

		String[][] ToPrint= new String[VertexToQuery.get(0).size()][5];
		for (int i=0; i < VertexToQuery.get(0).size(); i++)
		{
			VisitedList  = new ArrayList<VisitedList>();
			position = -1;
			seen = new HashSet<String> ();
			pruned = new HashSet<String> ();	
			distance= 0;
			lastVisited="";
			visitedFound=true;
					
			ArrayList<String> Queryvertexes  = new ArrayList<String>();
			Queryvertexes.add(VertexToQuery.get(0).get(i));
			Queryvertexes.add(VertexToQuery.get(1).get(i));

			
			System.out.println("Query: " +Queryvertexes  );
			ArrayList<ArrayList<String>> LandmarkIndex = LoadIndex(Index, Queryvertexes);
			System.out.println("Index content " +LandmarkIndex  );
			
			ArrayList<String> maxDistance=getMaxDistance(LandmarkIndex, Queryvertexes.get(1));
			System.out.println("maxDistance " +maxDistance.get(1)  + " Landmark " + maxDistance.get(0) + " Destination " + maxDistance.get(2));
	
			int VisitedDjstra = DijkstraShortestPath.DijkstraShortestPathJGrapht(Queryvertexes.get(0), Queryvertexes.get(1), graph);
			System.out.println("Visited Dijkstra "  + VisitedDjstra);
			
			//if (VisitedDjstra<4500)
			{
			
				traverseGraph(Queryvertexes.get(0), Queryvertexes.get(1), maxDistance.get(0), Double.parseDouble(maxDistance.get(1)),Double.parseDouble(maxDistance.get(2)));
				//System.out.println("Visited "  + VisitedList);
				System.out.println("Visited "  + (VisitedList.size() - 1));
				System.out.println("Pruned "  + pruned.size() );
				
				ToPrint[i][0]=VertexToQuery.get(0).get(i);
				ToPrint[i][1]=VertexToQuery.get(1).get(i);
				ToPrint[i][2]=Integer.toString(VisitedDjstra);
				ToPrint[i][3]=Integer.toString(VisitedList.size()-1);
				ToPrint[i][4]=Integer.toString(pruned.size());
			
			}
		}
		OutputResults(ToPrint);
	}
	private static void OutputResults(String[][] ToPrint)
	{
			 
			ExportCsv Csv = new ExportCsv("QueryOnIndex.csv", false,"Source,Destination,dijkstraVisited,Visited,Prunned");
			
			for (String[] s: ToPrint)
			{
				if (s[0]!=null)
				{
					try {
						Csv.WriteFieldCSV(s[0] ,0);
				
						Csv.WriteFieldCSV(s[1] ,0);
						Csv.WriteFieldCSV(s[2] ,0);
						Csv.WriteFieldCSV(s[3] ,0);
						Csv.WriteFieldCSV(s[4] ,0);
						Csv.WriteFieldCSV("" ,1);
						Csv.FlushCSV();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			Csv.closeCSV();
	}
	
	
	public static ArrayList<ArrayList<String>> LoadIndex(ReadIndex Index, ArrayList<String> Destination)
	{
		indexContent = Index.ReadRow();
		Iterator<String> iter = indexContent.keySet().iterator();
		ArrayList<ArrayList<String>> FilterContent= new ArrayList<ArrayList<String>>();
		while(iter.hasNext())
		{
			String key = iter.next();
			TreeMap<String, ArrayList<String>> Landmark = indexContent.get(key);

			for (int j=0; j < Destination.size(); j++)
			{
				String Dest=Destination.get(j).toString();
				ArrayList<String> filtercontent = Landmark.get(Dest);
				FilterContent.add(filtercontent);
			}
		}
		
		return FilterContent;
		
	}
	
	public static double GetDistToLandmark(ReadIndex Index, String CurrentVertex, String LandmarkToUse)
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
	
	public static ArrayList<String> getMaxDistance(ArrayList<ArrayList<String>> indexContent, String Destination)
	{
		double maxDistance=0; 
		double lastDistance=Double.POSITIVE_INFINITY;
		double destinationDist=0;
		ArrayList<String> MaxDistance = new ArrayList<String>();
		String Landmark="";
		for(int i =0 ; i < indexContent.size(); i+=2)
		{

			maxDistance= Double.parseDouble(indexContent.get(i).get(2)) + Double.parseDouble(indexContent.get(i+1).get(2));
			if (maxDistance<=lastDistance)
			{
				lastDistance=maxDistance;
				Landmark=indexContent.get(i).get(0);
			}
			if (Destination.equals(indexContent.get(i).get(1) ) )
				destinationDist=Double.parseDouble(indexContent.get(i).get(2));
			else if (Destination.equals(indexContent.get(i+1).get(1) ))
				destinationDist=Double.parseDouble(indexContent.get(i+1).get(2));
				
		}
		MaxDistance.add(Landmark);
		MaxDistance.add(Double.toString(lastDistance));
		MaxDistance.add(Double.toString(destinationDist));
		return MaxDistance;
	}
		
		/*ArrayList<String> discoverer =(new ArrayList<String>(DiscoveredPaths));

		ArrayList<String> path = new ArrayList<String>();
		//start from end to start node.
		path.add(End);
		String lastdiscoverer= discoverer.get(discoverer.size()-1);
		path.add(lastdiscoverer);
		while(lastdiscoverer.equals(Start)==false)
		{
			
			lastdiscoverer=DiscoveredPaths.get(lastdiscoverer);
			path.add(lastdiscoverer);
		}
		//path.add(Start);

		System.out.println("Path " + path);
		 }*/	
	

	
	//static LinkedHashMap<String,String> VertexToCheck = new LinkedHashMap<String,String>();
	
	
	private static void traverseGraph(String Start,  String End, String Landmark, double maxDistance, double destDistance )
	{
		//the vertex that discovered path is stored in values...actual vertex is on keyset
		ClosestFirstIterator<String,  DefaultWeightedEdge> iter =   new ClosestFirstIterator<String,  DefaultWeightedEdge>(graph, Start);
		int i =0;
		double DistToLandmark =0;
		double maxTreshold=0;
		String EndVertex="";

		if (lastVisited.equals(Start) || lastVisited.isEmpty()==true)
		{
			//distance++;
			visitedFound=true;
		}
		else
			visitedFound=false;
		
	
		
		//only increment when all closest are processed	
		while (iter.hasNext()) {
			 String neighbor = iter.next();
			 //System.out.println("shortest path " + i + neighbor );
			 distance=(int)  iter.getShortestPathLength(neighbor);
	
			 VisitedList Visited = new VisitedList(neighbor, Start, distance);	

			 if (neighbor.equals(End)) {
				 EndVertex=neighbor; 				 
				 VisitedList.add(Visited);
				 return;
			 }
			 
			 
			 if (i>0)
			 {						
		
			 	DistToLandmark =GetDistToLandmark(Index,neighbor,Landmark);
			 	Visited.setDistaceToLandmark(DistToLandmark);
				maxTreshold=maxDistance + destDistance;
			 	//if current node has infinity path, whole path is infinity
			 	if (DistToLandmark==Double.POSITIVE_INFINITY || maxTreshold==Double.POSITIVE_INFINITY)
			 		return;		 
			
			 	if (DistToLandmark+distance>maxTreshold)
			 	{
			 		
			 		if (!seen.contains(neighbor))
			 		{
			 			//System.out.println("pruned " + neighbor + " Distan " +  DistToLandmark  + " currDistance " +distance );
			 			pruned.add(neighbor);
			 		}
			 		seen.add(neighbor);
			 		i++;
			 		
			 		continue;
			 	}


				 if (seen.contains(neighbor)==false)
				 {				 			
					//System.out.println( i + " " + neighbor + " " + distance + " " + lastVisited + " " +DistToLandmark);
					VisitedList.add(Visited);
				
				 	if (visitedFound==true)
				 	{
				 		lastVisited=neighbor;
				 		visitedFound=false;
				 	}

				 }
					 
						
			 }
			 else if(seen.isEmpty())
				 VisitedList.add(Visited);

			 	
			 seen.add(neighbor);
			 i++;
			
		}	
		position++;
		if (EndVertex.isEmpty())
		{	
			String value ="";
			value= VisitedList.get(position).getName();
			//System.out.println("pos " + position + " value " + value + " distance " + VisitedList.get(position).getDistancefromStart()  );				
	
			traverseGraph(value,End, Landmark, maxDistance, destDistance );
		}
		
		return;

	}

	
	
	

}

