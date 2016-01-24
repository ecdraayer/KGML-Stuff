package RunQueryOnIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphPathImpl;
import org.jgrapht.graph.ListenableUndirectedGraph;
import org.jgrapht.traverse.ClosestFirstIterator;

import BuildIndex.BuildIndex;
import KOExtraction.ExportCsv;
import KOSearch.ReadCSV;
import TransformToGraph.TransformToGraph;

public class RunQueryOnIndex {

	static ReadIndex Index = new ReadIndex("_idx.csv");
	static ArrayList<ArrayList<String>> indexContent;	
	
	
	static ArrayList<VisitedList> VisitedList;
	static int position = -1;
	static HashSet<String> seen;
	static HashSet<String> pruned;
	static int distance= 0;
	static String lastVisited="";
	static boolean visitedFound=true;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//call graph
		ListenableGraph<String, DefaultEdge> Dgraph = TransformToGraph.BuildGraph("","Bacteria_879462.4.PATRIC/csv", false);
		//ListenableGraph<String, DefaultEdge> Dgraph = TransformToGraph.BuildGraph("Bacteria_879462.4.PATRIC/xmls", "", false);	
		ListenableUndirectedGraph<String, DefaultEdge> graph=BuildIndex.DirectedtoUndirected(Dgraph);
		
		
		ArrayList<ArrayList<String>> VertexToQuery  = new ArrayList<ArrayList<String>>();
		//read from genetoquery.csv		
		ReadCSV Source= new ReadCSV("Bacteria_879462.4.PATRIC/csv/GenesToQuery.csv");			
		VertexToQuery.add(Source.ReadCol(0));
		ReadCSV Destination= new ReadCSV("Bacteria_879462.4.PATRIC/csv/GenesToQuery.csv");
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
			//Queryvertexes.add("75");
			//Queryvertexes.add("82");
			
			
			ArrayList<ArrayList<String>> LandmarkIndex = LoadIndex(Index, Queryvertexes);
			System.out.println("Index content " +LandmarkIndex  );
			
			ArrayList<String> maxDistance=getMaxDistance(graph, LandmarkIndex,Queryvertexes.get(0), Queryvertexes.get(1));
			System.out.println("maxDistance " +maxDistance.get(1)  + " Landmark " + maxDistance.get(0));
			
			int VisitedDjstra = DijkstraShortestPathJGrapht(graph, Queryvertexes.get(0), Queryvertexes.get(1));
			System.out.println("Visited Dijkstra "  + VisitedDjstra);
			
