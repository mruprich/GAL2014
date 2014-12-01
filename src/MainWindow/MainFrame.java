/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package MainWindow; 


import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.io.mxCodec;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.reader.mxGraphViewImageReader;
import com.mxgraph.reader.mxGraphViewReader;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUtils;


import com.mxgraph.view.mxStylesheet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import static java.awt.PageAttributes.ColorType.COLOR;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.awt.FlowLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author Dan
 */
public class MainFrame extends JFrame implements MouseListener,MouseWheelListener,KeyListener{
    
//    /*****  "global" variables  *****/
      public int innerFrameCount = 0;
      public int activeFrame = 0;
      
      public static JDesktopPane deskPanel;
      
      public ArrayList<Object> innerFrameArray;
      private ArrayList<Object> vertex_array = new ArrayList<Object>();
      
      /***** Buttons *****/
      public JButton NewButton;
      public JButton SaveButton;
      public JButton LoadButton;
      public JButton DeleteButton;
      
      /***** controls buttons *****/
      public JButton SlowDownButton;
      public JButton StepBackButton;
      public JButton PlayButton;
      public JButton PauseButton;
      public JButton AbortButton;
      public JButton StepFwdButton;
      public JButton SpeedUpButton;
      
//    public int vertex_id = 0;
//    public String xml;
//    File soubor = null;
//    public int vertex_num, edge_num = 0;
//    private boolean edge_style = false; //false = unoriented, true = oriented
//    
//    /*****   swing components *****/
//    public JTextArea vertex_text;
//    public JTextArea edge_text;
//    public mxGraphComponent graphComponent;
//    public mxGraph graph;
//    private JFrame f;
//    private JTextArea action_performed;
  
//    private ArrayList<Object> vertex_array = new ArrayList<Object>();
    
    
    public MainFrame() {
        innerFrameArray = new ArrayList<Object>();
        Main.utils = new Utils();
        
        /***** Main window *****/
        Main.f = new JFrame();
        Main.f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Main.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /********************************************************************
        * Main pannels
        ********************************************************************/
        
        /***** Pannel for graph creation - multidocument interface *****/
        Main.graphPanel = new JPanel();
        Main.graphPanel.setLayout(new BorderLayout());
        
        
        //add multidocument panel to center panel area
        Main.desktopPanel = new JDesktopPane();
        
        
        //InnerFrame initialFrame = new InnerFrame(500,500, ++innerFrameCount);
        //initialFrame.setInnerFrameBorder(new LineBorder(Color.CYAN));
        
        //innerFrameArray.add(initialFrame);
        //Main.utils.createComp(initialFrame);
        
        /***** Pannel for controls inside graph *****/
        Main.controlsPanel = new JPanel();
        Main.controlsPanel.setLayout(new BorderLayout());
        Main.controlsPanel.setPreferredSize(new Dimension(300, 70));
        Main.controlsPanel.setLayout(new GridLayout(1,7));
        
        
        
        /***** Pannel on the right side *****/
        Main.mainPanel = new JPanel();
        Main.mainPanel.setBorder(new LineBorder(Color.BLACK));
        Main.mainPanel.setLayout(new BorderLayout());
        Main.mainPanel.setPreferredSize(new Dimension(350,350));

                    
        /**********************************************************
         * Inside Pannels
         *********************************************************/
        /***** Pannel for displying info about graph *****/
        Main.infoPanel = new JPanel();
        Main.infoPanel.setLayout(new BoxLayout(Main.infoPanel, BoxLayout.Y_AXIS));
        Main.infoPanel.setPreferredSize(new Dimension(100, 270));
        
        /***** Panel for buttons *****/
        Main.buttonPanel = new JPanel();
        Main.buttonPanel.setLayout(new GridLayout(6,1));
        Main.buttonPanel.setPreferredSize(new Dimension(100, 70));
        
        /***** Panel for start button *****/
        Main.startPanel = new JPanel();
        Main.startPanel.setLayout(new BorderLayout());
        Main.startPanel.setPreferredSize(new Dimension(100, 70));
          
        Main.mainPanel.add(Main.infoPanel, BorderLayout.PAGE_START);
        Main.mainPanel.add(Main.buttonPanel, BorderLayout.CENTER);
        Main.mainPanel.add(Main.startPanel, BorderLayout.PAGE_END);
        

        /******************************************************
         * Frames in infoPanel
         ******************************************************/
        Dimension basicDim = new Dimension(Integer.MAX_VALUE, 20);
        
        /*****  Frame INFO  *****/
        Main.action_performed = new JTextArea(4,1);//JTextArea(Document doc, String text, int rows, int columns)
        //action_performed.setBorder(border);
        Main.action_performed.setSize(new Dimension(100,150));
        Main.action_performed.append("Chart inicialized");
        
        Main.scroll_area = new JScrollPane(Main.action_performed);
        
        
        /***** Vertex info *****/
        Main.vertex_text = new JTextArea();
        Main.vertex_text.setEditable(false);
        //vertex_text.setBorder(border);
        Main.vertex_text.setMaximumSize(basicDim);
        Main.vertex_text.append("Number of vertexes: 0");
        
        /***** Edge info *****/
        Main.edge_text = new JTextArea();
        Main.edge_text.setEditable(false);
        //edge_text.setBorder(border);
        Main.edge_text.setMaximumSize(basicDim);
        Main.edge_text.append("Number of edges: 0");
        
        
        /*************************************************
         * Buttons creation
         *************************************************/

//        /***** PLAY button *****/
//        JButton StartButton = new JButton("Start!!!!!!");
//        StartButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                System.out.println("play");
//            }
//        });
//        
//        
//        /***** BACK button *****/
//        JButton BackwardButton = new JButton("<-");
//        BackwardButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                System.out.println("-");
//                action_performed.setText(action_performed.getText()+"\n"+"One step backward.");
//            }
//        });
//        
//        
//        /***** FWD button *****/
//        JButton FurtherButton = new JButton("->");
//        FurtherButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                double s = graph.getView().getScale();
//                Rectangle rect = graphComponent.getGraphControl().getVisibleRect();
//                rect.translate(50, 100);
//                graphComponent.getGraphControl().scrollRectToVisible(rect, true);
//                action_performed.setText(action_performed.getText()+"\n"+"Forward step.");
//            }
//        });
//        
//        
//        /***** CRUSH button *****/
//        JButton DeleteButton = new JButton("Crush Graph!");
//        DeleteButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                deleteAll();
//                action_performed.setText(action_performed.getText()+"\n"+"Graph deleted.");
//            }
//        });
//        
//        
//        /***** SAVE as image button *****/
//        JButton ImageButton = new JButton("Save as image");
//        ImageButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);
//                String filename = chart_title.getText();
//                try {
//                    ImageIO.write(image, "PNG", new File(".\\Image\\"+filename+".png"));
//                    action_performed.setText(action_performed.getText()+"\n"+"Image saved to " + filename + ".png");
//                } catch (IOException ex) {
//                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        
//        
//        /***** ORIENTED button *****/
//        JButton OrientButton = new JButton("Oriented edges");
//        OrientButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                if(edge_style){
//                    applyEdgeDefaults();
//                    OrientButton.setText("Oriented edges");
//                    edge_style = false;
//                    action_performed.setText(action_performed.getText()+"\n"+"Edges arent oriented.");
//                }
//                else{
//                    applyEdgeDefaultsOriented();
//                    OrientButton.setText("Unoriented edges");
//                    action_performed.setText(action_performed.getText()+"\n"+"Edges are now oriented.");
//                    edge_style = true;
//                }
//            }
//        });
//        
//        
//        
//        /***** PlayButton *****/
//        JButton PlayButton = new JButton("LAUNCH!");
//        PlayButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"Algorithm is being launched!");
//            }
//        });
//        
//        
//        /***** PauseButton *****/
//        JButton PauseButton = new JButton("PAUSE!");
//        PauseButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"Algorithm was put on hold...");
//            }
//        });
//        
//        
//        /***** StopButton *****/
//        JButton StopButton = new JButton("ABORT!");
//        StopButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"Algorithm was aborted!");
//            }
//        });
//        
//        /***** StepBackButton *****/
//        JButton StepBackButton = new JButton("STEP BACK!");
////StepBackButton.setSize(new Dimension(100, 80));
////try {
////    Image img = ImageIO.read(getClass().getResource("pokus.png"));
////    StepBackButton.setIcon(new ImageIcon(img.getScaledInstance(StepBackButton.getWidth(), StepBackButton.getHeight(), 0)));
////} catch (IOException ex) {
////}
//        
//        StepBackButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"One small step back...");
//            }
//        });
//        
//        /***** StepFwdButton *****/
//        JButton StepFwdButton = new JButton("FORWARD!");
//        StepFwdButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"One small step forward...");
//            }
//        });
//        
//        /***** SpeedUpButton *****/
//        JButton SpeedUpButton = new JButton("FASTER!");
//        SpeedUpButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"Faster! Faster!");
//            }
//        });
//        
//        /***** SlowDownButton *****/
//        JButton SlowDownButton = new JButton("SLOWER!");
//        SlowDownButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"Not so fast dude!");
//            }
//        });
        
        
        
        
        /******************************************************
         * Adding buttons to their locations
         *****************************************************/
        /* infoPanel */
        Main.infoPanel.add(Main.scroll_area);
        Main.infoPanel.add(Main.vertex_text);
        Main.infoPanel.add(Main.edge_text);
        
        
        /*buttonsPanel */
        //Main.buttonPanel.add(NewButton);
//        buttonPanel.add(SaveButton);
//        buttonPanel.add(LoadButton);
//        buttonPanel.add(ImageButton);
//        buttonPanel.add(DeleteButton);
//        buttonPanel.add(FurtherButton);
//        buttonPanel.add(BackwardButton);
//        buttonPanel.add(OrientButton);
//        
//        /* startPanel */
//        startPanel.add(StartButton);
//        
//        /* controlsPanel */
//        controlsPanel.add(SlowDownButton);
//        controlsPanel.add(StpeBackButton);
//        controlsPanel.add(StopButton);
//        controlsPanel.add(PlayButton);
//        controlsPanel.add(PauseButton);
//        controlsPanel.add(StepFwdButton);
//        controlsPanel.add(SpeedUpButton);
//        
//        graph.getModel().beginUpdate();
//        try {
//            
//            
//        } finally {
//            graph.getModel().endUpdate();
//        }
//        
//// define layout
//        mxIGraphLayout layout = new mxHierarchicalLayout(graph);
//        
//// layout using morphing
//        graph.getModel().beginUpdate();
//        try {
//            layout.execute(graph.getDefaultParent());
//        } finally {
//            graph.getModel().endUpdate();
//// fitViewport();
//        }
        /* ADD COMPONENTS TO FRAME AT THE END */
        Main.graphPanel.add(BorderLayout.CENTER,Main.desktopPanel);
        Main.graphPanel.add(BorderLayout.PAGE_END, Main.controlsPanel);
        Main.f.getContentPane().add(BorderLayout.CENTER, Main.graphPanel);
        Main.f.getContentPane().add(BorderLayout.EAST, Main.mainPanel);
        /* END OF ADDING */
        Main.f.setVisible(true);
        /* REVALIDATE ALL JPANELS AND JFRAME AFTER INICIALIZATION */
        Main.graphPanel.revalidate();
        Main.graphPanel.repaint();
        Main.controlsPanel.revalidate();
        Main.controlsPanel.repaint();      
        Main.mainPanel.revalidate();
        Main.mainPanel.repaint();
        Main.f.revalidate();
        Main.f.repaint();
        /* END OF REVALIDATION */ 
    }
    
//    private void newInnerFrame(int width, int height, LineBorder border){
//        InnerFrame newFrame = new InnerFrame(width, height, border);
//        newFrame.setVisible(true);
//        
//        Main.desktopPanel.add(newFrame); 
//        try {
//            newFrame.setSelected(true);
//        } catch (java.beans.PropertyVetoException e) {}
//    }
    
    
    
    
    
//    /***** OpenFile funkce pro Load *****/
//    private void OpenFile() throws SAXException, IOException, ParserConfigurationException, TransformerException{
//        final JFileChooser fc = new JFileChooser();
//        int returnVal;
//        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
//        fc.addChoosableFileFilter(xmlfilter);
//        fc.setDialogTitle("Choose file");
//        returnVal = fc.showOpenDialog(null);
//        
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            Main.soubor = fc.getSelectedFile();
//            System.err.println("Opening: " + soubor.getName());//location of file
//        } else {
//            System.err.println("Otevirani zruseno uzivatelem.");
//            return;
//        }
//        XMLconvertor graphmlToMxgraph = new XMLconvertor(soubor);
//        xml = graphmlToMxgraph.convertLoaded(soubor);;
//    }
//    /* Unoriented edges */
//    private void applyEdgeDefaults() {
//// Settings for edges
//        Map<String, Object> edge = new HashMap<String, Object>();
//        edge.put(mxConstants.STYLE_ROUNDED, false);//TODO
//        edge.put(mxConstants.STYLE_ORTHOGONAL, false);
////edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");//TODO
//        edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
//        edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
//        edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
//        edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
//        edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");
//        
//        mxStylesheet edgeStyle = new mxStylesheet();
//        edgeStyle.setDefaultEdgeStyle(edge);
//        graph.setStylesheet(edgeStyle);
//    }
//    /* Oriented edges */
//    private void applyEdgeDefaultsOriented() {
//        Map<String, Object> edge = new HashMap<String, Object>();
//        edge.put(mxConstants.STYLE_ROUNDED, true);
//        edge.put(mxConstants.STYLE_ORTHOGONAL, false);
//        edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");
//        edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
//        edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
//        edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
//        edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
//        edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
//        edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");
//        
//        mxStylesheet edgeStyle = new mxStylesheet();
//        edgeStyle.setDefaultEdgeStyle(edge);
//        graph.setStylesheet(edgeStyle);
//    }
//    
//    public void deleteAll(){
//        graph.removeCells(graph.getChildCells(graph.getDefaultParent(), true, true));
//        vertex_id = 0;
//        vertex_num = 0;
//        edge_num = 0;
//        vertex_array.clear();
//        vertex_text.setText("Number of vertexes: " + vertex_num);
//        edge_text.setText("Number of edges: " + edge_num);
//    }
//    /* DOM BUILDER FOR STRING */
//    public static Document loadXMLFromString(String xml) throws Exception
//    {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        InputSource is = new InputSource(new StringReader(xml));
//        return builder.parse(is);
//    }
//    /* SETS GRAPH FROM XML */
//    public void setGraph() throws Exception{
//        deleteAll();
//        java.lang.Object parent = graph.getDefaultParent();
//        Document doc;
////graphml was loaded at first
//        System.out.println(xml);
//        if("MxGraph".equals(xml)){
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            doc = dBuilder.parse(soubor);//mxgraph load
//        }
//        else{
//            doc = loadXMLFromString(xml);
//        }
//        NodeList nList = doc.getElementsByTagName("mxCell");
//        NodeList nListGeo = doc.getElementsByTagName("mxGeometry");
//        String value = "";
//        int source_v, target_v = 0;
//        Double x_coord,y_coord,w,h = 0.0;
//        int begin = Integer.parseInt(((Element) nList.item(2)).getAttribute("id"));
//        System.out.println(begin);
//        if(nList.getLength()>0){
//            for (int i = 0; i < nList.getLength(); i++) {
//                if("1".equals(((Element) nList.item(i)).getAttribute("vertex"))){ //vertex
//                    value = ((Element) nList.item(i)).getAttribute("value");
//                    x_coord = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("x"));
//                    y_coord = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("y"));
//                    w = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("width"));
//                    h = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("height"));
//                    Object v = graph.insertVertex(parent, null, value, x_coord, y_coord, w, h);
//                    vertex_array.add(v);
//                    vertex_id++;
//                    vertex_num++;
//                }
//            }
//            System.out.println("Inserted");
//        }
//        
//        if(nList.getLength()>0){
//            for (int i = 0; i < nList.getLength(); i++) {
//                if("1".equals(((Element) nList.item(i)).getAttribute("edge"))){ //vertex
//                    source_v = Integer.parseInt(((Element) nList.item(i)).getAttribute("source"));
//                    target_v = Integer.parseInt(((Element) nList.item(i)).getAttribute("target"));
//                    if("MxGraph".equals(xml))
//                        graph.insertEdge(parent, null, "", vertex_array.get(source_v-begin), vertex_array.get(target_v-begin));
//                    else
//                        graph.insertEdge(parent, null, "", vertex_array.get(source_v), vertex_array.get(target_v));
//                    edge_num++;
//                }
//            }
//            System.out.println("Inserted edges");
//        }
//        vertex_text.setText("Number of vertexes: " + vertex_num);
//        edge_text.setText("Number of edges: " + edge_num);
//    }
    /* Create graphComponent */
//    public void createComp(){
//        graphComponent = new mxGraphComponent(graph);
////graphComponent.setSize(new Dimension(300, 300));
//        java.lang.Object parent = graph.getDefaultParent();
//        graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener(){
//            @Override
//            public void invoke(Object sender, mxEventObject evt)  {
//                edge_num++;
//                edge_text.setText("Number of edges: " + edge_num);
//                action_performed.setText(action_performed.getText()+"\n"+"Edge created.");
//            }
//        });
//        
//        graphComponent.getGraphControl().addMouseListener(new MouseAdapter(){
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                x = e.getX();
//                y = e.getY();
//                graph.insertVertex(parent, null, vertex_id, x-40, y-10, 80, 30);
//                vertex_id++;
//                vertex_num++;
//                vertex_text.setText("Number of vertexes: " + vertex_num);
//                action_performed.setText(action_performed.getText()+"\n"+"Vertex created.");
//            }
//        });
////f.getContentPane().add(BorderLayout.CENTER, graphComponent);
//        graphPanel.add(graphComponent, BorderLayout.CENTER);
//    }
    
    
    
    @Override
    public void mouseClicked(MouseEvent me) {
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {
    }
    
    @Override
    public void mouseEntered(MouseEvent me) {
    }
    
    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        System.out.println(mwe.getWheelRotation());
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void keyReleased(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}