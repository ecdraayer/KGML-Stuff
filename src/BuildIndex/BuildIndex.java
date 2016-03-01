package BuildIndex;

import java.io.IOException;
import java.util.ArrayList;

import org.jgrapht.ListenableGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;
import org.jgrapht.graph.ListenableUndirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import KOExtraction.ExportCsv;
import TransformToGraph.TransformToGraph;
import TransformToGraph.WeightedEdge;

/**
 * Generate index based on landmarks and calculates distances.
 * @author Raul Alvarado
 *
 */
public class BuildIndex {
	static SimpleWeightedGraph<String,  DefaultWeightedEdge> g =new SimpleWeightedGraph<String,  DefaultWeightedEdge>( DefaultWeightedEdge.class);
	//static ListenableUndirectedGraph<String, DefaultWeightedEdge> g =new ListenableUndirectedGraph<String,  DefaultWeightedEdge>( DefaultWeightedEdge.class);

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ListenableDirectedWeightedGraph<String, WeightedEdge> Dgraph = new ListenableDirectedWeightedGraph<String,  WeightedEdge>( WeightedEdge.class);
		Dgraph =TransformToGraph.BuildGraph("","Bacteria_879462.4.PATRIC/csv", false);
		//Dgraph= TransformToGraph.BuildGraph("Bacteria_879462.4.PATRIC/xmls","", false);	
		g=DirectedtoUndirected(Dgraph);
		
		ArrayList<String> LandMarks = new ArrayList<String> ();
		LandMarks.add("ko:K09458");
		LandMarks.add("ko:K11533");
		//LandMarks.add("80");
		//LandMarks.add("39");
		DistancesFromLandmark(g, LandMarks);
	}
	public static SimpleWeightedGraph<String,  DefaultWeightedEdge> DirectedtoUndirected(ListenableDirectedWeightedGraph<String, WeightedEdge> Dgraph)
	{
		for(String vertex:Dgraph.vertexSet())
		{
			g.addVertex(vertex);
		}
		for(WeightedEdge edges : Dgraph.edgeSet())
		{
			//avoid loops
			if (!Dgraph.getEdgeSource(edges).equals(Dgraph.getEdgeTarget(edges)))
			{
				DefaultWeightedEdge d = g.addEdge(Dgraph.getEdgeSource(edges), Dgraph.getEdgeTarget(edges));
				if (d!=null)
					g.setEdgeWeight(d,Dgraph.getEdgeWeight(edges));
			}
		}
		return g;
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void DistancesFromLandmark(SimpleWeightedGraph<String,  DefaultWeightedEdge> Ugraph ,ArrayList<String> Landmarks )
	{
		String[][] Distances  = new String[(Ugraph.vertexSet().size()* Landmarks.size()) ][3];
		int j=0;
		for (int i=0; i < Landmarks.size();i++)
		{
			for (String vertex: Ugraph.vertexSet())
			{
				
				DijkstraShortestPath d= new DijkstraShortestPath(g, Landmarks.get(i), vertex);
				Distances[j][0]=Landmarks.get(i);
				Distances[j][1]=vertex;
				Distances[j][2]= Double.toString(d.getPathLength());
				System.out.println("From: " +Landmarks.get(i) + " to: " + vertex + " distance " + d.getPathLength());
				j++;
			}
		}
		WriteIndex(Distances);
		
	}
	 public static void WriteIndex(String[][] Distances)
	 {
		ExportCsv Csv = new ExportCsv("_idx.csv", false,"Landamark, Destination, Distance");
		for (String[] s: Distances)
		{
			try {
				Csv.WriteFieldCSV(s[0] ,0);
			
				Csv.WriteFieldCSV(s[1] ,0);
				Csv.WriteFieldCSV(s[2] ,0);
				Csv.WriteFieldCSV("" ,1);
				Csv.FlushCSV();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
		}
		Csv.closeCSV();
		
	 }

}
