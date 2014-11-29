/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDesktopPane;




//public JButton SlowDownButton;
//      public JButton StepBackButton;
//      public JButton PlayButton;
//      public JButton PauseButton;
//      public JButton AbortButton;
//      public JButton StepFwdButton;
//      public JButton SpeedUpButton;
/**
 *
 * @author Formaiko
 */
public class InnerActions {
    public InnerActions(MainFrame main,JDesktopPane inner){
        main.SlowDownButton = new JButton("SlowDown");
        Main.controlsPanel.add(main.SlowDownButton);
        main.SlowDownButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.action_performed.setText(Main.action_performed.getText() + "\nSlowDown Pressed");
            }
        });
        
        main.StepBackButton = new JButton("StepBack");
        Main.controlsPanel.add(main.StepBackButton);
        main.StepBackButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.action_performed.setText(Main.action_performed.getText() + "\nStepBack Pressed");
            }
        });
        
        
        main.PlayButton = new JButton("Play");
        Main.controlsPanel.add(main.PlayButton);
        main.PlayButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.action_performed.setText(Main.action_performed.getText() + "\nPlay Pressed");
            }
        });
        
        
        main.PauseButton = new JButton("Pause");
        Main.controlsPanel.add(main.PauseButton);
        main.PauseButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.action_performed.setText(Main.action_performed.getText() + "\nPause Pressed");
            }
        });
        
        
        main.AbortButton = new JButton("Abort");
        Main.controlsPanel.add(main.AbortButton);
        main.AbortButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.action_performed.setText(Main.action_performed.getText() + "\nAbort Pressed");
            }
        });
        
        
        main.StepFwdButton = new JButton("Step forward");
        Main.controlsPanel.add(main.StepFwdButton);
        main.StepFwdButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.action_performed.setText(Main.action_performed.getText() + "\nStepFwd Pressed");
            }
        });
        
        
        main.SpeedUpButton = new JButton("SpeedUp");
        Main.controlsPanel.add(main.SpeedUpButton);
        main.SpeedUpButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.action_performed.setText(Main.action_performed.getText() + "\nSpeedUp Pressed");
            }
        });
    }
    
}
