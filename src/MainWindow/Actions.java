/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import MainWindow.MainFrame;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxUtils;
import java.awt.Color;
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
import javax.swing.JOptionPane;
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
            public void actionPerformed(ActionEvent e){
                
                InnerFrame newFrame = new InnerFrame(500,500,++frame.innerFrameCount, frame);
                Main.utils.createComp(newFrame);
                
                Main.action_performed.setText(Main.action_performed.getText() + "\n" + "chart" + frame.innerFrameCount + " created");
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
                
                inner.graph.getChildVertices(inner.graph.getDefaultParent());
                mxCodec codec = new mxCodec();
                inner.xml = mxUtils.getXml(codec.encode(inner.graph.getModel()));//getXml(codec.encode(inner.graph.getModel()));
                
                
                if(inner.notSaved){//this branch is for the first save - user needs to provide location for the file to be saved
                    Main.action_performed.setText(Main.action_performed.getText() + "\n" + "Choosing location to save your graph ;)");
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
                            Main.action_performed.setText(Main.action_performed.getText() + "\n" + saveName);
                            try{//saving file to its chosen location
                                PrintWriter out = new PrintWriter(saveName);
                                out.println(inner.xml);
                                out.close();
                            }catch (FileNotFoundException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            inner.notSaved = false;
                            inner.setTitle(fileName);
                        }
                        else{
                            Main.action_performed.setText(Main.action_performed.getText() + "\n" + "Extension needs to be .xml");
                        }
                    }
                }
                else{//File location has already been chosen, the file is just overwritten
                    try {
                        PrintWriter out = new PrintWriter(saveName);
                        //out.println(xml);
                        out.close();
                        Main.action_performed.setText(Main.action_performed.getText()+"\n"+fileName+" succesfully saved");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
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
                    JOptionPane.showMessageDialog(frame, "Inner chart document must be selected to load a graph.");
                }
                else{
                    try {
                        Main.utils.OpenFile(inner);
                    } catch (SAXException | IOException | ParserConfigurationException | TransformerException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        
                        Main.utils.setGraph(inner);
                        Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Graph loaded.");
                        inner.notSaved = false;
                    } catch (Exception ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        });
     
        
        /***** CRUSH button *****/
        frame.DeleteButton = new JButton("Crush Graph!");
        Main.buttonPanel.add(frame.DeleteButton);
        frame.DeleteButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                Main.utils.deleteAll(inner);
                Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Graph deleted.");
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
                
                BufferedImage image = mxCellRenderer.createBufferedImage(inner.graph, null, 1, Color.WHITE, true, null);
                String filename = inner.chartName;
                try {
                    ImageIO.write(image, "PNG", new File(".\\Image\\"+filename+".png"));
                    Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Image saved to " + filename + ".png");
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        /***** ORIENTED button *****/
        frame.OrientedButton = new JButton("Oriented edges");
        Main.buttonPanel.add(frame.OrientedButton);
        frame.OrientedButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
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
        });
        
        /***** ORIENTED button *****/
        frame.StartButton = new JButton("Start!!");
        Main.buttonPanel.add(frame.StartButton);
        frame.StartButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                inner.clickable = true;
                
                inner.parent.SlowDownButton.setEnabled(inner.clickable);
                inner.parent.StepBackButton.setEnabled(inner.clickable);
                inner.parent.PlayButton.setEnabled(inner.clickable);
                inner.parent.PauseButton.setEnabled(inner.clickable);
                inner.parent.AbortButton.setEnabled(inner.clickable);
                inner.parent.StepFwdButton.setEnabled(inner.clickable);
                inner.parent.SpeedUpButton.setEnabled(inner.clickable);
            }
        });
    }
    
}