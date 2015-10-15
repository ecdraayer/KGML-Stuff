package TransformToGraph;



import com.mxgraph.swing.*;

import KGMLParser.Kegg_Entry;
import KGMLParser.Pathway;
import KGMLParser.PathwayMap;
import KGMLParser.Reaction;
import KGMLParser.Relation;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.LineBorder;

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
	static ListenableGraph<String, DefaultEdge> g ;

	public static void main(String[] args) {
		
		String xmlFolder="";
		Integer k=10;
		int j=0;
		boolean nv=true;
		

		if (args.length >= 0)
		{
			while (j < args.length)
			{
				 
				if (args[j].equals("-f"))
				{
					xmlFolder=args[j+1];
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
			System.out.println("-f folder containing KGML files -k Number of random queries(default 10) -nv no visual representation");
			System.exit(1);
		}
		
		if (xmlFolder.isEmpty()==false)
		{
			//measure xml loading time.
			long startTime = System.nanoTime();				
			Organism = new PathwayMap( xmlFolder);
			long stopTime = System.nanoTime();
			
			double readElapsed = (stopTime - startTime);
			//readElapsed= TimeUnit.MILLISECONDS.convert(readElapsed, TimeUnit.NANOSECONDS);
			readElapsed= (readElapsed/1000000.0);
			System.out.println("Reading File time(miliseconds) "+ readElapsed);
			
			
			g=new ListenableDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
			// TODO Auto-generated method stub
		
			TransformToGraph applet = new TransformToGraph();
			
			if (nv==true)
		    {
				applet.init();	
				JFrame frame = new JFrame();
		        frame.getContentPane().add(applet);		
		        frame.setTitle("Pathway graph");
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.pack();
		        frame.setVisible(true);
		    }
			
		    AddVerticesAndEdges(nv);
	   
	        String[] Gene1 = null,Gene2 = null, temp;
	        QueryGenerator Query = new QueryGenerator(Organism);
	        
	        String[][] ToPrint= new String[k*2][6];
	        		
	        
	        for (int i=0; i < k*2; i++)
	        {
	            //search for path from both directions
	        	if (i%2==0)
	        	{
			        Gene1= Query.GetRandomGene();
			        Gene2= Query.GetRandomGene();
	        	}
	        	else
	        	{
	        		temp =Gene1;
	        		Gene1=Gene2;
	        		Gene2=	temp;
	        	}
		        //Gene1[1]="ko:K00875";
		        //Gene2[1]="ko:K03081";	
		   

			   
			    startTime = System.nanoTime();	
				List<DefaultEdge> path = DijkstraShortestPath.findPathBetween(g, Gene1[1], Gene2[1]);
				stopTime = System.nanoTime();		
				
				double FindPathElapsed = (stopTime - startTime);
				//readElapsed= TimeUnit.MILLISECONDS.convert(readElapsed, TimeUnit.NANOSECONDS);
				FindPathElapsed=  (FindPathElapsed/1000000.0);
				System.out.println("Shortest path from " + Gene1[1] + " from pathway "+ Gene1[0] + " to " + Gene2[1] + " from pathway " + Gene2[0] + " in " + FindPathElapsed +" (miliseconds)");
			    
				
				
				
				ToPrint[i][0]=Gene1[1];
				ToPrint[i][1]=Gene2[1];
				    if (path != null )
				    {
					    if (path.size()>0 )
					    {
						    System.out.println(path);
						    if (nv==true)
						    {
							    ShortestPathtoGraph SG= new ShortestPathtoGraph();
							    SG.CreateGraph(path, Organism.pathways);
						    }
						    
						    
						    
						    int NoPathways=Query.DistinctPathways(path).size();
						    
						
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
	        Query.OutputResults(ToPrint, readElapsed);
	        System.out.println("Done!");
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
	
	
	
	private static void AddVerticesAndEdges(boolean nv)
	{
		     
    	ArrayList<Pathway> pathways = Organism.pathways;
    	Color col= null;
    	for(Pathway cpway : pathways){
    	
    		col=GenerateColor();
    		if (cpway.getName() != null)
			{
	    		System.out.println(cpway.getName());
	    		
	    			for(Kegg_Entry k : cpway.getEntryL()){   	
	    		
	    			String ko= k.getName();
	    		
	    		
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
    		
    			g.addEdge( cpway.GetNameFromId(r.getEntry1()), cpway.GetNameFromId(r.getEntry2()));
    			//System.out.println("Relations " + cpway.GetNameFromId(r.getEntry1()) +"-" + r.getEntry1() + " " + cpway.GetNameFromId(r.getEntry2())+ "-" + r.getEntry2() );
    		}
    		for(Reaction r : cpway.getReactionL()){
    			if (g.containsVertex(r.getSubstrate().get(0).getName()) && g.containsVertex(r.getProduct().get(0).getName()) )
    				g.addEdge( r.getSubstrate().get(0).getName(),  r.getProduct().get(0).getName());
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
