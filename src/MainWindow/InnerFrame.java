/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import javax.swing.JInternalFrame;
import javax.swing.event.*;
import javax.swing.border.LineBorder;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.bind.Marshaller.Listener;


/**
 *
 * @author Formaiko
 */
public class InnerFrame extends JInternalFrame implements ActionListener,InternalFrameListener{
    public int vertex_count = 0;
    public int edge_count = 0;
    public int vertex_id = 0;
    public int x,y;
    public int innerFrameId;
    public File soubor;
    public String chartName;
    public boolean edge_style = false; //false = unoriented, true = oriented
    public boolean clickable = false;
    public boolean edited = false;
    public boolean menu = true;
    public Object first,second = null;
    public mxCell actualVert = null;
    int step = 0;
    public boolean startPressed = false;
    
    public int[][] matrix;
    public Map vertexMap;
    public Map pathMap;
    public LinkedHashMap walkthrough;
    
    public boolean bridgeFound = false;
    public mxCell bridge = null;
    
    public int waitTime = 1;
    public boolean pausePressed = false;
    public boolean abortPressed = false;
    
    public ArrayList<Object> vertex_array;
    public ArrayList<Object> vertex_array_copy;
    public ArrayList<Object> vertexes;//this is for actual vertexes only!!
    public ArrayList<Object> edges;
    public ArrayList<Object> edges_walk;
    
    public mxGraphComponent graphComponent;
    public mxGraph graph;
    public mxGraphView view;
    public String xml;
    
    public MouseListener compListener;
    public Listener graphListener;
    
    /***** These will be used by algorithm to delete original graph step by step *****/
    public mxGraphComponent graphComponentCopy;
    public mxGraph graphCopy;
    public mxGraphView viewCopy;
    
    Thread play;
    
    public ArrayList<Object> finalSequence;//final sequence of vertexes
    public ArrayList<String> printSequence;
    
    public boolean notSaved;
    
    public MainFrame parent;
    
    public BufferedImage image;
    public JLabel label;
    
    public InnerFrame(int count){  
        Main.desktopPanel.add(this); 
        try {
            this.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
        
        setDefaults(count);
        innerFrameId = count - 1;
        
        
        this.setVisible(true);
    }
    
    public InnerFrame(int width, int height, int count, MainFrame frame){
        Main.desktopPanel.add(this); 
        
        try {
            this.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
        
        this.setInnerFrameSize(width, height);
        setDefaults(count);
        engageGraph();
        innerFrameId = count;
        this.parent = frame;
        
        try{
            this.setMaximum(true);
        }
        catch (PropertyVetoException e) {
            // Vetoed by internalFrame
            // ... possibly add some handling for this case
        }
        this.setVisible(true);
    }
    
    //just some basic graph settings
    private void engageGraph(){
        graph = new mxGraph(){
            public boolean isCellMovable(Object cell)
            {
                return !getModel().isEdge(cell);
            }
        };
        
        view = graph.getView();
        Main.utils.createComp(this);
           
        mxIGraphLayout layout = new mxHierarchicalLayout(graph);
        graph.setCellsDeletable(true);
        
        graph.getModel().beginUpdate();
        try {
            layout.execute(graph.getDefaultParent());
        } finally {
            graph.getModel().endUpdate();
            // fitViewport();
        }
        
        
        Main.utils.applyEdgeDefaults(this);
    }
    
    private void setDefaults(int count){
        this.addInternalFrameListener(this);
        this.closable = true;
        
        this.title = "chart" + count;
        this.resizable = true;
        this.maximizable = true;
        
        this.notSaved = true;
        this.chartName = this.title;
        
        this.vertex_array = new ArrayList<Object>();
        this.vertex_array_copy = new ArrayList<Object>();
        this.vertexes = new ArrayList<Object>();
        this.edges_walk = new ArrayList<Object>();
        this.edges = new ArrayList<Object>();
        this.finalSequence = new ArrayList<Object>();
        this.printSequence = new ArrayList<String>();
        
        this.vertexMap = new HashMap();
        this.pathMap = new HashMap();
        this.walkthrough = new LinkedHashMap();
    }
    
    public void setInnerFrameSize(int width, int height){
        this.setSize(width, height);
    }
    
    public void setInnerFrameBorder(LineBorder border){
        this.setBorder(border);
    }
    
    
    
    
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        int ret = JOptionPane.showConfirmDialog(null, "All unsaved progress will be discarded!!\nDo you still wish to close this graph?", 
                "Close graph", JOptionPane.YES_NO_OPTION);
        
        if(ret == JOptionPane.YES_OPTION){
            Main.action_performed.setText(Main.action_performed.getText() + "\n"+this.chartName + " closed");
            dispose();
            this.parent.StepBackButton.setEnabled(false);
            this.parent.AbortButton.setEnabled(false);
            this.parent.StepFwdButton.setEnabled(false);
            this.parent.ReeditButton.setEnabled(false);
            
            this.parent.NewButton.setEnabled(true);
            this.parent.SaveButton.setEnabled(true);
            this.parent.LoadButton.setEnabled(true);
            this.parent.SaveAsImage.setEnabled(true);
            this.parent.DeleteButton.setEnabled(true);
            this.parent.StartButton.setEnabled(true);
        }
        else{
            setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        }
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        /*int ret = JOptionPane.showConfirmDialog(this, "All unsaved progress will be discarded!!\nDo you still wish to close this graph?", 
                "Close graph", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(ret == JOptionPane.YES_OPTION){
            //this.dispose();
        }*/
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        //Main.action_performed.setText(Main.action_performed.getText()+"\nframe activated");
        this.parent.StepBackButton.setEnabled(this.clickable);
        this.parent.AbortButton.setEnabled(this.clickable);
        this.parent.StepFwdButton.setEnabled(this.clickable);
        this.parent.ReeditButton.setEnabled(this.clickable);
        
        this.parent.NewButton.setEnabled(this.menu);
        this.parent.SaveButton.setEnabled(this.menu);
        this.parent.LoadButton.setEnabled(this.menu);
        this.parent.SaveAsImage.setEnabled(this.menu);
        this.parent.DeleteButton.setEnabled(this.menu);
        this.parent.StartButton.setEnabled(this.menu);
        
        Main.vertex_text.setText("Number of vertexes: "+this.vertex_count);
        Main.edge_text.setText("Number of edges: "+this.edge_count);
        
        if(this.startPressed){
            try {
                Main.utils.fillView(this);
            } catch (Exception ex) {
                Logger.getLogger(InnerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.moveToFront();
    }

   

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        this.moveToBack();
        if(this.startPressed){
            Main.utils.removeView(this);
        }
    }
    
    public int getArrayIndex(String id){
        for(int i=0; i<this.vertexes.size(); i++){
            mxCell vertex = (mxCell) this.vertexes.get(i);
            
            if(vertex.getId().equals(id)){
                return i;
            }
        }
        return -1;
    }
    
    public void getVertexMap(){
        
    }
}
