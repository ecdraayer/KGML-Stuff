package TransformToGraph;

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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

/**
 * This class generates a graphical representation from the shortest path calculated in TransformtoGraph.
 * Takes path as parameter and creates visual representation.
 * @author Raul Alvarado
 *
 */
public class ShortestPathtoGraph extends JApplet {

	private static final long serialVersionUID = 7980400801849305625L;
	private static final Dimension DEFAULT_SIZE = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static JGraphModelAdapter<String, DefaultEdge> jgAdapter;
    int numberofpathways =0;

    static ListenableGraph<String, DefaultEdge> g ;

	public ShortestPathtoGraph ()
	{

	
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
		g = new ListenableDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		 // create a visualization using JGraph, via an adapter
		jgAdapter = new JGraphModelAdapter<String, DefaultEdge>(g);
		JGraph jgraph = new JGraph(jgAdapter);

        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);
        
   
        
    
    }
	public void CreateGraph(List<DefaultEdge>  path, ArrayList<Pathway>  pathways)
    {
		String posx="", posx2="";
		String posy="", posy2="";
		for (int i=0; i<path.size();i++)
		{
		    String[] vertex = path.get(i).toString().split(" : ");
		    vertex[0]= vertex[0].replace("(", "");
		    vertex[1]= vertex[1].replace(")", "");	
		    g.addVertex(vertex[0]);
		    g.addVertex(vertex[1]);
		    g.addEdge(vertex[0],vertex[1]);
		 
		    
		    Kegg_Entry entry= new Kegg_Entry();
		    Kegg_Entry entry2= new Kegg_Entry();
		    for(Pathway cpway : pathways){
		        
		        if (cpway.GetPathwayFromGene(vertex[0]).GetGraph() != null)
		        	entry= cpway.GetPathwayFromGene(vertex[0]); 
			   
		        if (cpway.GetPathwayFromGene(vertex[1]).GetGraph() != null)
		        	entry2=cpway.GetPathwayFromGene(vertex[1]);
			
		    }
		    
		
		    
		    
		    
		    posx=entry.GetGraph().getX();
		    posx2=entry2.GetGraph().getX(); 
		    posy=entry.GetGraph().getY();
		    posy2=entry2.GetGraph().getY();
		    positionVertexAt(vertex[0],Integer.parseInt(posx), Integer.parseInt(posy) );
		   // posx+=160;
		   // posy+=100;
		    positionVertexAt(vertex[1],Integer.parseInt(posx2), Integer.parseInt(posy2) );
		    
		    
		    

		}
	 
    }

	@SuppressWarnings("unchecked")
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
