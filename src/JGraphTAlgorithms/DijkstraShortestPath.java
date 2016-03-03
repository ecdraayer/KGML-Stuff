package JGraphTAlgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.GraphPathImpl;

public class DijkstraShortestPath {

	public static int DijkstraShortestPathJGrapht(String Start, String End, Graph<String, DefaultWeightedEdge> graph)
	{
		 ClosestFirstIterator<String,  DefaultWeightedEdge> iter =
		            new ClosestFirstIterator<String,  DefaultWeightedEdge>(graph, Start);
		 		int i =0;
		        while (iter.hasNext()) {
		        	String vertex = iter.next();
		        
		        	//System.out.println( i + " Dij " + vertex );
		            if (vertex.equals(End)) {
		                createEdgeList(graph, iter, Start, End);
		                return i;
		            }
		            i++;
		        }
				return i;
	}
	 private static void createEdgeList(Graph<String,  DefaultWeightedEdge> graph,
		        ClosestFirstIterator<String,  DefaultWeightedEdge> iter,
		        String startVertex,
		        String endVertex) {
		 
		 		GraphPath<String,  DefaultWeightedEdge> path;
		        List< DefaultWeightedEdge> edgeList = new ArrayList< DefaultWeightedEdge>();

		        String v = endVertex;

		        while (true) {
		        	 DefaultWeightedEdge edge = iter.getSpanningTreeEdge(v);

		            if (edge == null) {
		                break;
		            }

		            edgeList.add(edge);
		            v = Graphs.getOppositeVertex(graph, edge, v);
		        }

		        Collections.reverse(edgeList);
		        double pathLength = iter.getShortestPathLength(endVertex);
		        path =
		            new GraphPathImpl<String,  DefaultWeightedEdge>(
		                graph,
		                startVertex,
		                endVertex,
		                edgeList,
		                pathLength);
		        
		     	//System.out.println("Jgrapht path " +path);
		    }
	 
}
