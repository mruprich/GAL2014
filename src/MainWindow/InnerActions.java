/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package MainWindow;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
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
        //Main.controlsPanel.add(main.SlowDownButton);
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
       // Main.controlsPanel.add(main.PlayButton);
        main.PlayButton.setEnabled(false);
        main.PlayButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
                
                inner.pausePressed = false;
                
                if(inner.first != null){
                    inner.actualVert = (mxCell) inner.first;
                }                   
                else{
                    Random rand = new Random();
                    int n = rand.nextInt(inner.vertexes.size()-1);
                    inner.actualVert = (mxCell) inner.vertexes.get(n);
                }
                
                inner.parent.PlayButton.setEnabled(false);
            }
        });
        
        
        main.PauseButton = new JButton("Pause");
        //Main.controlsPanel.add(main.PauseButton);
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
        
        
        main.AbortButton = new JButton("Start Over");
        Main.controlsPanel.add(main.AbortButton);
        main.AbortButton.setEnabled(false);
        main.AbortButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
               startOver();
               if(!inner.parent.StepFwdButton.isEnabled())
                   inner.parent.StepFwdButton.setEnabled(true);
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
                oneStepFwd();
            }
        });
        
        
        main.SpeedUpButton = new JButton("SpeedUp");
        //Main.controlsPanel.add(main.SpeedUpButton);
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
        main.ReeditButton = new JButton("Re-edit");
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
                
                startOver();
                inner.graph.getModel().beginUpdate();
                inner.graph.getModel().setStyle(inner.actualVert, "fillColor=none");
                inner.graph.getModel().endUpdate();
                
                
                inner.graph.getSelectionModel().clear();
                
                Main.utils.removeView(inner);
                inner.startPressed=false;
                
                //inner.graphComponent.getGraphControl().addMouseListener(inner.compListener);
                
            }
        });
    }
    
    public void startOver(){
        Main.action_performed.setText(Main.action_performed.getText() + "\nThe graph was reset");
        
               InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
               inner.parent.StepBackButton.setEnabled(false);
               
               
               Random rand = new Random();
               int n = rand.nextInt(inner.vertexes.size()-1);
               
               //inner.actualVert = (mxCell) inner.vertexes.get(n);
               
                while(inner.edges_walk.size()>0){
                    inner.graph.getModel().beginUpdate();
                    try {
                        java.lang.Object parent = inner.graph.getDefaultParent();
                        mxCell cell = (mxCell)inner.edges_walk.get(inner.edges_walk.size()-1);
                        inner.edges_walk.remove(inner.edges_walk.size()-1);
                        String id = cell.getId();
                        Object target = cell.getTarget();
                        Object source = cell.getSource();
                        inner.graph.insertEdge(parent, id, "", source, target);

                        inner.graph.getModel().setStyle(inner.actualVert, "fillColor=none");
                        if(inner.actualVert == (mxCell)cell.getSource())
                            inner.actualVert = (mxCell)cell.getTarget();
                        else
                             inner.actualVert = (mxCell)cell.getSource();
                        inner.graph.getModel().setStyle(inner.actualVert, "fillColor=#80c280");
                        }
                    finally{
                        inner.graph.getModel().endUpdate();
                    }
                }
                inner.edges_walk.clear();
                
                
                
    }
    /***** Fills dictionary vertexMap - in the map - "vertex_id" -> vertex_position_in_matrix *****/
    public void fillVertexMap(InnerFrame inner){
        for(int i = 0; i < inner.vertexes.size(); i++){
            mxCell vertex = (mxCell)inner.vertexes.get(i);
            String id = vertex.getId();
            inner.vertexMap.put(id, i);
        }
    }
    
