package TrasnformToGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import KGMLParser.Pathway;
import KGMLParser.PathwayMap;

public class QueryGenerator {

	private PathwayMap Organism ;      
	private ArrayList<Pathway> pathways;
	public QueryGenerator()
	{
		PathwayMap Organism = new PathwayMap();
		ArrayList<Pathway> pathways = Organism.pathways;
	}
	public String GetRandomGene()
	{		
		Random rand = new Random();
		int r = rand.nextInt(pathways.size()+1);
		
		
		Pathway pathway = pathways.get( r);
		
		pathway.getEntryL().get(0);
		
		return pathway.getEntryL().get(0).getName();
	}
	public void OutputResults()
	{
		  
		
	}
  
}
