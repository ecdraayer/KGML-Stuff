/*-------------------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * KGML Writer: Takes in a data structure created in the parser, and writes it into a new KGML file
 *-----------------------------------------------------------------------------------------*/
package KGMLParser;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KGMLWrite {
	public KGMLWrite(Pathway pathway) {
		 
		  try {
	 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root element pathway
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("pathway");
			doc.appendChild(rootElement);
			
			// attributes of root element pathway
			rootElement.setAttribute("name", pathway.getName());
			rootElement.setAttribute("org", pathway.getOrg());
			rootElement.setAttribute("number", pathway.getNumber());
			rootElement.setAttribute("image", pathway.getImage());
			rootElement.setAttribute("link", pathway.getLink());

            //Entry elements
			for(Kegg_Entry tmpE : pathway.getEntryL()){
				Element e = doc.createElement("entry");
			    if(tmpE.getID() != null)
			       e.setAttribute("id", tmpE.getID());
			    if(tmpE.getName() != null)
			       e.setAttribute("name", tmpE.getName());
			    if(tmpE.getType() != null)
			       e.setAttribute("type", tmpE.getType());
			    if(tmpE.getReaction() != null)
			       e.setAttribute("reaction", tmpE.getReaction());
			    if(tmpE.getLink() != null)
			       e.setAttribute("link", tmpE.getLink());
			    rootElement.appendChild(e);
			}
			
	        //Relation Elements
			for(Relation tmpRe : pathway.getRelationL()){
				Element relation = doc.createElement("relation");
				if(tmpRe.getEntry1() != null) relation.setAttribute("entry1", tmpRe.getEntry1());
				if(tmpRe.getEntry2() != null) relation.setAttribute("entry2", tmpRe.getEntry2());
				if(tmpRe.getType() != null) relation.setAttribute("type", tmpRe.getType());
				//Subtype Elements (Children of Relation elements)
				for(Subtype tmpS : tmpRe.getSubtypes()){
					Element subtype = doc.createElement("subtype");
					if(tmpS.getName() != null) subtype.setAttribute("name", tmpS.getName());
					if(tmpS.getValue() != null) subtype.setAttribute("value", tmpS.getValue());
					relation.appendChild(subtype);
				}
				rootElement.appendChild(relation);
				
			}
			
			//Reaction Elements
			for(Reaction tmpR : pathway.getReactionL()){
				Element reaction = doc.createElement("reaction");
				if(tmpR.getId() != null) reaction.setAttribute("id", tmpR.getId());
				if(tmpR.getName() != null) reaction.setAttribute("name", tmpR.getName());
				if(tmpR.getType() != null) reaction.setAttribute("type", tmpR.getType());
				for(Substrate substrate : tmpR.getSubstrate()){
					Element sub = doc.createElement("substrate");
					if(substrate.getId() != null) sub.setAttribute("id", substrate.getId());
					if(substrate.getName() != null) sub.setAttribute("name", substrate.getName());
					reaction.appendChild(sub);
				}
				for(Product Pro : tmpR.getProduct()){
					Element product = doc.createElement("product");
					if(Pro.getId() != null) product.setAttribute("id",Pro.getId());
					if(Pro.getName() != null) product.setAttribute("name", Pro.getName());
					reaction.appendChild(product);
				}
				rootElement.appendChild(reaction);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			String filename = pathway.getName();
			StreamResult result = new StreamResult(new File("file"+filename+".xml")); /* Name of KGML file */
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
			System.out.println("File saved!");
	 
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		}
}
