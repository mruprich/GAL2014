/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Dan
 */
public class XMLconvertor {
    public File soubor;
    private ArrayList<String> Node_representation = new ArrayList<String>();
    private ArrayList<Integer> x_node_representation = new ArrayList<Integer>();
    private ArrayList<Integer> y_node_representation = new ArrayList<Integer>();
    private ArrayList<String> Source_representation = new ArrayList<String>();
    private ArrayList<String> Target_representation = new ArrayList<String>();
    private int x_coord = 0;
    private int y_coord = 0;
    XMLconvertor(File f){
        soubor = f;
    }
    
    public String convertToMx() throws ParserConfigurationException, TransformerConfigurationException, TransformerException{
    String result = "";
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    Document doc = docBuilder.newDocument();
    Element rootElement = doc.createElement("mxGraphModel");
    doc.appendChild(rootElement);
    
    Element root = doc.createElement("root");
    rootElement.appendChild(root);

    Element mx0 = doc.createElement("mxCell");
    Attr attr = doc.createAttribute("id");
    attr.setValue("0");
    mx0.setAttributeNode(attr);
    root.appendChild(mx0);
    
    Attr attr_parent = doc.createAttribute("parent"); //globalni
    attr_parent.setValue("1");
        
    
    
    Element mx1 = doc.createElement("mxCell");
    attr = doc.createAttribute("id");
    attr.setValue("1");
    mx1.setAttributeNode(attr);
    mx1.setAttributeNode(attr_parent);
    root.appendChild(mx1);
    
    Element mx;
    Element sub_mx;
    for(int i = 0; i < Node_representation.size(); i++){
        Attr x = doc.createAttribute("x");
        Attr y = doc.createAttribute("y");
        Attr height = doc.createAttribute("height");
        Attr value = doc.createAttribute("value");
        Attr width = doc.createAttribute("width");
        Attr vertex = doc.createAttribute("vertex");
        Attr as = doc.createAttribute("as");
        Attr attr_loop1 = doc.createAttribute("id");
        Attr attr2 = doc.createAttribute("parent"); //globalni
        
        x.setValue(x_node_representation.get(i).toString());
        y.setValue(y_node_representation.get(i).toString());
        height.setValue("30.0");
        value.setValue(Node_representation.get(i));
        width.setValue("80.0");
        vertex.setValue("1");
        as.setValue("geometry");
        attr_loop1.setValue(Integer.toString(i+2));
        attr2.setValue("1");
        
        mx = doc.createElement("mxCell");
        mx.setAttributeNode(attr_loop1); //id
        mx.setAttributeNode(attr2); //parent
        mx.setAttributeNode(value); //value
        mx.setAttributeNode(vertex); //vertex
        
        sub_mx = doc.createElement("mxGeometry");
        sub_mx.setAttributeNode(as);
        sub_mx.setAttributeNode(height);
        sub_mx.setAttributeNode(width);
        sub_mx.setAttributeNode(x);
        sub_mx.setAttributeNode(y);
        
        mx.appendChild(sub_mx);
        root.appendChild(mx);
    }
    
    Element sub_sub_mx;
    for(int i = 0; i < Source_representation.size(); i++){
        mx = doc.createElement("mxCell");
        
        Attr attr2 = doc.createAttribute("parent"); //globalni 
        Attr as = doc.createAttribute("as");
        Attr attr_loop = doc.createAttribute("id");
        Attr edge = doc.createAttribute("edge");
        Attr point_source = doc.createAttribute("as");
        Attr point_target = doc.createAttribute("as");
        Attr source_v = doc.createAttribute("source");
        Attr target_v = doc.createAttribute("target");
        Attr style = doc.createAttribute("style");
        Attr edge_v = doc.createAttribute("value");
        Attr relative = doc.createAttribute("relative");
        
        /*x.setValue((x_node_representation.get(Source_representation.get(i))));
        y.setValue(Source_representation.get(i).toString());
        x2.setValue(Target_representation.get(i).toString());
        y2.setValue(Target_representation.get(i).toString());*/
        attr2.setValue("1");
        as.setValue("geometry");
        attr_loop.setValue(Integer.toString(i+Node_representation.size()+2));
        edge.setValue("1");    
        point_source.setValue("sourcePoint");
        point_target.setValue("targetPoint");
        source_v.setValue(Source_representation.get(i));
        target_v.setValue(Target_representation.get(i));
        style.setValue("");
        edge_v.setValue("");
        relative.setValue("1");

        mx.setAttributeNode(edge); //edge
        mx.setAttributeNode(attr_loop); //id
        mx.setAttributeNode(attr2); //parent
        
        mx.setAttributeNode(source_v);
        mx.setAttributeNode(style);
        mx.setAttributeNode(target_v);
        mx.setAttributeNode(edge_v);
        
        sub_mx = doc.createElement("mxGeometry");
        sub_mx.setAttributeNode(as);
        sub_mx.setAttributeNode(relative);        
        mx.appendChild(sub_mx);
        root.appendChild(mx);
    }
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource source = new DOMSource(doc);
    // Output to console for testing
    StringWriter outWriter = new StringWriter();
    StreamResult res1 = new StreamResult(outWriter);
    transformer.transform(source, res1);
    //get string
    StringBuffer sb = outWriter.getBuffer(); 
    String finalstring = sb.toString();
    return finalstring;
    }
    
    public String convertLoaded(File f) throws SAXException, IOException, ParserConfigurationException, TransformerException{
        soubor = f;
        String result;
        String jmeno = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(soubor);
         //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        jmeno = doc.getDocumentElement().getNodeName();
        if("mxGraphModel".equals(jmeno)){
            return "MxGraph";
        }
        NodeList nList = doc.getElementsByTagName("node");
        NodeList nListE = doc.getElementsByTagName("edge");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node_representation.add(((Element) nList.item(temp)).getAttribute("id"));
            x_node_representation.add(x_coord);
            y_node_representation.add(y_coord);
            x_coord += 100;
            if(x_coord > 800){ //maximalne 8x za sebou vertex
                x_coord = 0;
                y_coord += 50;
            }
        }

        for (int temp = 0; temp < nListE.getLength(); temp++) {
            Source_representation.add(((Element) nListE.item(temp)).getAttribute("source"));
            Target_representation.add(((Element) nListE.item(temp)).getAttribute("target"));
        }

        result = convertToMx();
        return result;
    }
}
