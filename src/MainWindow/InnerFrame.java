/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import javax.swing.JInternalFrame;
import javax.swing.event.*;
import javax.swing.border.LineBorder;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;


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
    
    public int waitTime = 1;
    public boolean pausePressed = false;
    
    public ArrayList<Object> vertex_array;
    
    public mxGraphComponent graphComponent;
    public mxGraph graph;
    public mxGraphView view;
    public String xml;
    
    public boolean notSaved;
    
    public MainFrame parent;
    
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
        this.maximizable = true;
        this.title = "chart" + count;
        this.resizable = true;
        
        this.notSaved = true;
        this.chartName = this.title;
        
        this.vertex_array = new ArrayList<Object>();
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
        this.parent.SlowDownButton.setEnabled(this.clickable);
        this.parent.StepBackButton.setEnabled(this.clickable);
        this.parent.PlayButton.setEnabled(this.clickable);
        this.parent.PauseButton.setEnabled(this.clickable);
        this.parent.AbortButton.setEnabled(this.clickable);
        this.parent.StepFwdButton.setEnabled(this.clickable);
        this.parent.SpeedUpButton.setEnabled(this.clickable);
        this.parent.ReeditButton.setEnabled(this.clickable);
        
        this.parent.NewButton.setEnabled(this.menu);
        this.parent.SaveButton.setEnabled(this.menu);
        this.parent.LoadButton.setEnabled(this.menu);
        this.parent.SaveAsImage.setEnabled(this.menu);
        this.parent.DeleteButton.setEnabled(this.menu);
        this.parent.StartButton.setEnabled(this.menu);
        
        this.moveToFront();
    }

   

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        this.moveToBack();
    }
}
