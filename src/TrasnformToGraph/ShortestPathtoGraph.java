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

public class ShortestPathtoGraph extends JApplet {

	private static final long serialVersionUID = 7980400801849305625L;
	private static final Dimension DEFAULT_SIZE = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static JGraphModelAdapter<String, DefaultEdge> jgAdapter;


    ListenableGraph<String, DefaultEdge> g ;

	public ShortestPathtoGraph ()
	{
		this.g= new ListenableDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

		//ShortestPathtoGraph applet2 = new ShortestPathtoGraph();
		//applet2.CreateGraph();
		//this.path = Path;
		init();
		
		JFrame frame = new JFrame();
        frame.getContentPane().add(this);		
        frame.setTitle("Shortest path graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

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
        
        
    
    }
	public void CreateGraph(List<DefaultEdge>  path)
    {
		int posx=0;
		int posy=0;
		for (int i=0; i<path.size();i++)
		{
		    String[] vertex = path.get(i).toString().split(" : ");
		    vertex[0]= vertex[0].replace("(", "");
		    vertex[1]= vertex[1].replace(")", "");	
	
		    g.addVertex(vertex[0]);
		    g.addVertex(vertex[1]);
		    g.addEdge(vertex[0],vertex[1]);
		    
		    positionVertexAt(vertex[0],posx, posy );
		    posx+=190;
		    posy+=130;
		    positionVertexAt(vertex[1],posx, posy );
		}
	 
    }
	private void positionVertexAt(Object vertex, int x, int y )
    {
		 DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
	        
	        AttributeMap attr = cell.getAttributes();
	        //attr.applyValue(GraphConstants.BACKGROUND, color);
	        //attr.applyValue(GraphConstants.FOREGROUND, FColor);
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
}
