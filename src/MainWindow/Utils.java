/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxStylesheet;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Formaiko
 */
public class Utils {
    
    public void createComp(InnerFrame frame){
        frame.graphComponent = new mxGraphComponent(frame.graph);
//graphComponent.setSize(new Dimension(300, 300));
        java.lang.Object parent = frame.graph.getDefaultParent();
        frame.graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxEventSource.mxIEventListener(){
            @Override
            public void invoke(Object sender, mxEventObject evt)  {
                frame.edge_count++;
                Main.edge_text.setText("Number of edges: " + frame.edge_count);
                Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Edge created.");
            }
        });
        
        frame.graphComponent.getGraphControl().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.x = e.getX();
                frame.y = e.getY();
                frame.graph.insertVertex(parent, null, frame.vertex_id, frame.x-40, frame.y-10, 80, 30);
                frame.vertex_id++;
                frame.vertex_count++;
                Main.vertex_text.setText("Number of vertexes: " + frame.vertex_count);
                Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Vertex created.");
            }
        });
//f.getContentPane().add(BorderLayout.CENTER, graphComponent);
        frame.add(frame.graphComponent, BorderLayout.CENTER);
    }
    
    
    /***** OpenFile funkce pro Load *****/
    public void OpenFile(InnerFrame inner) throws SAXException, IOException, ParserConfigurationException, TransformerException{
        JFileChooser fc = new JFileChooser();
        int returnVal;
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fc.addChoosableFileFilter(xmlfilter);
        fc.setDialogTitle("Choose file");
        returnVal = fc.showOpenDialog(inner);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            inner.soubor = fc.getSelectedFile();
            inner.setTitle(fc.getSelectedFile().getName());
        } else {
            System.err.println("Otevirani zruseno uzivatelem.");
            return;
        }
        XMLconvertor graphmlToMxgraph = new XMLconvertor(inner.soubor);
        inner.xml = graphmlToMxgraph.convertLoaded(inner.soubor);
    }
    
    
    public void setGraph(InnerFrame inner) throws Exception{
        deleteAll(inner);
        java.lang.Object parent = inner.graph.getDefaultParent();
        Document doc;
        
        //graphml was loaded at first
        if("MxGraph".equals(inner.xml)){
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inner.soubor);//mxgraph load
        }
        else{
            doc = loadXMLFromString(inner.xml);
        }
        
        NodeList nList = doc.getElementsByTagName("mxCell");
        NodeList nListGeo = doc.getElementsByTagName("mxGeometry");
        String value = "";
        int source_v, target_v = 0;
        String vertex_id = "";
        Double x_coord,y_coord,w,h = 0.0;
        
        if(nList.getLength()>0){
            for (int i = 0; i < nList.getLength(); i++) {
                if("1".equals(((Element) nList.item(i)).getAttribute("vertex"))){ //vertex
                    value = ((Element) nList.item(i)).getAttribute("value");
                    x_coord = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("x"));
                    vertex_id = ((Element) nList.item(i-2)).getAttribute("id");
                    y_coord = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("y"));
                    w = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("width"));
                    h = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("height"));
                    Object v = inner.graph.insertVertex(parent, vertex_id, value, x_coord, y_coord, w, h);
                    inner.vertex_array.add(v);
                    inner.vertex_id++;
                    inner.vertex_count++;
                }
                else{
                    inner.vertex_array.add(null);
                }
            }
        }
        
        if(nList.getLength()>0){
            for (int i = 0; i < nList.getLength(); i++) {
                if("1".equals(((Element) nList.item(i)).getAttribute("edge"))){ //vertex
                    source_v = Integer.parseInt(((Element) nList.item(i)).getAttribute("source"));
                    target_v = Integer.parseInt(((Element) nList.item(i)).getAttribute("target"));
                //    System.out.println("Source id:" + source_v + "Dest id:" + target_v);
                    if("MxGraph".equals(inner.xml))
                        inner.graph.insertEdge(parent, null, "", inner.vertex_array.get(source_v), inner.vertex_array.get(target_v));
                    else
                        inner.graph.insertEdge(parent, null, "", inner.vertex_array.get(source_v), inner.vertex_array.get(target_v));
                    inner.edge_count++;
                }
            }
            System.out.println("Inserted edges");
        }
        Main.vertex_text.setText("Number of vertexes: " + inner.vertex_count);
        Main.edge_text.setText("Number of edges: " + inner.edge_count);
    }
    
    
    public void deleteAll(InnerFrame inner){
        inner.graph.removeCells(inner.graph.getChildCells(inner.graph.getDefaultParent(), true, true));
        inner.vertex_id = 0;
        inner.vertex_count = 0;
        inner.edge_count = 0;
        inner.vertex_array.clear();
        Main.vertex_text.setText("Number of vertexes: " + inner.vertex_count);
        Main.edge_text.setText("Number of edges: " + inner.edge_count);
    }
    
    
    /* DOM BUILDER FOR STRING */
    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
    
    /* Oriented edges */
    public void applyEdgeDefaultsOriented(InnerFrame inner) {
        Map<String, Object> edge = new HashMap<String, Object>();
        edge.put(mxConstants.STYLE_ROUNDED, false);
        edge.put(mxConstants.STYLE_ORTHOGONAL, false);
        edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");
        edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
        edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");
        
        mxStylesheet edgeStyle = new mxStylesheet();
        edgeStyle.setDefaultEdgeStyle(edge);
        inner.graph.setStylesheet(edgeStyle);
    }
    
        /* Unoriented edges */
    public void applyEdgeDefaults(InnerFrame inner) {
        // Settings for edges
        Map<String, Object> edge = new HashMap<String, Object>();
        edge.put(mxConstants.STYLE_ROUNDED, false);//TODO
        edge.put(mxConstants.STYLE_ORTHOGONAL, false);
        //edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");//TODO
        edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
        edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");
        
        mxStylesheet edgeStyle = new mxStylesheet();
        edgeStyle.setDefaultEdgeStyle(edge);
        inner.graph.setStylesheet(edgeStyle);
        
    }
    
    public boolean saveFile(String location, InnerFrame inner, String filename){
        try{
            PrintWriter out = new PrintWriter(location);
            out.println(inner.xml);
            out.close();
            Main.action_performed.setText(Main.action_performed.getText()+"\n"+filename+" succesfully saved");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
}
