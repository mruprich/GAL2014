/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package MainWindow;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author Formaiko
 */
public class Actions{
    public Actions(MainFrame frame){
        /***** NewButton - creates new document for new graph *****/
        frame.NewButton = new JButton("New chart");
        Main.buttonPanel.add(frame.NewButton);
        frame.NewButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                //Thread newThread = new Thread();
                //newThread.start();
                InnerFrame newFrame = new InnerFrame(500,500,++frame.innerFrameCount, frame);
                Main.utils.createComp(newFrame);
                Main.action_performed.setText(Main.action_performed.getText() + "\n" + "chart" + frame.innerFrameCount + " created");
                //Main.f.threads.add(newThread);
                //InnerFrame newFrame = new InnerFrame(500,500,++frame.innerFrameCount, frame);
                //Main.utils.createComp(newFrame);
                
                //Main.action_performed.setText(Main.action_performed.getText() + "\n" + "chart" + frame.innerFrameCount + " created");
            }
        });
        
        
        
        /***** SAVE button *****/
        frame.SaveButton = new JButton("Save");
        Main.buttonPanel.add(frame.SaveButton);
        frame.SaveButton.setBounds(900, 110, 200, 20);
        frame.SaveButton.addActionListener( new ActionListener()
        {
            
            String saveName = "";
            String fileName = "";
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                if(inner == null){
                    JOptionPane.showMessageDialog(frame, "A chart document must be selected to load a graph.");
                }
                else{
                    inner.graph.getChildVertices(inner.graph.getDefaultParent());
                    mxCodec codec = new mxCodec();
                    inner.xml = mxUtils.getXml(codec.encode(inner.graph.getModel()));//getXml(codec.encode(inner.graph.getModel()));
                    
                    
                    if(inner.notSaved){//this branch is for the first save - user needs to provide location for the file to be saved
                        Main.action_performed.setText(Main.action_performed.getText() + "\n" + "Choosing location to save your graph");
                        JFileChooser saveLoc = new JFileChooser();
                        FileNameExtensionFilter locFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");//only xml files will be used
                        saveLoc.setFileFilter(locFilter);
                        int retVal = saveLoc.showSaveDialog(frame);
                        
                        /***** Checking extension and getting absolute path to chosen file *****/
                        if(retVal == saveLoc.APPROVE_OPTION) {
                            saveName = saveLoc.getSelectedFile().getName();
                            if (saveName.endsWith(".xml")){
                                saveName = saveLoc.getSelectedFile().getAbsolutePath();
                                fileName = saveLoc.getSelectedFile().getName();
                                Main.action_performed.setText(Main.action_performed.getText() + "\nChart save to " + saveName);
                                try{//saving file to its chosen location
                                    PrintWriter out = new PrintWriter(saveName);
                                    out.println(inner.xml);
                                    out.close();
                                }catch (FileNotFoundException ex) {
                                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                inner.notSaved = false;
                                inner.setTitle(fileName);
                                inner.chartName = fileName;
                            }
                            else{
                                Main.action_performed.setText(Main.action_performed.getText() + "\n" + "Extension needs to be .xml");
                            }
                        }
                    }
                    else{//File location has already been chosen, the file is just overwritten
                        Main.utils.saveFile(inner.soubor.getAbsolutePath(), inner, fileName);
                    }
                }
            }
        });
        
        
        /***** LOAD button *****/
        frame.LoadButton = new JButton("Load");
        Main.buttonPanel.add(frame.LoadButton);
        frame.LoadButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                if(inner == null){
                    JOptionPane.showMessageDialog(frame, "A graph document must be selected to load a graph.");
                }
                else{
                    boolean ret = false;
                    try {
                        ret = Main.utils.OpenFile(inner);
                    } catch (SAXException | IOException | ParserConfigurationException | TransformerException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(ret){
                        try {
                            
                            Main.utils.setGraph(inner);
                            Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Graph loaded.");
                            inner.notSaved = false;
                        } catch (Exception ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
            }
        });
        
        
        /***** CRUSH button *****/
        frame.DeleteButton = new JButton("Erase graph");
        Main.buttonPanel.add(frame.DeleteButton);
        frame.DeleteButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                if(inner == null){
                    JOptionPane.showMessageDialog(frame, "A graph document must be selected to load a graph.");
                }
                else{
                    int ret = JOptionPane.showConfirmDialog(inner, "Do you really want to delete your graph?", "Erase", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(ret == JOptionPane.YES_OPTION){
                        Main.utils.deleteAll(inner);
                        Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Graph deleted.");
                    }
                }
            }
        });
        
        /***** SAVE as image button *****/
        frame.SaveAsImage = new JButton("Save as image");
        Main.buttonPanel.add(frame.SaveAsImage);
        frame.SaveAsImage.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                String saveName;
                String fileName;
                BufferedImage image;
                
                if(inner == null){
                    JOptionPane.showMessageDialog(frame, "A graph document must be selected to load a graph.");
                }
                else{
                    JFileChooser saveLoc = new JFileChooser();
                    FileNameExtensionFilter locFilter = new FileNameExtensionFilter("png files (*.png)", "png");//only png files will be used
                    saveLoc.setFileFilter(locFilter);
                    int retVal = saveLoc.showSaveDialog(frame);
                    
                    /***** Checking extension and getting absolute path to chosen file *****/
                    if(retVal == saveLoc.APPROVE_OPTION) {
                        saveName = saveLoc.getSelectedFile().getName();
                        if (saveName.endsWith(".png")){
                            saveName = saveLoc.getSelectedFile().getAbsolutePath();
                            fileName = saveLoc.getSelectedFile().getName();
                            
                            image = mxCellRenderer.createBufferedImage(inner.graph, null, 1, Color.WHITE, true, null);
                            //BufferedImage finalImage = new BufferedImage(image.getWidth()+50, image.getHeight()+50, image.getType());
                            //Main.action_performed.setText(Main.action_performed.getText()+"\n"+image.getWidth() + " " + image.getHeight());
                            try {
                                ImageIO.write(image, "PNG", new File(saveName));
                                Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Image saved to " + fileName);
                            } catch (IOException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //String filename = inner.chartName;
                        }
                        else{
                            JOptionPane.showMessageDialog(frame, "Graph needs to be save as .png image");
                        }
                    }
                }
            }
        });
        
        /***** ORIENTED button *****//*
        frame.OrientedButton = new JButton("Oriented edges");
        Main.buttonPanel.add(frame.OrientedButton);
        frame.OrientedButton.addActionListener( new ActionListener()
        {
        @Override
        public void actionPerformed(ActionEvent e)
        {
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        
        if(inner == null){
        JOptionPane.showMessageDialog(frame, "A graph document must be selected to load a graph.");
        }
        else{
        if(inner.edge_style){
        Main.utils.applyEdgeDefaults(inner);
        frame.OrientedButton.setText("Oriented edges");
        inner.edge_style = false;
        Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Edges arent oriented.");
        }
        else{
        Main.utils.applyEdgeDefaultsOriented(inner);
        frame.OrientedButton.setText("Unoriented edges");
        Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Edges are now oriented.");
        inner.edge_style = true;
        }
        }
        }
        });*/
        
        /***** Help button *****/
        frame.HelpButton = new JButton("Help");
        Main.buttonPanel.add(frame.HelpButton);
        frame.HelpButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame help = new JFrame();
                help.setTitle("Help");
                help.setSize(600, 500);
                
                JTabbedPane insideHelp = new JTabbedPane();
                
                //inner tabs
                JPanel authors = new JPanel();
                authors.setSize(help.getWidth(), help.getHeight());
                JPanel controls = new JPanel();
                controls.setSize(help.getWidth(), help.getHeight());
                JPanel about = new JPanel();
                about.setSize(help.getWidth(), help.getHeight());
                JPanel fleury = new JPanel();
                fleury.setSize(help.getWidth(), help.getHeight());
                
                //inner tabs content
                JLabel authorsText = new JLabel("<html><b>Authors of this program are:</b><br><br>"
                        + "<pre>     Bc. Daniel Javorsky - javorskydanniel@gmail.com<br><br>"
                        + "     Bc. Michal Ruprich - michalruprich@gmail.com</pre></html>", JLabel.LEFT);
                authorsText.setPreferredSize(new Dimension(authors.getWidth()-20,authors.getHeight()-50));
                authorsText.setVerticalAlignment(JLabel.TOP);
                
                
                JLabel controlsText = new JLabel("<html><pre><b>This guide shows how to operate this program:</b><br><br>"
                        + "The work desktop is a multidocument area. You can open multiple<br>"
                        + "windows and work on more than one graph. Every time you hit <br>"
                        + "one of the buttons on the right side, a graph window needs<br>"
                        + "to be selected, otherwise nothing will happen.<br><br>"
                        + "The graph can be created either by clicking inside <br>"
                        + "selected graph window, or you can load your own graph. <br>"
                        + "After you are satisfied with your graph, hit Start button.<br>"
                        + "If your graph doesn't fullfil conditions for Fleury algorithm<br>"
                        + "The program won't let you perform any algorithm operations. <br>"
                        + "For Fleury's conditions see Fleury tab in Help.<br><br>"
                        + "<b>Inner buttons description: </b><br><br>"
                        + "     Step Back - takes one step back through the graph<br>"
                        + "     Start Over - erases the graph and goes back to the start<br>"
                        + "     Step Forward - takes one step forward through the graph<br>"
                        + "     Re-edit - allows you to make changes in your graph<br>"
                        + "</pre></html>", JLabel.LEFT);
                controlsText.setPreferredSize(new Dimension(controls.getWidth()-20,controls.getHeight()-50));
                controlsText.setVerticalAlignment(JLabel.TOP);
                
                JLabel fleuryText = new JLabel("<html><pre><b>Fleury's algorithm in a nutshell:</b><br><br>"
                        + "<b>Euler tour</b><br>"
                        + "A trail that traverses every edge of a graph is called an Euler tour.<br>"
                        + "A tour of a connected graph is a closed walk that traverses each edge <br>"
                        + "of graph at least once. Euler tour is a walk that traverses each edge <br>"
                        + "<b>exactly</b> once. <br><br>"
                        + "<b>Fleury's algorithm</b><br>"
                        + "Fleury's algorithm is used for finding an Euler tour in a graph. <br>"
                        + "Before traversing any edge in a graph, algorithm makes sure that <br>"
                        + "a cut edge (let's call it a bridge) of untraced subgraph <br>"
                        + "is taken only when there is no other alternative. <br><br>"
                        + "Bridge<br>"
                        + "Bridge is an edge which when traversed will divide untraced<br>"
                        + "graph into two disconnected subgraphs.<br><br>"
                        + "When is an edge a bridge?<br>"
                        + "when the algorithm needs to decide whether en edge is a bridge <br>"
                        + "or not, it counts number of accesible vertexes in an untraced <br>"
                        + "subgraph with DFS. Then this edge is removed from the subgraph <br>"
                        + " and DFS count number of accessible vertexes again. If the first <br>"
                        + "count is a smaller number than the second, this edge is a bridge <br>"
                        + "and another edge needs to be selected for next step."
                        + "</pre></html>", JLabel.LEFT);
                fleuryText.setPreferredSize(new Dimension(fleury.getWidth()-20,fleury.getHeight()-50));
                fleuryText.setVerticalAlignment(JLabel.TOP);
                
                JLabel aboutText = new JLabel("<html><b>Program description:</b><br><br>"
                        + "<pre>This program was developed to show how Fleury's algorithm works.<br>"
                        + "Program can be used for graph creation as well as for editing<br>"
                        + "previously created graphs.<br><br>"
                        + "It is possible to save created graphs in xml format or save <br>"
                        + "them as an image in png format. "
                        + "The program is also capable<br>of loading graphs in graphml format. If there are<br>"
                        + "no coordinates provided for vertexes in graphml file<br>"
                        + "they will be generated randomly on the workspace and<br>"
                        + "you can edit their positions and save for later use.<br><br>"
                        + "The workspace is a multidocument area which means it is <br>"
                        + "possible to works on multiple graphs at the same time.<br><br>"
                        + "Enjoy!!!</pre></html>", JLabel.LEFT);
                aboutText.setPreferredSize(new Dimension(about.getWidth()-20,about.getHeight()-50));
                aboutText.setVerticalAlignment(JLabel.TOP);
                
                
                authors.add(authorsText);
                controls.add(controlsText);
                about.add(aboutText);
                fleury.add(fleuryText);
                
                insideHelp.addTab("About", about);
                insideHelp.addTab("Controls", controls);
                insideHelp.addTab("Fleury", fleury);
                insideHelp.addTab("Authors", authors);
                help.add(insideHelp);
                help.setVisible(true);
            }
        });
        
        /***** START button *****/
        frame.StartButton = new JButton("Start!!");
        Main.startPanel.add(frame.StartButton);
        frame.StartButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                if(inner == null){
                    JOptionPane.showMessageDialog(frame, "A graph document must be selected to load a graph.");
                }
                else{
                    inner.graph.selectAll();
                    
                    inner.pausePressed = false;
                    
                    Object[] cells = inner.graph.getSelectionCells();
                    
                    int odd_vertexes = 0;
                    int vertexes = 0;
                    
                    for(Object c: cells){
                        mxCell cell = (mxCell) c;
                        if(cell.isVertex()){
                            vertexes++;
                            if(cell.getEdgeCount()%2 == 1){
                                odd_vertexes++;
                                if (inner.first==null){
                                    inner.first = cell;
                                }
                                else if(inner.second==null){
                                    inner.second = cell;
                                }
                            }
                        }
                    }
                    
                    //check whether the algorithm is even possible
                    if(odd_vertexes != 0 && odd_vertexes != 2){
                        JOptionPane.showMessageDialog(frame, "The graph does not fulfill conditions for Fleury algorithm");
                        inner.first = null;
                        inner.second = null;
                        inner.graph.getSelectionModel().clear();
                        inner.actualVert = null;
                    }
                    else if(vertexes==0){
                        JOptionPane.showMessageDialog(frame, "The graph is empty. Nothing to do here...");
                    }
                    else{
                        //GET GRAPH COPY
                        inner.graphCopy = new mxGraph();
                        inner.graphCopy.addCells(inner.graph.cloneCells(inner.graph.getChildCells(inner.graph.getDefaultParent())));
                        /* KONTROLA ZE SE TO ZOBRAZILO SPRAVNE */
                        inner.graphCopy.getChildVertices(inner.graphCopy.getDefaultParent());
                        /*mxCodec codec = new mxCodec();
                        String check = mxUtils.getXml(codec.encode(inner.graphCopy.getModel()));//getXml(codec.encode(inner.graph.getModel()));
                        System.out.println(check);*/
                        /* END OF CHECK */
                        
                        if(inner.first != null){
                            inner.actualVert = (mxCell) inner.first;
                        }
                        else{
                            Random rand = new Random();
                            int n = rand.nextInt(inner.vertexes.size()-1);
                            inner.actualVert = (mxCell) inner.vertexes.get(n);
                        }
                        inner.finalSequence.add(inner.actualVert);
                        
                        inner.graph.getModel().beginUpdate();
                        inner.graph.getModel().setStyle(inner.actualVert, "fillColor=#80c280");
                        inner.graph.getModel().endUpdate();
                        
                        inner.parent.PlayButton.setEnabled(false);
                        
                        
                        inner.clickable = true;
                        inner.menu = false;
                        
                        //inner.parent.StepBackButton.setEnabled(inner.clickable);
                        inner.parent.AbortButton.setEnabled(inner.clickable);
                        inner.parent.StepFwdButton.setEnabled(inner.clickable);
                        inner.parent.ReeditButton.setEnabled(inner.clickable);
                        
                        inner.parent.NewButton.setEnabled(inner.menu);
                        inner.parent.SaveButton.setEnabled(inner.menu);
                        inner.parent.LoadButton.setEnabled(inner.menu);
                        inner.parent.SaveAsImage.setEnabled(inner.menu);
                        inner.parent.DeleteButton.setEnabled(inner.menu);
                        inner.parent.StartButton.setEnabled(inner.menu);
                        
                        Main.utils.fillView(inner);
                        inner.startPressed = true;
                        
                        Main.controls.fillVertexMap(inner);
                        
                        inner.graph.getSelectionModel().clear();
//                        inner.graph.setCellsEditable(false);
//                        inner.graph.setEnabled(false);
//                        inner.graphComponent.setConnectable(false);
                        inner.graph.setEnabled(false);
                        
//                        for(MouseListener ml: inner.graphComponent.getGraphControl().getMouseListeners()){
//                            inner.graphComponent.getGraphControl().removeMouseListener(ml);
//                        }
                        //inner.graphComponent.getGraphControl().removeMouseListener(null);
                        inner.MouseListenerIsActive = false;
                    }
                }
            }
        });
    }
}