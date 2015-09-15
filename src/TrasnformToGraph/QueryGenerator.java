package TrasnformToGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import KGMLParser.Pathway;
import KGMLParser.PathwayMap;

/**
 * This class is used to generate queries from one random gene to another random gene. Genes may or may not be from different pathways.
 * It utilizes pathwayMap generated in  TrasnformToGraph.
 * 
 * DistinctPathways return a list with the different number of pathways the query travel thru.
 * @author Raul Alvarado
 *
 */
public class QueryGenerator {

	private PathwayMap Organism ;      
	private ArrayList<Pathway> pathways;
	public QueryGenerator(PathwayMap Organism   )
	{
		this.Organism= Organism;
		this.pathways = Organism.pathways;
	}
	public String[] GetRandomGene()
	{		
		String[] Genedata = new String[2];
		//random path
		Random rand = new Random();
		int r = rand.nextInt(pathways.size());

		Pathway pathway = pathways.get( r);
		Genedata[0]=pathway.getName();
		
		//random gene in pathway	
	
		int geneidx = rand.nextInt(pathway.getEntryL().size());
		Genedata[1]=pathway.getEntryL().get(geneidx).getName();
		
		return Genedata;
	}
	public void OutputResults(String Gene1, String Gene2, int NoEdges, int pathwayNumber)
	{
		  
		System.out.println("q1 \t\t q2 \t\t  edgeNum \t\t pathwayNumber "  );
		System.out.println(Gene1 + "\t" + Gene2 + "\t\t" + NoEdges + "\t\t" + pathwayNumber   );
	}
	/**
	 * @param path
	 * @return
	 */
	public  Set<String> DistinctPathways(List<DefaultEdge>  path)
	{
		 Set<String> hs = new HashSet<>();
		 ArrayList<String> found = new ArrayList<String> ();
		 for(Pathway cpway : pathways){
			 
			 for (int i=0; i<path.size();i++)
			 {
				 String[] vertex = path.get(i).toString().split(" : ");
				 vertex[0]= vertex[0].replace("(", "");
				 vertex[1]= vertex[1].replace(")", "");	
				
				 if (found.contains(vertex[0])==false)
				 { 
					 String pathname= cpway.GetPathwayNameFromGene(vertex[0]);
					//only get name of last node of the path.  Avoid Duplicate searches.
					 if (i ==path.size()-1)
					 {
						 String pathname2= cpway.GetPathwayNameFromGene(vertex[1]);
						 if (pathname2!="")
						 {
							 hs.add( pathname2 );
							 found.add(vertex[1]);
						 }
					 }
						 
					 
					 if (pathname!="")
					 {
						 hs.add( pathname );
						 found.add(vertex[0]);
					 }
				 }
			 }
			 
		 }


		return hs;
		
	}
  
}
