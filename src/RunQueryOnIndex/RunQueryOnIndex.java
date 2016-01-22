package RunQueryOnIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphPathImpl;
import org.jgrapht.graph.ListenableUndirectedGraph;
import org.jgrapht.traverse.ClosestFirstIterator;

import BuildIndex.BuildIndex;
import TransformToGraph.TransformToGraph;

public class RunQueryOnIndex {

	static ReadIndex Index = new ReadIndex("_idx.csv");
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		ArrayList<String> Queryvertexes  = new ArrayList<String>();
		Queryvertexes.add("75");
		Queryvertexes.add("88");
		ArrayList<ArrayList<String>> indexContent = FilterIndex(Index, Queryvertexes);
		
		System.out.println("Index content" +indexContent );
		//call graph
		ListenableGraph<String, DefaultEdge> Dgraph = TransformToGraph.BuildGraph("Bacteria_879462.4.PATRIC/xmls", "", false);		
		ListenableUndirectedGraph<String, DefaultEdge> graph=BuildIndex.DirectedtoUndirected(Dgraph);
		
		
	
		
		ShortestPath(graph, indexContent,Queryvertexes.get(0), Queryvertexes.get(1));
		DijkstraShortestPathJGrapht(graph, Queryvertexes.get(0), Queryvertexes.get(1));
	}
	
	public static ArrayList<ArrayList<String>> FilterIndex(ReadIndex Index, ArrayList<String> Destination)
	{
		ArrayList<ArrayList<String>> indexContent = Index.ReadRow();
		ArrayList<ArrayList<String>> FilterContent= new ArrayList<ArrayList<String>>();
		for (int i=0; i < indexContent.size();i++)
		{
			for (int j=0; j < Destination.size(); j++)
			{
				if (Destination.get(j).equals(indexContent.get(i).get(1)))
				{
					FilterContent.add(indexContent.get(i));
				}
			}
			
		}
		return FilterContent;
		
	}
	public static void ShortestPath(ListenableUndirectedGraph<String, DefaultEdge> graph,ArrayList<ArrayList<String>> indexContent, String Start, String End)
	{
		double maxDistance=0; 
		for(int i =0 ; i < indexContent.size(); i++)
		{
			maxDistance= maxDistance + Double.parseDouble(indexContent.get(i).get(2));
		}
		System.out.println("Max distance " + maxDistance);
		
		ArrayList<VisitedList> DiscoveredPaths =GetNearestList(graph,Start, End, indexContent.get(0).get(0), maxDistance);
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
				*/	
	}

	
	//static LinkedHashMap<String,String> VertexToCheck = new LinkedHashMap<String,String>();
	
	static ArrayList<VisitedList> VisitedList  = new ArrayList<VisitedList>();
	static int position = -1;
	static HashSet<String> seen = new HashSet<String> ();
	static int distance= 0;
	static String lastDiscovered="";
	static boolean DiscoveredFound=true;
	private static ArrayList<VisitedList> GetNearestList(ListenableUndirectedGraph<String, DefaultEdge> graph,String Start,  String End, String Landmark, double maxDistance)
	{
		//the vertex that discovered path is stored in values...actual vertex is on keyset
		ClosestFirstIterator<String, DefaultEdge> iter =   new ClosestFirstIterator<String, DefaultEdge>(graph, Start, 1);
		int i =0;
		double DistToLandmark =0;
		String EndVertex="";
		
		
		if (lastDiscovered.equals(Start) || lastDiscovered.isEmpty()==true)
		{
			distance++;
			DiscoveredFound=true;
		}
		else
			DiscoveredFound=false;
		
		//only increment went all closest are processed	
		while (iter.hasNext()) {
			 String vertex = iter.next();
			 
			 System.out.println( i + " " + vertex + " " + distance + " " + lastDiscovered); 
			 VisitedList Visited = new VisitedList(vertex, Start, distance);	

			 if (vertex.equals(End)) {
				 EndVertex=vertex; 				 
				 VisitedList.add(Visited);
				 System.out.println("Closest vertex " + VisitedList  );
				 return VisitedList;
			 }
			 
			 
			 if (i>0)
			 {						
				 if (!Landmark.equals(vertex))
				 {
					ArrayList<String> Queryvertexes  = new ArrayList<String>();
			 		Queryvertexes.add(vertex);
			 		ArrayList<ArrayList<String>> indexContent = FilterIndex(Index, Queryvertexes);
			 		DistToLandmark =Double.parseDouble(indexContent.get(0).get(2));
			 		if (DistToLandmark+distance>maxDistance)
			 		{
			 			seen.add(vertex);
			 			i++;
			 			continue;
			 		}
			 		else 
			 			System.out.println("vertex prunned:" + vertex);
				 }
				 	
				 	{
				 		if (seen.contains(vertex)==false)
				 		{				 			
				 			
				 			VisitedList.add(Visited);			
				 			 if (DiscoveredFound==true)
				 			 {
								 lastDiscovered=vertex;
								 DiscoveredFound=false;
				 			 }

				 		}
				 	}
						
			 }
			 	
			 seen.add(vertex);
			 i++;
			
		}	
		position++;
		
		if (EndVertex.isEmpty())
		{	
			String value ="";
			value= VisitedList.get(position).getName();
					
			System.out.println("pos " + position + " Closest vertex " + VisitedList);
			GetNearestList(graph,value,End, Landmark, maxDistance );
		}

		return VisitedList;

	}

	
	public static void DijkstraShortestPathJGrapht(ListenableUndirectedGraph<String, DefaultEdge> graph, String Start, String End)
	{
		 ClosestFirstIterator<String, DefaultEdge> iter =
		            new ClosestFirstIterator<String, DefaultEdge>(graph, Start);
		 		int i =0;
		        while (iter.hasNext()) {
		        	String vertex = iter.next();
		        
		        	System.out.println( i + " " + vertex);
		            if (vertex.equals(End)) {
		                createEdgeList(graph, iter, Start, End);
		                return;
		            }
		            i++;
		        }
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
		        
		     	System.out.println("Jgrapht path " +path);
		    }
	 
	
	

}
