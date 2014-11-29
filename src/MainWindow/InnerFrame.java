/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import java.awt.Color;
import javax.swing.JInternalFrame;
import javax.swing.event.*;
import javax.swing.border.LineBorder;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JDesktopPane;


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
    
    public ArrayList<Object> vertex_array;
    
    public mxGraphComponent graphComponent;
    public mxGraph graph;
    public mxGraphView view;
    public String xml;
    
    public boolean notSaved;
    
    
    
    public InnerFrame(int count){  
        Main.desktopPanel.add(this); 
        try {
            this.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
        
        setDefaults(count);
        innerFrameId = count - 1;
        
        
        this.setVisible(true);
    }
    
    public InnerFrame(int width, int height, int count){
        
        
        Main.desktopPanel.add(this); 
        
        try {
            this.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
        
        this.setInnerFrameSize(width, height);
        setDefaults(count);
        engageGraph();
        innerFrameId = count - 1;
        
        this.setVisible(true);
    }
    
    //just some basic graph settings
    private void engageGraph(){
        graph = new mxGraph();
        view = graph.getView();
        Main.utils.createComp(this);
           
        mxIGraphLayout layout = new mxHierarchicalLayout(graph);
        
        graph.getModel().beginUpdate();
        try {
            layout.execute(graph.getDefaultParent());
        } finally {
            graph.getModel().endUpdate();
            // fitViewport();
        }
    }
    
    private void setDefaults(int count){
        this.closable = true;
        this.maximizable = true;
        this.title = "chart" + count;
        this.resizable = true;
        
        this.notSaved = true;
        
        this.vertex_array = new ArrayList<Object>();
    }
    
    public void setInnerFrameSize(int width, int height){
        this.setSize(width, height);
    }
    
    public void setInnerFrameBorder(LineBorder border){
        this.setBorder(border);
    }
    
    public mxGraph getMxGraph(){
        return this.graph;
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        Main.action_performed.setText(Main.action_performed.getText()+"\n"+"novy graaaaaaaaf");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        Main.action_performed.setText(Main.action_performed.getText()+"\n"+"zaviraaam graaaaaaaaf");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        this.setBackground(Color.YELLOW);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
