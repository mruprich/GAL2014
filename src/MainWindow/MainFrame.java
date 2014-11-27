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
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
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

/**
 *
 * @author Dan
 */
public class MainFrame implements MouseListener,MouseWheelListener,KeyListener{
    
    /*****  "global" variables  *****/
    public int x,y;
    public int vertex_id = 0;
    public String xml;
    File soubor = null;
    public int vertex_num, edge_num = 0;
    private boolean edge_style = false; //false = unoriented, true = oriented
    
    /*****   swing components *****/
    public JTextArea vertex_text;
    public JTextArea edge_text;
    public mxGraphComponent graphComponent;
    public mxGraph graph;
    private JFrame f;
    private JTextArea action_performed;
    private JScrollPane scroll_area;
    private JTextArea chart_title;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel graphPanel;
    private JPanel controlsPanel;
    private JPanel buttonPanel;
    private JPanel startPanel;
    
    
    private ArrayList<Object> vertex_array = new ArrayList<Object>();
    
    
    public MainFrame() {
        
        /***** Main window *****/
        f = new JFrame();
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addMouseWheelListener(this);
        //f.setLocation(300, 200); //neni potreba vzhledem k tomu ze pouzivame MAXIMIZED_BOTH
        
        
        /**********************************************************
         * Inside Pannels
         *********************************************************/
        
        /***** Pannel for controls inside graph *****/
        controlsPanel = new JPanel();
        //controlsPanel.setBorder(new LineBorder(Color.YELLOW));
        controlsPanel.setLayout(new GridLayout(1, 7));
        controlsPanel.setPreferredSize(new Dimension(300, 70));
        
        /***** Pannel for displying info about graph *****/
        infoPanel = new JPanel();
        //infoPanel.setBorder(new LineBorder(Color.CYAN));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(100, 270));
        
        /***** Panel for buttons *****/
        buttonPanel = new JPanel();
        //buttonPanel.setBorder(new LineBorder(Color.BLACK));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(100, 70));
        
        /***** Panel for start button *****/
        startPanel = new JPanel();
        //startPanel.setBorder(new LineBorder(Color.ORANGE));
        startPanel.setLayout(new BorderLayout());
        startPanel.setPreferredSize(new Dimension(100, 70));
        
        /********************************************************************
         * Main pannels
         ********************************************************************/
        
        /***** Pannel for graph creation *****/
        graphPanel = new JPanel();
        //graphPanel.setBorder(new LineBorder(Color.RED));
        graphPanel.setLayout(new BorderLayout());
        graphPanel.add(controlsPanel, BorderLayout.PAGE_END);
        f.getContentPane().add(BorderLayout.CENTER, graphPanel);
        
        /***** Pannel on the right side *****/
        mainPanel = new JPanel();
        //mainPanel.setBorder(new LineBorder(Color.BLACK));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(350,350));
        f.getContentPane().add(BorderLayout.EAST, mainPanel);
        
        mainPanel.add(infoPanel, BorderLayout.PAGE_START);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(startPanel, BorderLayout.PAGE_END);
        
        
        Random rand = new Random();
        Border border = BorderFactory.createLineBorder(Color.RED); //ramecek u stats vpravo nahore
        
        
        /******************************************************
         * Frames in infoPanel
         ******************************************************/
        Dimension basicDim = new Dimension(Integer.MAX_VALUE, 20);
        
        /*****  Frame NAME  *****/
        chart_title = new JTextArea(); //nazev vprevo nahore
        //chart_title.setBorder(border);
        chart_title.setMaximumSize(basicDim);
        chart_title.append("Chart" + rand.nextInt(100-1));
        
        /*****  Frame INFO  *****/
        action_performed = new JTextArea(4,1);//JTextArea(Document doc, String text, int rows, int columns)
        //action_performed.setBorder(border);
        action_performed.setSize(new Dimension(100,150));
        action_performed.append("Chart inicialized");
        
        scroll_area = new JScrollPane(action_performed);
        
        
        
        /***** Vertex info *****/
        vertex_text = new JTextArea();
        vertex_text.setEditable(false);
        //vertex_text.setBorder(border);
        vertex_text.setMaximumSize(basicDim);
        vertex_text.append("Number of vertexes: " + vertex_num);
        
        /***** Edge info *****/
        edge_text = new JTextArea();
        edge_text.setEditable(false);
        //edge_text.setBorder(border);
        edge_text.setMaximumSize(basicDim);
        edge_text.append("Number of edges: " + edge_num);
        
        
        /***** Graph area *****/
        graph = new mxGraph();
        applyEdgeDefaults();
        mxGraphView view = graph.getView();
        createComp();
        
        
        
        
        
        /*************************************************
         * Buttons creation
         *************************************************/
        
        /***** SAVE button *****/
        JButton SaveButton = new JButton("Save");
        
        SaveButton.setBounds(900, 110, 200, 20);
        SaveButton.addActionListener( new ActionListener()
        {
            boolean notSaved = true;
            String saveName = "";
            String fileName = "";
            public void actionPerformed(ActionEvent e)
            {
                graph.getChildVertices(graph.getDefaultParent());
                mxCodec codec = new mxCodec();
                xml = mxUtils.getXml(codec.encode(graph.getModel()));
                
                
                if(notSaved){//this branch is for the first save - user needs to provide location for the file to be saved
                    action_performed.setText(action_performed.getText() + "\n" + "Choosing location to save your graph ;)");
                    JFileChooser saveLoc = new JFileChooser();
                    FileNameExtensionFilter locFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");//only xml files will be used
                    saveLoc.setFileFilter(locFilter);
                    int retVal = saveLoc.showSaveDialog(f);
                    
                    /***** Checking extension and getting absolute path to chosen file *****/
                    if(retVal == saveLoc.APPROVE_OPTION) {
                        saveName = saveLoc.getSelectedFile().getName();
                        if (saveName.endsWith(".xml")){
                            saveName = saveLoc.getSelectedFile().getAbsolutePath();
                            fileName = saveLoc.getSelectedFile().getName();
                            action_performed.setText(action_performed.getText() + "\n" + saveName);
                            try{//saving file to its chosen location
                                PrintWriter out = new PrintWriter(saveName);
                                out.println(xml);
                                out.close();
                            }catch (FileNotFoundException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            notSaved = false;
                        }
                        else{
                            action_performed.setText(action_performed.getText() + "\n" + "Extension needs to be .xml");
                        }
                    }
                }
                else{//File location has already been chosen, the file is just overwritten
                    try {
                        PrintWriter out = new PrintWriter(saveName);
                        out.println(xml);
                        out.close();
                        action_performed.setText(action_performed.getText()+"\n"+fileName+" succesfully saved");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
                
                
        /***** LOAD button *****/
        JButton LoadButton = new JButton("Load");
        LoadButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try {
                    OpenFile();
                } catch (SAXException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    setGraph();
                    action_performed.setText(action_performed.getText()+"\n"+"Graph loaded.");
                } catch (Exception ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });


        /***** PLAY button *****/
        JButton StartButton = new JButton("Start!!!!!!");
        StartButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("play");
            }
        });
        
        
        /***** BACK button *****/
        JButton BackwardButton = new JButton("<-");
        BackwardButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("-");
                action_performed.setText(action_performed.getText()+"\n"+"One step backward.");
            }
        });
        
        
        /***** FWD button *****/
        JButton FurtherButton = new JButton("->");
        FurtherButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                double s = graph.getView().getScale();
                Rectangle rect = graphComponent.getGraphControl().getVisibleRect();
                rect.translate(50, 100);
                graphComponent.getGraphControl().scrollRectToVisible(rect, true);
                action_performed.setText(action_performed.getText()+"\n"+"Forward step.");
            }
        });
        
        
        /***** CRUSH button *****/
        JButton DeleteButton = new JButton("Crush Graph!");
        DeleteButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                deleteAll();
                action_performed.setText(action_performed.getText()+"\n"+"Graph deleted.");
            }
        });
        
        
        /***** SAVE as image button *****/
        JButton ImageButton = new JButton("Save as image");
        ImageButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);
                String filename = chart_title.getText();
                try {
                    ImageIO.write(image, "PNG", new File(".\\Image\\"+filename+".png"));
                    action_performed.setText(action_performed.getText()+"\n"+"Image saved to " + filename + ".png");
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        /***** ORIENTED button *****/
        JButton OrientButton = new JButton("Oriented edges");
        OrientButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(edge_style){
                    applyEdgeDefaults();
                    OrientButton.setText("Oriented edges");
                    edge_style = false;
                    action_performed.setText(action_performed.getText()+"\n"+"Edges arent oriented.");
                }
                else{
                    applyEdgeDefaultsOriented();
                    OrientButton.setText("Unoriented edges");
                    action_performed.setText(action_performed.getText()+"\n"+"Edges are now oriented.");
                    edge_style = true;
                }
            }
        });
        
        
        
        /***** PlayButton *****/
        JButton PlayButton = new JButton("LAUNCH!");
        PlayButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                action_performed.setText(action_performed.getText()+"\n"+"Algorithm is being launched!");
            }
        });
        
        
        /***** PauseButton *****/
        JButton PauseButton = new JButton("PAUSE!");
        PauseButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                action_performed.setText(action_performed.getText()+"\n"+"Algorithm was put on hold...");
            }
        });
        
        
        /***** StopButton *****/
        JButton StopButton = new JButton("ABORT!");
        StopButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                action_performed.setText(action_performed.getText()+"\n"+"Algorithm was aborted!");
            }
        });
        
        /***** StepBackButton *****/
        JButton StepBackButton = new JButton("STEP BACK!");
