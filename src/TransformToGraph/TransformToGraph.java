package TransformToGraph;



import KGMLParser.Kegg_Entry;
import KGMLParser.Pathway;
import KGMLParser.PathwayMap;
import KGMLParser.Reaction;
import KGMLParser.Relation;
import KOExtraction.ExportCsv;
import KOSearch.ReadCSV;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.*;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.*;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

public class TransformToGraph extends JApplet {

	/**
	 * This class transforms pathways(kgmls) to graphs.  Reads xml data from KGMLParser.PathwayMap.
	 * Adds genes, compounds, relations and reactions.
	 * Genes and compounds of the same pathway are same color, but genes are a lighter tone.
	 * Each Pathway has its one specific random color
	 *   @author Raul Alvarado
	 */
	private static final long serialVersionUID = 7980400801849305625L;
	
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static JGraphModelAdapter<String, DefaultEdge> jgAdapter;

    static PathwayMap Organism;    
	static ListenableGraph<String, DefaultEdge> g =new ListenableDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		
		String xmlFolder="", csvFolder="";		
		Integer k=10;
		int j=0;
		boolean nv=true;
		

		if (args.length > 0)
		{
			while (j < args.length)
			{
				 
				if (args[j].equals("-f"))
				{
					xmlFolder=args[j+1];
				}
				if (args[j].equals("-c"))
				{
					csvFolder=args[j+1];
				}
				if (args[j].equals("-k"))
				{
					k=Integer.parseInt(args[j+1]);
				}
				if (args[j].equals("-nv"))
				{
					nv=false;
				}
				j++;
				
			}
		}
		else			
		{
			System.out.println("Please provide appropriate arguments");
			System.out.println("-f folder containing KGML files -c Folder containing graph csv Files -k Number of random queries(default 10) -nv no visual representation");
			System.exit(1);
		}
		
