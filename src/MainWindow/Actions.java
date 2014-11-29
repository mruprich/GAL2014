/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import MainWindow.MainFrame;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
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
                
                InnerFrame newFrame = new InnerFrame(500,500,++frame.innerFrameCount);
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
            boolean notSaved = true;
            String saveName = "";
            String fileName = "";
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                inner.graph.getChildVertices(inner.graph.getDefaultParent());
                mxCodec codec = new mxCodec();
                inner.xml = mxUtils.getXml(codec.encode(inner.graph.getModel()));//getXml(codec.encode(inner.graph.getModel()));
                
                
                if(notSaved){//this branch is for the first save - user needs to provide location for the file to be saved
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
                            notSaved = false;
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
                
                try {
                    Main.utils.OpenFile(inner);
                } catch (SAXException | IOException | ParserConfigurationException | TransformerException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    Main.utils.setGraph(inner);
                    Main.action_performed.setText(Main.action_performed.getText()+"\n"+"Graph loaded.");
                } catch (Exception ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
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
    }
    
}