			if (Double.isInfinite(VisitedDjstra))
				VisitedDjstra=10000;
			if (VisitedDjstra<6500)
			{
			
				traverseGraph(graph,Queryvertexes.get(0), Queryvertexes.get(1), maxDistance.get(0), Double.parseDouble(maxDistance.get(1)));
				System.out.println("Visited "  + VisitedList.size() );
				System.out.println("Pruned "  + pruned.size() );
				
				ToPrint[i][0]=VertexToQuery.get(0).get(i);
				ToPrint[i][1]=VertexToQuery.get(1).get(i);
				ToPrint[i][2]=Integer.toString(VisitedDjstra);
				ToPrint[i][3]=Integer.toString(VisitedList.size());
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
			
		ArrayList<ArrayList<String>> FilterContent= new ArrayList<ArrayList<String>>();
		for (int i=0; i < indexContent.size();i++)
		{
			for (int j=0; j < Destination.size(); j++)
			{
				if (Destination.get(j).equals(indexContent.get(i).get(1)) )
				{
					FilterContent.add(indexContent.get(i));
					
				}
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
		double DistToLanmark=0;
		for (int i=0; i < indexContent.size();i++)
		{

			if (CurrentVertex.equals(indexContent.get(i).get(1)) && LandmarkToUse.equals(indexContent.get(i).get(0)))
			{
				DistToLanmark=Double.parseDouble(indexContent.get(i).get(2));
					
				break; 
			}

			
		}
		return DistToLanmark;
		
	}
	
	public static ArrayList<String> getMaxDistance(ListenableUndirectedGraph<String, DefaultEdge> graph,ArrayList<ArrayList<String>> indexContent, String Start, String End)
	{
		double maxDistance=0; 
		double lastDistance=Double.POSITIVE_INFINITY;;
		ArrayList<String> MaxDistance = new ArrayList<String>();
		String Landmark="";
		for(int i =0 ; i < indexContent.size(); i+=2)
		{

			maxDistance= Double.parseDouble(indexContent.get(i).get(2)) + Double.parseDouble(indexContent.get(i+1).get(2));
			if (maxDistance<lastDistance)
			{
				lastDistance=maxDistance;
				Landmark=indexContent.get(i).get(0);
			}
		}
		MaxDistance.add(Landmark);
		MaxDistance.add(Double.toString(lastDistance));
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
	
	
	private static void traverseGraph(ListenableUndirectedGraph<String, DefaultEdge> graph,String Start,  String End, String Landmark, double maxDistance)
	{
		//the vertex that discovered path is stored in values...actual vertex is on keyset
		ClosestFirstIterator<String, DefaultEdge> iter =   new ClosestFirstIterator<String, DefaultEdge>(graph, Start, 1);
		int i =0;
		double DistToLandmark =0;
		String EndVertex="";

		if (lastVisited.equals(Start) || lastVisited.isEmpty()==true)
		{
			distance++;
			visitedFound=true;
		}
		else
			visitedFound=false;
		
		//only increment went all closest are processed	
		while (iter.hasNext()) {
			 String neighbor = iter.next();
			 
			 //System.out.println( i + " " + neighbor + " " + distance + " " + lastVisited); 
			 VisitedList Visited = new VisitedList(neighbor, Start, distance);	

			 if (neighbor.equals(End)) {
				 EndVertex=neighbor; 				 
				 VisitedList.add(Visited);
				 //System.out.println("Closest vertex " + VisitedList  );
				 return;
			 }
			 
			 
			 if (i>0)
			 {						
		
			 	DistToLandmark =GetDistToLandmark(Index,neighbor,Landmark);
			 	if (DistToLandmark+distance>maxDistance)
			 	{
			 		if (!seen.contains(neighbor))
			 			pruned.add(neighbor);
			 		seen.add(neighbor);
			 		i++;
			 		
			 		continue;
			 	}


				 if (seen.contains(neighbor)==false)
				 {				 			
				 			
				 	VisitedList.add(Visited);			
				 	if (visitedFound==true)
				 	{
				 		lastVisited=neighbor;
				 		visitedFound=false;
				 	}

				 }
						
			 }
			 	
			 seen.add(neighbor);
			 i++;
			
		}	
		position++;
		
		if (EndVertex.isEmpty())
		{	
			//System.out.println("pos " + position  );
			String value ="";
			value= VisitedList.get(position).getName();
					
			traverseGraph(graph,value,End, Landmark, maxDistance );
		}
		
		return;

	}

	
	public static int DijkstraShortestPathJGrapht(ListenableUndirectedGraph<String, DefaultEdge> graph, String Start, String End)
	{
		 ClosestFirstIterator<String, DefaultEdge> iter =
		            new ClosestFirstIterator<String, DefaultEdge>(graph, Start);
		 		int i =0;
		        while (iter.hasNext()) {
		        	String vertex = iter.next();
		        
		        	//System.out.println( i + " " + vertex);
		            if (vertex.equals(End)) {
		                createEdgeList(graph, iter, Start, End);
		                return i;
		            }
		            i++;
		        }
				return i;
	}
	 private static void createEdgeList(Graph<String, DefaultEdge> graph,
		        ClosestFirstIterator<String, DefaultEdge> iter,
		        String startVertex,
		        String endVertex) {
		 
		 		GraphPath<String, DefaultEdge> path;
		        List<DefaultEdge> edgeList = new ArrayList<DefaultEdge>();

		        String v = endVertex;

		        while (true) {
		        	DefaultEdge edge = iter.getSpanningTreeEdge(v);

		            if (edge == null) {
		                break;
		            }

		            edgeList.add(edge);
		            v = Graphs.getOppositeVertex(graph, edge, v);
		        }

		        Collections.reverse(edgeList);
		        double pathLength = iter.getShortestPathLength(endVertex);
		        path =
		            new GraphPathImpl<String, DefaultEdge>(
		                graph,
		                startVertex,
		                endVertex,
		                edgeList,
		                pathLength);
		        
		     	//System.out.println("Jgrapht path " +path);
		    }
	 
	
	

}
