/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import com.mxgraph.model.mxCell;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                
                Thread play = new Thread(){
                    @Override
                    public void run(){
                        performAlg();
                    }
                };
                play.start();
                
                //while(!inner.pausePressed){
//                    try {
//    Thread.sleep(1000);                 //1000 milliseconds is one second.
//} catch(InterruptedException ex) {
//    Thread.currentThread().interrupt();
//}
                    
                    
                    
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

    /***** this function will perform one step through the graph *****/
    public void oneStepFwd(){
        Main.action_performed.setText(Main.action_performed.getText()+"\nOneStepFwd");

    }

    /***** This function will perform one step backward *****/
    public void oneStepBack(){
        Main.action_performed.setText(Main.action_performed.getText()+"\nOneStepBack");
    }

    public void performAlg(){
        InnerFrame inner = (InnerFrame)Main.desktopPanel.getSelectedFrame();
        while(!inner.pausePressed){
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            
            oneStepFwd();
        }
        
        Thread.interrupted();
    }
}
    
