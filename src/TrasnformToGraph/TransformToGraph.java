package TrasnformToGraph;


import com.mxgraph.layout.*;
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
	 * This class transforms pathways(kgmls) to graphs
	 *   @author Raul Alvarado
	 */
	private static final long serialVersionUID = 7980400801849305625L;
	private static final Dimension DEFAULT_SIZE = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static JGraphModelAdapter<String, DefaultEdge> jgAdapter;

    
	static ListenableGraph<String, DefaultEdge> g =new ListenableDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TransformToGraph applet = new TransformToGraph();
		applet.init();
		

		JFrame frame = new JFrame();
        frame.getContentPane().add(applet);		
        frame.setTitle("Pathway graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

   
        System.out.println("Shortest path from " + "ko:K01184" + " to " + "ko:K01184" );
	    List<DefaultEdge> path = DijkstraShortestPath.findPathBetween(g, "ko:K01184", "ko:K00880");
	    
	    System.out.println(path);
	    ShortestPathtoGraph SG= new ShortestPathtoGraph();
	    SG.CreateGraph(path);
	}

	public void init()
    {
		 // create a JGraphT graph
	
		 // create a visualization using JGraph, via an adapter
		jgAdapter = new JGraphModelAdapter<String, DefaultEdge>(g);
		JGraph jgraph = new JGraph(jgAdapter);

        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);
        
        
        AddVerticesAndEdges();
    }
	
	
	
	private void AddVerticesAndEdges()
	{
		PathwayMap Organism = new PathwayMap();        
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
		    		if (ko.contains("cpd"))
		    		{
		    			positionVertexAt(ko, Integer.parseInt(k.GetGraph().getX()),  Integer.parseInt(k.GetGraph().getY()),  col, Color.WHITE);
		    		}
		    		else
		    			positionVertexAt(ko, Integer.parseInt(k.GetGraph().getX()),  Integer.parseInt(k.GetGraph().getY()), col.brighter(), Color.WHITE);
		    		
		    			

	    			System.out.println(k.getID() + " " + k.getName() + " " + k.GetGraph().getX());
	    			}
	    			
	    			
			}
    		for(Relation r : cpway.getRelationL()){ 
    		
    			g.addEdge( cpway.GetNameFromId(r.getEntry1()), cpway.GetNameFromId(r.getEntry2()));
    			System.out.println("Relations " + cpway.GetNameFromId(r.getEntry1()) +"-" + r.getEntry1() + " " + cpway.GetNameFromId(r.getEntry2())+ "-" + r.getEntry2() );
    		}
    		for(Reaction r : cpway.getReactionL()){
    			g.addEdge( r.getSubstrate().get(0).getName(),  r.getProduct().get(0).getName());
    			System.out.println("Reaction " + r.getId() +" Subtrate " + r.getSubstrate().get(0).getName() + " Product "  + r.getProduct().get(0).getName()  );
    		} 		 
    		
    	}
    	
   
    
	}


	
	 @SuppressWarnings("unchecked")
	 ArrayList<Integer> count= new ArrayList<Integer>();
	private void positionVertexAt(Object vertex, int x, int y, Color color, Color FColor )
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
	  private Color GenerateColor()
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
