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
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
                InnerFrame newFrame = new InnerFrame(500,500,++Main.f.innerFrameCount, Main.f);
                Main.utils.createComp(newFrame);
                Main.action_performed.setText(Main.action_performed.getText() + "\n" + "chart" + Main.f.innerFrameCount + " created");
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
                        Main.utils.saveFile(saveName, inner, fileName);
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
                    JOptionPane.showMessageDialog(frame, "A chart document must be selected to load a graph.");
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
                help.setSize(500, 500);
                
                JTabbedPane insideHelp = new JTabbedPane();
                
                JComponent authors = Main.utils.createTab();
                JComponent controls = Main.utils.createTab();
                JComponent about = Main.utils.createTab();
                
                JTextArea authorsText = new JTextArea();
                authorsText.setEditable(false);
                authorsText.setText("This program was created by\nDaniel Javorsky and Michal Ruprich");
                authors.add(authorsText);
                
                insideHelp.addTab("About", about);
                insideHelp.addTab("Controls", controls);
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
                    }
                    else if(vertexes==0){
                        JOptionPane.showMessageDialog(frame, "The graph is empty. Nothing to do here...");
                    }
                    else{
                        inner.clickable = true;
                        inner.menu = false;
                        
                        inner.parent.SlowDownButton.setEnabled(inner.clickable);
                        inner.parent.StepBackButton.setEnabled(inner.clickable);
                        inner.parent.PlayButton.setEnabled(inner.clickable);
                        inner.parent.PauseButton.setEnabled(inner.clickable);
                        inner.parent.AbortButton.setEnabled(inner.clickable);
                        inner.parent.StepFwdButton.setEnabled(inner.clickable);
                        inner.parent.SpeedUpButton.setEnabled(inner.clickable);
                        inner.parent.ReeditButton.setEnabled(inner.clickable);
                        
                        inner.parent.NewButton.setEnabled(inner.menu);
                        inner.parent.SaveButton.setEnabled(inner.menu);
                        inner.parent.LoadButton.setEnabled(inner.menu);
                        inner.parent.SaveAsImage.setEnabled(inner.menu);
                        inner.parent.DeleteButton.setEnabled(inner.menu);
                        inner.parent.StartButton.setEnabled(inner.menu);
                        
                        Main.utils.graphMatrix(inner);
                        Main.controls.fillVertexMap(inner);
                        
                        inner.graph.getSelectionModel().clear();
                        inner.graph.setCellsEditable(false);
                        inner.graph.setEnabled(false);
                        
                        inner.graphComponent.setConnectable(false);
                        inner.graphComponent.setEnabled(false);
                        inner.graphComponent.getGraphControl().removeMouseListener(null);
                        
                    }
                }
            }
        });
    }
}