//StepBackButton.setSize(new Dimension(100, 80));
//try {
//    Image img = ImageIO.read(getClass().getResource("pokus.png"));
//    StepBackButton.setIcon(new ImageIcon(img.getScaledInstance(StepBackButton.getWidth(), StepBackButton.getHeight(), 0)));
//} catch (IOException ex) {
//}
        
        StepBackButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                action_performed.setText(action_performed.getText()+"\n"+"One small step back...");
            }
        });
        
        /***** StepFwdButton *****/
        JButton StepFwdButton = new JButton("FORWARD!");
        StepFwdButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                action_performed.setText(action_performed.getText()+"\n"+"One small step forward...");
            }
        });
        
        /***** SpeedUpButton *****/
        JButton SpeedUpButton = new JButton("FASTER!");
        SpeedUpButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                action_performed.setText(action_performed.getText()+"\n"+"Faster! Faster!");
            }
        });
        
        /***** SlowDownButton *****/
        JButton SlowDownButton = new JButton("SLOWER!");
        SlowDownButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                action_performed.setText(action_performed.getText()+"\n"+"Not so fast dude!");
            }
        });
        
        
        
        
        /******************************************************
         * Adding buttons to their locations
         *****************************************************/
        /* infoPanel */
        infoPanel.add(chart_title);
        infoPanel.add(scroll_area);
        infoPanel.add(vertex_text);
        infoPanel.add(edge_text);
        
        
        /*buttonsPanel */
        buttonPanel.add(SaveButton);
        buttonPanel.add(LoadButton);
        buttonPanel.add(ImageButton);
        buttonPanel.add(DeleteButton);
        buttonPanel.add(FurtherButton);
        buttonPanel.add(BackwardButton);
        buttonPanel.add(OrientButton);
        
        /* startPanel */
        startPanel.add(StartButton);
        
        /* controlsPanel */
        controlsPanel.add(SlowDownButton);
        controlsPanel.add(StepBackButton);
        controlsPanel.add(StopButton);
        controlsPanel.add(PlayButton);
        controlsPanel.add(PauseButton);
        controlsPanel.add(StepFwdButton);
        controlsPanel.add(SpeedUpButton);
        
        graph.getModel().beginUpdate();
        try {
            
            
        } finally {
            graph.getModel().endUpdate();
        }
        
