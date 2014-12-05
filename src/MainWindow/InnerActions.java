/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Formaiko
 */
public class InnerActions {
    public InnerActions(MainFrame main,JDesktopPane inner){
        main.SlowDownButton = new JButton("SlowDown");
        Main.controlsPanel.add(main.SlowDownButton);
        main.SlowDownButton.setEnabled(false);
        main.SlowDownButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                if(inner.waitTime == 5){
                    Main.action_performed.setText(Main.action_performed.getText() + "\nSpeed is at minimum level");
                }
                else{
                    inner.waitTime++;
                    Main.action_performed.setText(Main.action_performed.getText() + "\nSpeed:" + inner.waitTime);//todo - upravit ty speed levely
                }
            }
        });
        
        main.StepBackButton = new JButton("StepBack");
        Main.controlsPanel.add(main.StepBackButton);
        main.StepBackButton.setEnabled(false);
        main.StepBackButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                oneStepBack();
            }
        });
        
        
        main.PlayButton = new JButton("Play");
        Main.controlsPanel.add(main.PlayButton);
        main.PlayButton.setEnabled(false);
        main.PlayButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                inner.pausePressed = false;
                if(inner.first != null){
                    System.out.println("neni prazdny");
                }
                
                //while(!inner.abortPressed){
                    //Main.controls.wait(inner.waitTime);
                    Main.controls.oneStepFwd((mxCell)inner.actualVert);
                //}
            }
        });
        
        
        main.PauseButton = new JButton("Pause");
        Main.controlsPanel.add(main.PauseButton);
        main.PauseButton.setEnabled(false);
        main.PauseButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                inner.pausePressed = true;
                Main.action_performed.setText(Main.action_performed.getText() + "\nPause pressed, algorithm will now pause");
                
            }
        });
        
        
        main.AbortButton = new JButton("Abort");
        Main.controlsPanel.add(main.AbortButton);
        main.AbortButton.setEnabled(false);
        main.AbortButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.action_performed.setText(Main.action_performed.getText() + "\nAbort Pressed");
            }
        });
        
        
        main.StepFwdButton = new JButton("Step forward");
        main.StepFwdButton.setEnabled(false);
        Main.controlsPanel.add(main.StepFwdButton);
        main.StepFwdButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //oneStepFwd();
            }
        });
        
        
        main.SpeedUpButton = new JButton("SpeedUp");
        Main.controlsPanel.add(main.SpeedUpButton);
        main.SpeedUpButton.setEnabled(false);
        main.SpeedUpButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                if(inner.waitTime == 1){
                    Main.action_performed.setText(Main.action_performed.getText() + "\nSpeed is at maximum level");
                }
                else{
                    inner.waitTime--;
                    Main.action_performed.setText(Main.action_performed.getText() + "\nSpeed:" + inner.waitTime);
                }
            }
        });
        
        /***** Reedit button *****/
        main.ReeditButton = new JButton("Reedit");
        Main.controlsPanel.add(main.ReeditButton);
        main.ReeditButton.setEnabled(false);
        main.ReeditButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                inner.clickable = false;
                inner.menu = true;
                
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
                
                
                inner.graph.getSelectionModel().clear();
                
                inner.graph.setCellsEditable(true);
                inner.graphComponent.setConnectable(true);
                inner.graph.setEnabled(true);
                inner.graphComponent.setEnabled(true);
                
            }
        });
    }

    /***** Fills dictionary vertexMap *****/
    public void fillVertexMap(InnerFrame inner){       
        inner.vertexMap = new HashMap();
        inner.edgeMap = new HashMap();
        
        for(int i = 0; i < inner.vertexes.size(); i++){
            mxCell vertex = (mxCell)inner.vertexes.get(i);
            String id = vertex.getId();
            inner.vertexMap.put(id, i);
        }

        for(Object key: inner.vertexMap.keySet()){
	    System.out.println(key + ": " + inner.vertexMap.get(key));
        }
//        for (Enumeration e = inner.vertexMap.keys(); e.hasMoreElements();) {
//            System.out.println(e.nextElement());
//        }
    }
    
//    
//    new Thread()
//{
//public void run() {
//krok vypoctu;
//sleep();
//}
//}.start();
    public void countDFS(mxGraph graph, mxCell vertex, InnerFrame inner){
        
    }
    
    public void wait (int n){
        long t0,t1;
        t0=System.currentTimeMillis();
        do{
            t1=System.currentTimeMillis();
        }
        while (t1-t0<1000);
    }
    
    
    
    /***** this function will perform one step through the graph *****/
    public void oneStepFwd(mxCell vertex){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        Main.action_performed.setText(Main.action_performed.getText()+"\nOneStepFwd");
        
        ArrayList<Object> edges = new ArrayList();
        mxCell edge = null;
        
        if(vertex.getEdgeCount() > 1) {
            Random rand = new Random();
            int n = rand.nextInt(vertex.getEdgeCount() - 1);
            
            edges.addAll(getEdges(vertex.getId()));// = getEdges(vertex.getId());
        }
        else if(vertex.getEdgeCount() == 1){
            System.out.println("jen jeden edge");
            edge = getEdge(vertex.getId());
            System.out.println(edge.getSource().getId());
            inner.graph.getModel().beginUpdate();
            try {
                System.out.println(edge.getSource().getId());
                inner.actualVert = edge.getTarget();
                inner.graph.getModel().remove( edge);
                
            } finally {
                inner.graph.getModel().endUpdate();
            }
        }
        
        
        
        /*Random rand = new Random();
        int  n = rand.nextInt(inner.vertexes.size()-1);
        Main.utils.countDFS(inner.graph, (mxCell)inner.vertexes.get(n), inner);*/
    }

    public ArrayList<Object> getEdges(String source){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        inner.graph.selectAll();
        Object[] edges = inner.graph.getSelectionCells();
        
        ArrayList ret = new ArrayList();
        
        for(Object c:edges){
            mxCell cell = (mxCell)c;
            if(cell.isEdge() && cell.getSource().getId() == source){
                ret.add(cell);
            }
        }
        
        inner.graph.getSelectionModel().clear();
        
        return ret;
    }
    
    public mxCell getEdge(String source){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        inner.graph.selectAll();
        Object[] edges = inner.graph.getSelectionCells();
        
        mxCell ret=null;
        
        System.out.println("pred for");
        for(Object c:edges){
            mxCell cell = (mxCell)c;
            if(cell.isEdge() && cell.getSource().getId().equals(source)){
                ret = cell;
                System.out.println("true");
            }
            System.out.println("pruchod");
        }
        
        inner.graph.getSelectionModel().clear();
        
        return ret;
    }
    
    /***** This function will perform one step backward *****/
    public void oneStepBack(){
        Main.action_performed.setText(Main.action_performed.getText()+"\nOneStepBack");
    }

    public void performAlg(){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        
        
        
    }
}
    
