package TransformToGraph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WeightedEdge extends DefaultWeightedEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1645421538697685470L;

	@Override
	  public String toString() {
	    return Double.toString(getWeight());
	  }
	}