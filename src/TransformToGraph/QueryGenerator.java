package TransformToGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.ListenableGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import KGMLFunctions.Pathway;
import KOExtraction.ExportCsv;
import KOSearch.ReadCSV;

/**
 * @author Raul Alvarado
 *
 */
public class QueryGenerator {
	static ArrayList<ArrayList<String>> GeneNames = new ArrayList<ArrayList<String>>();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args2) {
		// TODO Auto-generated method stub
		String GenesToQuery="",GraphDirectory="";	
		int j=0;
		boolean nv=true;
		

		if (args2.length > 0)
		{
			while (j < args2.length)
			{
				 
				if (args2[j].equals("-f"))
				{
					GraphDirectory=args2[j+1];
				}
				if (args2[j].equals("-g"))
				{
					GenesToQuery=args2[j+1];
				}
				if (args2[j].equals("-nv"))
				{
					nv=false;
				}
				j++;
				
			}
		}
		else			
		{
			System.out.println("Please provide appropriate arguments");
			System.out.println("-f Directory containing Edges.csv and Nodes.csv -g file that contains Genes to query -nv no visual representation");
			System.exit(1);
		}
		
		if (GraphDirectory.isEmpty()==false && GenesToQuery.isEmpty()==false)
		{
			long startTime = System.nanoTime();	
			createGraphCSV(GraphDirectory);
			long stopTime = System.nanoTime();
			
			double readElapsed = (stopTime - startTime);
			//readElapsed= TimeUnit.MILLISECONDS.convert(readElapsed, TimeUnit.NANOSECONDS);
			readElapsed= (readElapsed/1000000.0);
			double memory = usedMemory()/ (1024.0 * 1024.0);
			System.out.println("JVM memory :" +memory +"MB");			
			System.out.println("Reading File time(miliseconds) "+ readElapsed);
			
			ArrayList<ArrayList<String>> GeneNames = new ArrayList<ArrayList<String>>();
			ReadCSV Source= new ReadCSV(GenesToQuery);			
			GeneNames.add(Source.ReadCol(0));
			ReadCSV Destination= new ReadCSV(GenesToQuery);
			GeneNames.add(Destination.ReadCol(1));
		
			
			ListenableDirectedWeightedGraph<String, WeightedEdge> g = TransformToGraph.g;

			String[][] ToPrint= new String[GeneNames.get(0).size()*2][6];
			String Gene1 = null,Gene2 = null, temp;
			int k=0;
			for (int i=0; i< GeneNames.get(0).size()*2; i ++)
			{
				if (i%2==0)
	        	{
			        Gene1= GeneNames.get(0).get(k);
			        Gene2= GeneNames.get(1).get(k);
			        k++;
	        	}
	        	else
	        	{
	        		temp =Gene1;
	        		Gene1=Gene2;
	        		Gene2=	temp;
	        	}
				
				//remove initialization from time metric 
	        	DijkstraShortestPath d;
			    startTime = System.nanoTime();	
	        	d = new DijkstraShortestPath(g, Gene1, Gene2);	        	
				stopTime = System.nanoTime();
				double FindPathElapsed = (stopTime - startTime);
				
				FindPathElapsed=  (FindPathElapsed/1000000.0);
				List<DefaultEdge> path =d.getPathEdgeList();				
				System.out.println("Shortest path from " + Gene1 + " from pathway "+ Gene1 + " to " + Gene2 + " from pathway " + Gene2 + " in " + FindPathElapsed +" (miliseconds)");

				
				
				ToPrint[i][0]=Gene1;
				ToPrint[i][1]=Gene2;
				    if (path != null )
				    {
					    if (path.size()>0 )
					    {
						    System.out.println(path);
						    if (nv==true)
						    {
							    ShortestPathtoGraph SG= new ShortestPathtoGraph();
							    SG.CreateGraphCSV(path);
						    }
						    
						    
						    //determine number of pathways path travel thru
						    int NoPathways=DistinctPathways(path).size();

						
						    ToPrint[i][2]= Integer.toString(path.size());
						    ToPrint[i][3]= Integer.toString(NoPathways);
						    ToPrint[i][4]= path.toString();
						    ToPrint[i][5]=Double.toString(FindPathElapsed);
						    
					    }
					    else
					    	System.out.println("No path found");
				    }
				    else
				    	System.out.println("No path found");
				    
			}
			
			   //print results
	        OutputResults(ToPrint, readElapsed, memory);
	        System.out.println("Done!");
			
			
			
		}
	}
	private static void createGraphCSV(String GraphDirectory)
	{
		ReadCSV NodesCSV= new ReadCSV(GraphDirectory+ "/Nodes.csv");
		GeneNames.add(NodesCSV.ReadCol(0));
		ReadCSV PathNames= new ReadCSV(GraphDirectory+ "/Nodes.csv");
		GeneNames.add(PathNames.ReadCol(3));
		
		ArrayList<ArrayList<String>> Edges = new ArrayList<ArrayList<String>>();
		ReadCSV Source= new ReadCSV(GraphDirectory+ "/Edges.csv");
		Edges.add(Source.ReadCol(0));
		ReadCSV Destination= new ReadCSV(GraphDirectory+ "/Edges.csv");
		Edges.add(Destination.ReadCol(1));
		
		
		//measure xml loading time.
		
		TransformToGraph.AddVerticesAndEdgesFromCSV(GeneNames.get(0), Edges);
	}
	 private static long usedMemory()
	 {
	        Runtime rt = Runtime.getRuntime();

	        return rt.totalMemory() - rt.freeMemory();
	 }
	 public static  Set<String> DistinctPathways(List<DefaultEdge>  path)
	 {
		 Set<String> hs = new HashSet<>();
		 
		 
		 for (int i=0; i<path.size();i++)
		 {
			
			 String[] vertex = path.get(i).toString().split(" : ");
			 vertex[0]= vertex[0].replace("(", "");
			 vertex[1]= vertex[1].replace(")", "");	
			 int pos = GeneNames.get(0).indexOf(vertex[0]);
			 int pos2 = GeneNames.get(0).indexOf(vertex[1]);
			 
			 hs.add(GeneNames.get(1).get(pos));
			 hs.add(GeneNames.get(1).get(pos2));
			 
		 }
		 
		return hs;
	 
	 }
	 public static void OutputResults(String[][] ToPrint, double readElapsed, double memory)
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
}
