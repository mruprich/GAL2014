/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package MainWindow;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxStylesheet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
    private ArrayList<Integer> list_x = new ArrayList<Integer>();
    private ArrayList<Integer> list_y = new ArrayList<Integer>();
    private int index = -1;
    private Object image;
    private Object imgage;
    public void createComp(InnerFrame frame){
        frame.graphComponent = new mxGraphComponent(frame.graph);
//graphComponent.setSize(new Dimension(300, 300));
        java.lang.Object parent = frame.graph.getDefaultParent();
        frame.graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxEventSource.mxIEventListener(){
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                frame.edge_count++;
                Main.edge_text.setText("Number of edges: " + frame.edge_count);
//Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Edge created.");
            }
        });
        frame.graph.addListener(mxEvent.CELLS_MOVED, new mxEventSource.mxIEventListener(){
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                Map<String, Object> props = evt.getProperties();
                Object[] cells = (Object[]) props.get("cells");
                if ((cells != null) && (cells.length > 0)) {
                    mxCell cell = (mxCell) frame.graph.getSelectionCell();
//get current mouse info to get initial position of cell
                    PointerInfo a = MouseInfo.getPointerInfo();
                    Point b = a.getLocation();
                    int x = (int) b.getX();
                    int y = (int) b.getY();
//end of current mouse info
                    int dx = ((Double) props.get("dx")).intValue();
                    int dy = ((Double) props.get("dy")).intValue();
                    int prev_x = 0;
                    int prev_y = 0;
                    if(x > dx) prev_x = x - dx;
                    else prev_x = x + dx;
                    if(y > dx) prev_y = y - dy;
                    else prev_y = y + dy;
                    goThroughList(prev_x,prev_y,list_x,80,frame,"change",x, y);
                }
            }
        });
        frame.graphComponent.getGraphControl().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    frame.x = e.getX();
                    frame.y = e.getY();
                    int x = e.getX();
                    int y = e.getY();
                    mxCell cell = goThroughList(x,y,list_x,80,frame,"",0,0);
                    if(cell != null){
                        return;
                    }
                    list_x.add(frame.x);
                    list_y.add(frame.y);
                    Object v = frame.graph.insertVertex(parent, null, frame.vertex_id, frame.x-40, frame.y-10, 80, 30,"fillColor=none");
                    frame.vertexes.add(v);
                    frame.vertex_id++;
                    frame.vertex_count++;
                    Main.vertex_text.setText("Number of vertexes: " + frame.vertex_count);
//Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Vertex created.");
                }
                else{
                    int x = e.getX();
                    int y = e.getY();
                    goThroughList(x,y,list_x,80,frame,"remove",0,0);
                    frame.graph.getModel().remove(frame.graph.getSelectionCell());
                    frame.vertex_id--;
                    frame.vertex_count--;
                    Main.vertex_text.setText("Number of vertexes: " + frame.vertex_count);
                }
            }
        });