//
//    new Thread()
//{
//public void run() {
//krok vypoctu;
//sleep();
//}
//}.start();

    
    public void wait (int n){
        long t0,t1;
        t0=System.currentTimeMillis();
        do{
            t1=System.currentTimeMillis();
        }
        while (t1-t0<1000);
    }
    
    
    
    /***** this function will perform one step through the graph *****/
    public void oneStepFwd(){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        
        mxCell edge = getEdge((mxCell)inner.actualVert);
        
        if(edge != null){
        
            inner.graph.getModel().beginUpdate();
            try {
                inner.graph.getModel().setStyle(inner.actualVert, "fillColor=none");
                
                if(inner.actualVert.getId().equals(edge.getTarget().getId())){
                    inner.actualVert = (mxCell)edge.getSource();
                    inner.walkthrough.put(inner.actualVert.getId(), edge.getSource().getId());
                }
                else{
                    inner.actualVert = (mxCell)edge.getTarget();
                    inner.walkthrough.put(inner.actualVert.getId(), edge.getTarget().getId());
                }     
                inner.graph.getModel().setStyle(inner.actualVert, "fillColor=#80c280");
                
                inner.step++;
                inner.finalSequence.add(inner.actualVert);
                //inner.printSequence.add((String) inner.actualVert.getValue());
                
                /* insert edge into array for walkthrough */ 
                inner.edges_walk.add(edge);
                
                
                inner.graph.getModel().remove(edge);
            } finally {
                inner.graph.getModel().endUpdate();
                inner.graph.refresh();
                inner.graphComponent.refresh();
                if(!inner.parent.StepBackButton.isEnabled()){
                    inner.parent.StepBackButton.setEnabled(true);
                }
            }
        }
        else{
            inner.parent.StepFwdButton.setEnabled(false);
            printFinal(inner);
        }
    }
    
    
    public mxCell getEdge(mxCell vertex){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        inner.graph.selectAll();
        
        Object[] cells = inner.graph.getSelectionCells();
        ArrayList found = new ArrayList();
        mxCell edge = null;
        
        for(Object c:cells){
            mxCell cell = (mxCell)c;
            if(cell.isEdge() && (cell.getSource().getId().equals(vertex.getId()) || cell.getTarget().getId().equals(vertex.getId()))){
                found.add(cell);
            }
        }
        
        inner.graph.getSelectionModel().clear();
        
        //ArrayList<Object> edges;
        if(vertex.getEdgeCount() > 1) {
            //edges = new ArrayList();
            
            Random rand = new Random();
            int n = rand.nextInt(found.size()-1);
            edge = (mxCell) found.get(n);
            //uchovam si source a target hrany kterou zkoumam
            Object source = edge.getSource();
            Object target = edge.getTarget();
            String id_edge = edge.getId();
            
            //nejdrive DFS i s touto hranou
            int count1 = countDFS(inner.graph, inner.actualVert, inner);
            inner.graph.getModel().remove(edge);
            //pak dfs bez te hrany
            int count2 = countDFS(inner.graph, inner.actualVert, inner);
            //tady ji musim nejak vratit do grafu DANONE
            java.lang.Object parent = inner.graph.getDefaultParent();
            /* kopie edge */
            edge = (mxCell)inner.graph.insertEdge(parent, id_edge, "", source, target);
            
            if(count1 > count2){
                if(n==0)
                    n++;
                else
                    n--;
                            
                edge = (mxCell)found.get(n); 
            }
            
        }
        else if(vertex.getEdgeCount() == 1){
            edge = (mxCell) found.get(0);
        }
        else{
            inner.graph.getSelectionModel().clear();
            return null;
        }
        
        inner.graph.getSelectionModel().clear();
        return edge;
    }

    
    
    /***** This function will perform one step backward *****/
    public void oneStepBack(){    
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        if(!inner.parent.StepFwdButton.isEnabled()){
            inner.parent.StepFwdButton.setEnabled(true);
        }
        
        if(inner.edges_walk.size()>0){
            inner.graph.getModel().beginUpdate();
            try {
                java.lang.Object parent = inner.graph.getDefaultParent();
                mxCell cell = (mxCell)inner.edges_walk.get(inner.edges_walk.size()-1);
                inner.edges_walk.remove(inner.edges_walk.size()-1);
                inner.finalSequence.remove(inner.finalSequence.size()-1);
                String id = cell.getId();
                Object target = cell.getTarget();
                Object source = cell.getSource();
                inner.graph.insertEdge(parent, id, "", source, target);
                inner.graph.getModel().setStyle(inner.actualVert, "fillColor=none");
                if(inner.actualVert == (mxCell)cell.getSource())
                    inner.actualVert = (mxCell)cell.getTarget();
                else
                     inner.actualVert = (mxCell)cell.getSource();
                inner.graph.getModel().setStyle(inner.actualVert, "fillColor=#80c280");
            }
            finally{
                inner.graph.getModel().endUpdate();
            }
        }else{
            inner.parent.StepBackButton.setEnabled(false);
        }
    }
    
    public void performAlg(){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        
        
        
    }
    
        public int countDFS(mxGraph graph, mxCell vertex, InnerFrame inner){
            int ret = 0;
            Stack stack = new Stack();
            ArrayList<String> array = new ArrayList();
  
            stack.add(vertex);
            
            while(!stack.empty()){
                ret++;
                mxCell actualVertex = (mxCell)stack.pop(); //take vertex from stack
                
                array.add(actualVertex.getId());//array contains checked vertexes
                
                ArrayList<mxCell> arr = new ArrayList<mxCell>();
                arr.addAll(getNeighbors(inner, (mxCell) actualVertex));//get all neighbors of vertex
                
                for(mxCell a:arr){
                    if(!arrayContains(array, a.getId()) && stack.search(a) == -1){
                        stack.add(a);
                        
                    }
                }
            }
        return ret;
    }
        
    public boolean arrayContains(ArrayList<String> array, String id){
        for(String s:array){
            if(s.equals(id)){
                return true;
            }
        }
        
        
        return false;
    }
        
    public ArrayList<mxCell> getNeighbors(InnerFrame inner, mxCell vertex){
        inner.graph.selectAll();
        Object[] cells = inner.graph.getSelectionCells();
        
        ArrayList<mxCell> ret = new ArrayList<mxCell>();
        
        for(Object c:cells){
            mxCell cell = (mxCell) c;
            if(cell.isEdge()){
                if(cell.getSource().getId().equals(vertex.getId())){
                    ret.add((mxCell) cell.getTarget());
                }
                else if(cell.getTarget().getId().equals(vertex.getId())){
                    ret.add((mxCell) cell.getSource());
                }
            }
        }
        return ret;
    }
        
    public void printFinal(InnerFrame inner){
        String finalSeq="";
        boolean first = true;
        for(int i =0;i<inner.finalSequence.size() -1;i++){
            mxCell cell = (mxCell) inner.finalSequence.get(i);
            if(first){
                finalSeq += cell.getValue();
                first = false;
            }
            else{
                finalSeq += ", "+cell.getValue();
            }
        }
        Main.action_performed.setText(Main.action_performed.getText()+"\nFinal sequence: "+finalSeq);
    }
}

