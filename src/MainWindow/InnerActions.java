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
                    inner.actualVert = (mxCell) inner.first;
                    
                }
                
                inner.parent.PlayButton.setEnabled(false);
                /*while(!inner.abortPressed){
                    Main.controls.wait(inner.waitTime);
                    //oneStepFwd();
                    //inner.printSequence.add((String) inner.actualVert.getValue());
                }*/
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
        
        
        main.AbortButton = new JButton("Start Over");
        Main.controlsPanel.add(main.AbortButton);
        main.AbortButton.setEnabled(false);
        main.AbortButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               Main.action_performed.setText(Main.action_performed.getText() + "\nAbort Pressed");
               InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
               
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

                        inner.graph.getModel().setStyle(inner.actualVert, "fillColor=gray");
                        inner.actualVert = (mxCell)cell.getSource();
                        inner.graph.getModel().setStyle(inner.actualVert, "fillColor=gray");
                        }
                    finally{
                        inner.graph.getModel().endUpdate();
                    }
                }
                inner.edges_walk.clear();
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
                inner.finalSequence.add(inner.actualVert.getId());
                System.out.println(edge.getId() + ":" + edge.getTarget().getId() + ":" + edge.getSource().getId());
                inner.printSequence.add((String) inner.actualVert.getValue());
                /* insert edge into array for walkthrough */ 
                inner.edges_walk.add(edge);
                
                inner.graph.getModel().remove(edge);
            } finally {
                inner.graph.getModel().endUpdate();
            }
        }
        else{
            printFinal(inner);
        }
        
        
        /*Random rand = new Random();
        int  n = rand.nextInt(inner.vertexes.size()-1);
        Main.utils.countDFS(inner.graph, (mxCell)inner.vertexes.get(n), inner);*/
    }
    
    
    public mxCell getEdge(mxCell vertex){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        inner.graph.selectAll();
        
        Object[] cells = inner.graph.getSelectionCells();
        ArrayList found = new ArrayList();
        mxCell edge = null;
        
        System.out.println("velikost cells pokazde: "+cells.length);
        System.out.println(vertex);
        if(vertex.getEdgeCount() == 1){
            System.out.println("velikost cells: "+cells.length);
        }
        
        for(Object c:cells){
            mxCell cell = (mxCell)c;
            if(cell.isEdge() && (cell.getSource().getId().equals(vertex.getId()) || cell.getTarget().getId().equals(vertex.getId()))){
                found.add(cell);
//                if(inner.pathMap.containsKey(cell.getSource().getId()) && !inner.pathMap.get(cell.getSource().getId()).equals(cell.getTarget().getId())){
//                    found.add(cell);
//                }
//                else if(inner.pathMap.containsKey(cell.getTarget().getId()) && !inner.pathMap.get(cell.getTarget().getId()).equals(cell.getSource().getId())){
//                    found.add(cell);
//                }
            }
        }
        
        inner.graph.getSelectionModel().clear();
        
        Main.action_performed.setText(Main.action_performed.getText()+"\n"+inner.actualVert.getId()+": "+inner.actualVert.getEdgeCount());
        //ArrayList<Object> edges;
        if(vertex.getEdgeCount() > 1) {
            //edges = new ArrayList();
            
            Random rand = new Random();
            int n = rand.nextInt(found.size()-1);
            System.out.println("vice edgu, edge: "+vertex.getEdgeCount()+", found: "+found.size());
            edge = (mxCell) found.get(n);
            
            //uchovam si source a target hrany kterou zkoumam
            Object source = edge.getSource();
            Object target = edge.getTarget();
            String id_edge = edge.getId();
            
            //nejdrive DFS i s touto hranou
            int count1 = countDFS(inner.graph, inner.actualVert, inner);
            System.out.println("dfs count1: "+count1);
            //inner.graph.getModel().remove(edge);
            inner.graph.getModel().remove(edge);
            //pak dfs bez te hrany
            int count2 = countDFS(inner.graph, inner.actualVert, inner);
            System.out.println("dfs count2: "+count2);
            
            //tady ji musim nejak vratit do grafu DANONE
            java.lang.Object parent = inner.graph.getDefaultParent();
            /* kopie edge */
            inner.graph.insertEdge(parent, id_edge, "", source, target);
            
            if(count1 > count2){
                if(n==0){
                    n++;
                }
                else{
                    n--;
                }
                edge = (mxCell) found.get(n); 
            }
        }
        else if(vertex.getEdgeCount() == 1){
            System.out.println("jen jeden edge, "+found.size());
            edge = (mxCell) found.get(0);
            System.out.println(edge.getSource().getId());
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
        Main.action_performed.setText(Main.action_performed.getText()+"\nOneStepBack");
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        if(inner.edges_walk.size()>0){
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
                inner.actualVert = (mxCell)cell.getSource();
                inner.graph.getModel().setStyle(inner.actualVert, "fillColor=#80c280");
            }
            finally{
                inner.graph.getModel().endUpdate();
            }
        }else{
            Main.action_performed.setText(Main.action_performed.getText()+"\nEnd of walkthrough");
        }
    }
    
    public void performAlg(){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        
        
        
    }
    
        public int countDFS(mxGraph graph, mxCell vertex, InnerFrame inner){
            int ret = 0;
            Stack stack = new Stack();
            Main.utils.graphMatrix(inner);
            
            graph.selectAll();
            Object[] cells = graph.getSelectionCells();    
            ArrayList<mxCell> array = new ArrayList();
        
//            for(Object c:cells){
//                mxCell cell = (mxCell) c;
//                if(cell.isVertex() && cell!=vertex){
//                    array.add(cell);
//                }
//            }
            
            stack.add(vertex);
            
            while(!stack.empty()){
                mxCell actualVertex = (mxCell)stack.pop(); //take vertex from stack
                ret++;
                array.add(actualVertex);
                Main.action_performed.setText(Main.action_performed.getText()+"\nvertex: "+actualVertex.getId());
                int index = inner.getArrayIndex(vertex.getId());
                
                for(int i=0; i<inner.vertexes.size(); i++){
                    if(inner.matrix[index][i] == 1){
                        if(!array.contains((mxCell)inner.vertexes.get(i))){
                            stack.add(inner.vertexes.get(i));
                        }
                    }
                }
            }
        return ret;
    }
        
    public void printFinal(InnerFrame inner){
        String finalSeq="";
        for(int i =0;i<inner.printSequence.size() -1;i++){
            finalSeq += inner.printSequence.get(i)+", ";
        }
        Main.action_performed.setText(Main.action_performed.getText()+"\n"+finalSeq);
    }
}