//f.getContentPane().add(BorderLayout.CENTER, graphComponent);
        frame.add(frame.graphComponent, BorderLayout.CENTER);
    }
    public mxCell goThroughList(int x,int y, ArrayList<Integer> list,int limit,InnerFrame frame, String action, int new_position_x, int new_position_y){
        mxCell cell = null;
        for(int i = 0; i < list.size();i++){
            int ele = list.get(i);
            if(ele < x + limit && ele > x - limit){
                int ele_y = list_y.get(i);
                if(ele_y < 30 + y && ele_y > y - 30){
                    index = i;
                    cell = (mxCell) frame.graphComponent.getCellAt(ele, ele_y, false);
                    if(action == "remove"){
                        list_x.remove(i);
                        list_y.remove(i);
                        return null;
                    }
                    else if(action == "change"){
                        list_x.remove(i);
                        list_y.remove(i);
                        list_x.add(new_position_x);
                        list_y.add(new_position_y);
                    }
                    return cell;
                }
            }
        }
        return cell;
    }
    /***** OpenFile funkce pro Load *****/
    public boolean OpenFile(InnerFrame inner) throws SAXException, IOException, ParserConfigurationException, TransformerException{
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
            return false;
        }
        XMLconvertor graphmlToMxgraph = new XMLconvertor(inner.soubor);
        inner.xml = graphmlToMxgraph.convertLoaded(inner.soubor);
        if(inner.xml.equals("directed")){
            Main.action_performed.setText(Main.action_performed.getText()+"\n"+" Could not load because of directed edges, alghoritm wont work.");
            return false;
        }
        return true;
    }
    
    
    public void setGraph(InnerFrame inner) throws Exception{
        deleteAll(inner);
        java.lang.Object parent = inner.graph.getDefaultParent();
        Document doc = null;
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
        String vertex_id = "";
        int vertex_id2 = -1;
        Double x_coord,y_coord,w,h = 0.0;
        inner.graph.getModel().beginUpdate();
        try {
            if(nList.getLength()>0){
                for (int i = 0; i < nList.getLength(); i++) {
                    if("1".equals(((Element) nList.item(i)).getAttribute("vertex"))){ //vertex
                        value = ((Element) nList.item(i)).getAttribute("value");
                        x_coord = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("x"));
                        if("MxGraph".equals(inner.xml)){
                            vertex_id = ((Element) nList.item(i-2)).getAttribute("id");
                        }else{
                            vertex_id = value;
                            if(vertex_id.matches("-?\\d+")){
                                vertex_id = ((Element) nList.item(i)).getAttribute("id");
                            }
                        }
                        y_coord = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("y"));
                        w = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("width"));
                        h = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("height"));
                        Object v = inner.graph.insertVertex(parent, vertex_id, value, x_coord, y_coord, w, h,"fillColor=none");
                        inner.vertex_array.add(v);
                        list_x.add(x_coord.intValue());
                        list_y.add(y_coord.intValue());
                        mxCell vert = (mxCell) v;

                        if(vert.isVertex()){
                            inner.vertexes.add(v);
                        }
                        inner.vertex_id++;
                        inner.vertex_count++;
                    }
                    else{
                        if("MxGraph".equals(inner.xml)){
                            inner.vertex_array.add(null);
                        }
                    }
                }
            }
            if(nList.getLength()>0){
                for (int i = 0; i < nList.getLength(); i++) {
                    if("1".equals(((Element) nList.item(i)).getAttribute("edge"))){ //vertex
                        if("MxGraph".equals(inner.xml)){
                            int source_v = Integer.parseInt(((Element) nList.item(i)).getAttribute("source"));
                            int target_v = Integer.parseInt(((Element) nList.item(i)).getAttribute("target"));
                            inner.graph.insertEdge(parent, null, "", inner.vertex_array.get(source_v), inner.vertex_array.get(target_v));
                        }else{
                            String source_v = ((Element) nList.item(i)).getAttribute("source");
                            String target_v = ((Element) nList.item(i)).getAttribute("target");
                            mxCell o_source = findInVertexList(inner.vertex_array,source_v);
                            mxCell o_target = findInVertexList(inner.vertex_array,target_v);
                            Object o = inner.graph.insertEdge(parent, null, "", o_source, o_target);
                            //break;
                        }
                        inner.edge_count++;
                    }
                }
            }
        }finally{
            inner.graph.getModel().endUpdate();
            inner.graph.getView().validate();
            inner.graphComponent.refresh();
        }
        
        Main.vertex_text.setText("Number of vertexes: " + inner.vertex_count);
        Main.edge_text.setText("Number of edges: " + inner.edge_count);
    }
    /**
     * Loops through vertex_array for specific array
     * @param vertex_array array of nodes
     * @param id of element we are looking for
     * @return mxCell to insert
     */
    public mxCell findInVertexList(ArrayList<Object> vertex_array, String id){
        mxCell cell = null;
        for(int i = 0; i < vertex_array.size();i++){
            cell = (mxCell)vertex_array.get(i);
            if(cell != null){
                String id_elem = cell.getId();
                if(id_elem.equals(id)){
                    break;
                }
            }
        }
        return cell;
    }
    public void deleteAll(InnerFrame inner){
        inner.graph.removeCells(inner.graph.getChildCells(inner.graph.getDefaultParent(), true, true));
        inner.vertex_id = 0;
        inner.vertex_count = 0;
        inner.edge_count = 0;
        inner.vertex_array.clear();
        inner.vertexes.clear();
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
            Main.action_performed.setText(Main.action_performed.getText()+"\n"+inner.soubor.getName()+" succesfully saved");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    public JPanel createTab(){
        JPanel panel = new JPanel();
        //JLabel aboutLabel = new JLabel();
        //aboutLabel.setHorizontalAlignment(JLabel.CENTER);
        //panel.add(aboutLabel);
        return panel;
    }
    
    
    public void graphMatrix(InnerFrame inner){
        InnerFrame frame = (InnerFrame) inner;
        
        inner.matrix = new int[(inner.vertexes.size())][(inner.vertexes.size())];
        //inicialize
        for(int i=0; i<inner.vertexes.size(); i++){
            for(int j=0; j<inner.vertexes.size(); j++){
                inner.matrix[i][j] = 0;
            }
        }
        
        
  
        inner.graph.selectAll();
        int edges_count = 0;
        Object[] cells = inner.graph.getSelectionCells();
        for(Object c:cells){
            mxCell cell = (mxCell) c;
            if(cell.isEdge()){
                edges_count++;
                int index_x = inner.getArrayIndex(cell.getSource().getId());
                int index_y = inner.getArrayIndex(cell.getTarget().getId());
                if(index_x != -1 && index_y != -1){
                    inner.matrix[index_x][index_y] = 1;
                    inner.matrix[index_y][index_x] = 1;
                }
            }
        }
        //debug
        for(int i =0; i<inner.vertexes.size(); i++){
            mxCell cell = (mxCell) inner.vertexes.get(i);
            System.out.print(cell.getId()+" ");
        }
        System.out.println();
        
        for(int i = 0; i<inner.vertexes.size(); i++){
            for(int j = 0; j<inner.vertexes.size(); j++){
                System.out.print(inner.matrix[i][j]+" ");
            }
            System.out.println();
        }
        
        System.out.println("edges_count: "+edges_count);
    }
    
    public void fillView(InnerFrame inner){
        //BufferedImage image;
        inner.image = mxCellRenderer.createBufferedImage(inner.graph, null, 1, Color.WHITE, true, null);
        BufferedImage resizedImage=resize(inner.image,300,300);
        inner.label = new JLabel(new ImageIcon(resizedImage));
        Main.viewPanel.add(inner.label);
        Main.f.revalidate();
        Main.f.repaint();
    }
    
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }
    
    public void removeView(InnerFrame inner){
        inner.image = null;
        Main.viewPanel.remove(inner.label);
        inner.label = null;
        Main.f.revalidate();
        Main.f.repaint();
    }
}
