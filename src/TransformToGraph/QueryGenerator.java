package TransformToGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import KGMLParser.Pathway;
import KGMLParser.PathwayMap;
import KOExtraction.ExportCsv;

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
		
		if (Genedata[1].equals("undefined")==true)
			Genedata[1]=Genedata[1]+Genedata[0];
				
		
		return Genedata;
	}
	public void OutputResults(String[][] ToPrint, double readElapsed, double memory)
	{
		 
		ExportCsv Csv = new ExportCsv("outputConnected.csv", false,"q1,q2,edgeNum,pathwayNumber,ShortestPath,RunningTime");
		
		ExportCsv CsvNotCon = new ExportCsv("outputNotConnected.csv", false,"q1,q2,edgeNum,pathwayNumber,ShortestPath");
		
		System.out.println("q1 \t\t\t q2 \t\t  edgeNum \t\t pathwayNumber \t\tShortestPath"  );
		

		for (String[] s: ToPrint)
		{
		

			System.out.println(s[0] + "\t\t" + s[1]  + "\t\t" + s[2]  + "\t\t" + s[3]    );
			
			try {
				if (s[2]==null && s[3]==null)
				{
					s[2]=" ";
					s[3]=" ";
					s[4]=" ";
					CsvNotCon.WriteFieldCSV(s[0] ,0);
					CsvNotCon.WriteFieldCSV(s[1] ,0);
					CsvNotCon.WriteFieldCSV(s[2] ,0);
					CsvNotCon.WriteFieldCSV(s[3] ,0);
					CsvNotCon.WriteFieldCSV(s[4]  ,0);
					CsvNotCon.WriteFieldCSV("" ,1);
					CsvNotCon.FlushCSV();
				}
				else
				{
								
					Csv.WriteFieldCSV(s[0] ,0);
					Csv.WriteFieldCSV(s[1] ,0);
					Csv.WriteFieldCSV(s[2] ,0);
					Csv.WriteFieldCSV(s[3] ,0);
					Csv.WriteFieldCSV("\"" + s[4] + "\""  ,0);
					Csv.WriteFieldCSV(s[5]  ,0);
					Csv.WriteFieldCSV("" ,1);
					Csv.FlushCSV();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		//write reading xml time
		try {
			Csv.WriteFieldCSV("Reading xml time(miliseconds): " ,0);
			Csv.WriteFieldCSV(Double.toString(readElapsed) ,0);
			Csv.WriteFieldCSV("" ,1);
			Csv.WriteFieldCSV("JVM memory: " ,0);
			Csv.WriteFieldCSV(Double.toString(memory) ,0);
			Csv.WriteFieldCSV("" ,1);
			Csv.FlushCSV();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Csv.closeCSV();
		CsvNotCon.closeCSV();
		
		
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
