/*-------------------------------------------------------------------------------
 * Author:             Erick Draayer
 * Written:            7/27/15
 * Last Updated:       7/27/15
 * 
 * Parses KGML files using SAX and stores the information in a Pathway DataStructures (ArrayLists)
 *
 *-----------------------------------------------------------------------------*/

package KGMLFunctions;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class MySaxParser extends DefaultHandler {
    String KeggXmlFileName; /* Name of file being parsed */
    
    /* temp values storing various data */
    String tmpValue;
    Kegg_Entry entryTmp;  
    Relation relationTmp;
    Reaction reactionTmp;
    Pathway pathwaytmp;
    Subtype subtypeTmp;
    Substrate substrateTmp;
    Product productTmp;
    Graph graphTmp;

    /*--------------------------------------------------------------------------
     * SAX parser function, calls parse function. 
     * Input: File name of KGML file and a empty pathway to fill
     *--------------------------------------------------------------------------*/
    public MySaxParser(String KeggXmlFileName, Pathway pathway) {
        this.KeggXmlFileName = KeggXmlFileName;
        pathwaytmp = pathway;
        parseDocument();
    }
    
    /*-----------------------------------------------------------------------------
     * Parse function that makes sure the SAX parser can operate on the given file,
     * gives error if there is an error in the file or IO problem
     *--------------------------------------------------------------------------*/
    private void parseDocument() {
        // parse
        SAXParserFactory factory = SAXParserFactory.newInstance(); /* Create new instance of SAX parser */
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(KeggXmlFileName, this); /* Pass name of file and this handle to parser */
        /* Errors with brief descriptions */
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
        } catch (IOException e) {
            System.out.println("IO error");
        }
    }
    
    /*-----------------------------------------------------------------------------------------------
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     *
     * Override of startElement, looks for the elements entry, id, name, reaction, link, and type within KGML file
     *---------------------------------------------------------------------------------------------*/
    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {

    	if (elementName.equalsIgnoreCase("entry")) {
            entryTmp = new Kegg_Entry();
            entryTmp.setID(attributes.getValue("id"));
            entryTmp.setName(attributes.getValue("name"));
            entryTmp.setReaction(attributes.getValue("reaction"));
            entryTmp.setLink(attributes.getValue("link"));
            entryTmp.setType(attributes.getValue("type"));
        }

        if(elementName.equalsIgnoreCase("graphics")){
        	entryTmp.setGraph(attributes.getValue("name"),attributes.getValue("fgcolor"),
        			          attributes.getValue("bgcolor"),attributes.getValue("type"),
        			          attributes.getValue("x"),attributes.getValue("y"),
        			          attributes.getValue("width"), attributes.getValue("height"));
        }
        if(elementName.equalsIgnoreCase("pathway")){
        	pathwaytmp.setName(attributes.getValue("name"));
        	pathwaytmp.setOrg(attributes.getValue("org")); 
        	pathwaytmp.setNumber(attributes.getValue("number"));
        	pathwaytmp.setTitle(attributes.getValue("title"));
            pathwaytmp.setImage(attributes.getValue("image"));
            pathwaytmp.setLink(attributes.getValue("link"));
            pathwaytmp.setVisited(false);

        }
        if(elementName.equalsIgnoreCase("relation")){
        	relationTmp = new Relation();
        	relationTmp.setEntry1(attributes.getValue("entry1"));
        	relationTmp.setEntry2(attributes.getValue("entry2"));
        	relationTmp.setType(attributes.getValue("type"));
        }
        if(elementName.equalsIgnoreCase("subtype")){
           subtypeTmp = new Subtype(attributes.getValue("name"), attributes.getValue("value"));
        }
        if(elementName.equalsIgnoreCase("reaction")){
        	reactionTmp = new Reaction();
        	reactionTmp.setId(attributes.getValue("id"));
        	reactionTmp.setName(attributes.getValue("name"));
            reactionTmp.setType(attributes.getValue("type"));
        }
        if(elementName.equalsIgnoreCase("substrate")){
        	substrateTmp = new Substrate(attributes.getValue("id"), attributes.getValue("name"));
        }
        if(elementName.equalsIgnoreCase("product")){
        	productTmp = new Product(attributes.getValue("id"), attributes.getValue("name"));
        }
    }
    
    /*--------------------------------------------------------------------------------------------------------
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     * 
     * Override of endElement, adds data to Pathway class when it detects the end of the elements entry, relation, reaction, subtype, substrate, product
     *-------------------------------------------------------------------------------------------------------*/
    @Override
    public void endElement(String s, String s1, String element) throws SAXException {

        // if end of entry element add to entry list
        if (element.equals("entry")) {
            pathwaytmp.entryL.add(entryTmp);
        }
        if (element.equals("relation")){
        	pathwaytmp.relationL.add(relationTmp);
        }
        if (element.equals("reaction")){
        	pathwaytmp.reactionL.add(reactionTmp);
        }
        if (element.equals("subtype")) {
        	relationTmp.getSubtypes().add(subtypeTmp);
        }
        if (element.equals("substrate")){
        	reactionTmp.getSubstrate().add(substrateTmp);
        }
        if (element.equals("product")){
        	reactionTmp.getProduct().add(productTmp);
        }

    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}