// define layout
        mxIGraphLayout layout = new mxHierarchicalLayout(graph);
        
// layout using morphing
        graph.getModel().beginUpdate();
        try {
            layout.execute(graph.getDefaultParent());
        } finally {
            graph.getModel().endUpdate();
// fitViewport();
        }
        f.setVisible(true);
        
    }
    
    
    
    /***** OpenFile funkce pro Load *****/
    private void OpenFile() throws SAXException, IOException, ParserConfigurationException, TransformerException{
        final JFileChooser fc = new JFileChooser();
        int returnVal;
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fc.addChoosableFileFilter(xmlfilter);
        fc.setDialogTitle("Choose file");
        returnVal = fc.showOpenDialog(null);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            soubor = fc.getSelectedFile();
            System.err.println("Opening: " + soubor.getName());//location of file
        } else {
            System.err.println("Otevirani zruseno uzivatelem.");
            return;
        }
        XMLconvertor graphmlToMxgraph = new XMLconvertor(soubor);
        xml = graphmlToMxgraph.convertLoaded(soubor);;
    }
    /* Unoriented edges */
    private void applyEdgeDefaults() {
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
        graph.setStylesheet(edgeStyle);
    }
    /* Oriented edges */
    private void applyEdgeDefaultsOriented() {
        Map<String, Object> edge = new HashMap<String, Object>();
        edge.put(mxConstants.STYLE_ROUNDED, true);
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
        graph.setStylesheet(edgeStyle);
    }
    
    public void deleteAll(){
        graph.removeCells(graph.getChildCells(graph.getDefaultParent(), true, true));
        vertex_id = 0;
        vertex_num = 0;
        edge_num = 0;
        vertex_array.clear();
        vertex_text.setText("Number of vertexes: " + vertex_num);
        edge_text.setText("Number of edges: " + edge_num);
    }
    /* DOM BUILDER FOR STRING */
    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
    /* SETS GRAPH FROM XML */
    public void setGraph() throws Exception{
        deleteAll();
        java.lang.Object parent = graph.getDefaultParent();
        Document doc;
//graphml was loaded at first
        System.out.println(xml);
        if("MxGraph".equals(xml)){
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(soubor);//mxgraph load
        }
        else{
            doc = loadXMLFromString(xml);
        }
        NodeList nList = doc.getElementsByTagName("mxCell");
        NodeList nListGeo = doc.getElementsByTagName("mxGeometry");
        String value = "";
        int source_v, target_v = 0;
        Double x_coord,y_coord,w,h = 0.0;
        int begin = Integer.parseInt(((Element) nList.item(2)).getAttribute("id"));
        System.out.println(begin);
        if(nList.getLength()>0){
            for (int i = 0; i < nList.getLength(); i++) {
                if("1".equals(((Element) nList.item(i)).getAttribute("vertex"))){ //vertex
                    value = ((Element) nList.item(i)).getAttribute("value");
                    x_coord = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("x"));
                    y_coord = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("y"));
                    w = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("width"));
                    h = Double.parseDouble(((Element) nListGeo.item(i-2)).getAttribute("height"));
                    Object v = graph.insertVertex(parent, null, value, x_coord, y_coord, w, h);
                    vertex_array.add(v);
                    vertex_id++;
                    vertex_num++;
                }
            }
            System.out.println("Inserted");
        }
        
        if(nList.getLength()>0){
            for (int i = 0; i < nList.getLength(); i++) {
                if("1".equals(((Element) nList.item(i)).getAttribute("edge"))){ //vertex
                    source_v = Integer.parseInt(((Element) nList.item(i)).getAttribute("source"));
                    target_v = Integer.parseInt(((Element) nList.item(i)).getAttribute("target"));
                    if("MxGraph".equals(xml))
                        graph.insertEdge(parent, null, "", vertex_array.get(source_v-begin), vertex_array.get(target_v-begin));
                    else
                        graph.insertEdge(parent, null, "", vertex_array.get(source_v), vertex_array.get(target_v));
                    edge_num++;
                }
            }
            System.out.println("Inserted edges");
        }
        vertex_text.setText("Number of vertexes: " + vertex_num);
        edge_text.setText("Number of edges: " + edge_num);
    }
    /* Create graphComponent */
    public void createComp(){
        graphComponent = new mxGraphComponent(graph);
//graphComponent.setSize(new Dimension(300, 300));
        java.lang.Object parent = graph.getDefaultParent();
        graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener(){
            @Override
            public void invoke(Object sender, mxEventObject evt)  {
                edge_num++;
                edge_text.setText("Number of edges: " + edge_num);
                action_performed.setText(action_performed.getText()+"\n"+"Edge created.");
            }
        });
        
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                graph.insertVertex(parent, null, vertex_id, x-40, y-10, 80, 30);
                vertex_id++;
                vertex_num++;
                vertex_text.setText("Number of vertexes: " + vertex_num);
                action_performed.setText(action_performed.getText()+"\n"+"Vertex created.");
            }
        });
//f.getContentPane().add(BorderLayout.CENTER, graphComponent);
        graphPanel.add(graphComponent, BorderLayout.CENTER);
    }
    
    public static void main(String[] args) {
        MainFrame f = new MainFrame();
        
    }
    
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