		if (xmlFolder.isEmpty()==false || csvFolder.isEmpty()==false)
		{

			
			// TODO Auto-generated method stub
			if (nv==true)
		    {
				TransformToGraph applet = new TransformToGraph();
				applet.init();	
				JFrame frame = new JFrame();
		        frame.getContentPane().add(applet);		
		        frame.setTitle("Pathway graph");
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.pack();
		        frame.setVisible(true);
		    }
			if (xmlFolder.isEmpty()==false)
			{
				Organism = new PathwayMap( xmlFolder);	
				AddVerticesAndEdgesFromXML(nv);
			}
			else
			{
				ReadCSV NodesCSV= new ReadCSV(csvFolder+ "/Nodes.csv");
				ArrayList<String> GeneNames= NodesCSV.ReadCol(0);
				
				ArrayList<ArrayList<String>> Edges = new ArrayList<ArrayList<String>>();
				ReadCSV Source= new ReadCSV(csvFolder+ "/Edges.csv");
				Edges.add(Source.ReadCol(0));
				ReadCSV Destination= new ReadCSV(csvFolder+ "/Edges.csv");
				Edges.add(Destination.ReadCol(1));
				
		
				AddVerticesAndEdgesFromCSV(GeneNames, Edges);
			}
			
			//generate genes to query;
			String[][] ToPrint= new String[k][6];
			for (int i=0; i < k; i++)
		    {
				ToPrint[i][0]=GetRandomGene();
				ToPrint[i][1]= GetRandomGene();
	        
		    }
			OutputResults(ToPrint);
		    System.out.println("Done!");
	   
		}
	
	}
	public static  String GetRandomGene()
	{		
		String Genedata = new String();
		//random path
		Random rand = new Random();
		
		ArrayList<String> VertexList = new ArrayList<String>(g.vertexSet());
		
		int r = rand.nextInt(VertexList.size());

		Genedata=VertexList.get(r);
		
		//random gene in pathway	
				
		
		return Genedata;
	}
	public static void OutputResults(String[][] ToPrint)
	{
		 
		ExportCsv Csv = new ExportCsv("GenesToQuery.csv", false,"q1,q2");
		

		System.out.println("q1 \t\t\t q2 "  );
		

		for (String[] s: ToPrint)
		{
			System.out.println(s[0] + "\t\t" + s[1] );
					try {
						Csv.WriteFieldCSV(s[0] ,0);
						Csv.WriteFieldCSV(s[1] ,0);
						Csv.WriteFieldCSV("" ,1);
						Csv.FlushCSV();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
	}
	public void init()
    {
		final Dimension DEFAULT_SIZE = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		 // create a JGraphT graph
	
		 // create a visualization using JGraph, via an adapter
		jgAdapter = new JGraphModelAdapter<String, DefaultEdge>(g);
		JGraph jgraph = new JGraph(jgAdapter);

        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);
        
        
       
    }
	

	public static  void AddVerticesAndEdgesFromCSV(ArrayList<String> geneNames, ArrayList<ArrayList<String>> edges)
	{
		
		String GeneName="";
		for (int i=0; i < geneNames.size(); i++)
		{
			GeneName=geneNames.get(i);
			
			g.addVertex(GeneName);
			
		}
		for (int i=0; i < edges.get(0).size(); i++)
		{
			System.out.println("Source: " +edges.get(0).get(i) + " Destination: " +edges.get(1).get(i) );
			//do not add relations to genes not included in KGML file
			if (g.containsVertex(edges.get(0).get(i)) && g.containsVertex(edges.get(1).get(i)))
				g.addEdge(edges.get(0).get(i).trim() ,edges.get(1).get(i).trim() );
		}
		
		
	}
	public static void AddVerticesAndEdgesFromXML(boolean nv)
	{
		//g=new ListenableDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
    	ArrayList<Pathway> pathways = Organism.pathways;
    	Color col= null;
    	for(Pathway cpway : pathways){
    		
    		col=GenerateColor();
    		if (cpway.getName() != null)
			{
	    		System.out.println(cpway.getName());
	    		
	    			for(Kegg_Entry k : cpway.getEntryL()){   	
	    		
	    			String ko= k.getName();
	    			
	    			if (ko.equals("undefined")==true)
	    				ko=ko + cpway.getName();

	    			g.addVertex(ko);
	    			if (nv==true)
	    			{
	    		  		if (ko.contains("cpd"))
			    		{
			    			positionVertexAt(ko, Integer.parseInt(k.GetGraph().getX()),  Integer.parseInt(k.GetGraph().getY()),  col, Color.WHITE);
			    		}
			    		else
			    			positionVertexAt(ko, Integer.parseInt(k.GetGraph().getX()),  Integer.parseInt(k.GetGraph().getY()), col.brighter(), Color.WHITE);
			    		
			    			
	    			}
		  

	    			//System.out.println(k.getID() + " " + k.getName() + " " + k.GetGraph().getX());
	    			}
	    			
	    			
			}
    		for(Relation r : cpway.getRelationL()){ 
    		
    			String edge1="", edge2="";
    			edge1=cpway.GetNameFromId(r.getEntry1());
    			edge2=cpway.GetNameFromId(r.getEntry2());

    			
    			//append pathname to undefined genes
    			if (edge1.equals("undefined")==true)
    				edge1=edge1 + cpway.getName();
    			if (edge2.equals("undefined")==true)
    				edge2=edge2 + cpway.getName();

    			
    			System.out.println(r.getEntry1() + " " + edge1 + " " + r.getEntry2()+ " " + edge2);
    			try {
    				g.addEdge(edge1 ,edge2 );
    			}
    			catch(NullPointerException e)
    			{
    				System.out.println(g.vertexSet());
    			}
    			//System.out.println("Relations " + cpway.GetNameFromId(r.getEntry1()) +"-" + r.getEntry1() + " " + cpway.GetNameFromId(r.getEntry2())+ "-" + r.getEntry2() );
    		}
    		for(Reaction r : cpway.getReactionL()){
    			if (g.containsVertex(r.getSubstrate().get(0).getName()) && g.containsVertex(r.getProduct().get(0).getName()) )
    			{
    				try {
    				g.addEdge( r.getSubstrate().get(0).getName(),  r.getProduct().get(0).getName());
    				}
    				catch(NullPointerException e)
        			{
        				System.out.println(g.vertexSet());
        			}
    			}
    			//System.out.println("Reaction " + r.getId() +" Subtrate " + r.getSubstrate().get(0).getName() + " Product "  + r.getProduct().get(0).getName()  );
    		} 		 
    		
    	}
    	
   
    
	}


	
	 @SuppressWarnings("unchecked")

	private static void positionVertexAt(Object vertex, int x, int y, Color color, Color FColor )
	    {
		
		 	
	        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
	        
	        AttributeMap attr = cell.getAttributes();
	        attr.applyValue(GraphConstants.BACKGROUND, color);
	        attr.applyValue(GraphConstants.FOREGROUND, FColor);
	        Rectangle2D bounds = GraphConstants.getBounds(attr);

	 
	        
	        Rectangle2D newBounds =
	            new Rectangle2D.Double(
	                x  ,
	                y,
	                bounds.getWidth(),
	                bounds.getHeight());
	        
	        		
	        GraphConstants.setBounds(attr, newBounds);

	        // TODO: Clean up generics once JGraph goes generic
	        AttributeMap cellAttr = new AttributeMap();
	        cellAttr.put(cell, attr);
	        jgAdapter.edit(cellAttr, null, null, null);
	    }
	  private void adjustDisplaySettings(JGraph jg)
	    {
		  final Dimension DEFAULT_SIZE = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	        jg.setPreferredSize(DEFAULT_SIZE);

	        Color c = DEFAULT_BG_COLOR;
	        String colorStr = null;

	        try {
	            colorStr = getParameter("bgcolor");
	        } catch (Exception e) {
	        }

	        if (colorStr != null) {
	            c = Color.decode(colorStr);
	        }

	        jg.setBackground(c);
	    }
	  private static Color GenerateColor()
	  {
		  Random rand = new Random();
		  float r = rand.nextFloat();
		  float g = rand.nextFloat();
		  float b = rand.nextFloat();
		
		  Color randomColor = new Color(r, g, b);
		  
		  
		  /*
		    float r = rand.nextFloat() / 2f + 0.5;
			float g = rand.nextFloat() / 2f + 0.5;
			float b = rand.nextFloat() / 2f + 0.5;
		   */
		  return randomColor;
	  }
	 
}
