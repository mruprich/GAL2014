/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package MainWindow; 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.JDesktopPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Dan
 */
public class MainFrame extends JFrame implements MouseListener,MouseWheelListener,KeyListener{
    
    /*****  "global" variables  *****/
    public ArrayList<Thread> threads;

    public int innerFrameCount = 0;
    public int activeFrame = 0;

    public static JDesktopPane deskPanel;

    public ArrayList<Object> innerFrameArray;

    /***** Buttons *****/
    public JButton NewButton;
    public JButton SaveButton;
    public JButton LoadButton;
    public JButton DeleteButton;
    public JButton SaveAsImage;
    public JButton OrientedButton;
    public JButton StartButton;
    public JButton HelpButton;

    /***** controls buttons *****/
    public JButton SlowDownButton;
    public JButton StepBackButton;
    public JButton PlayButton;
    public JButton PauseButton;
    public JButton AbortButton;
    public JButton StepFwdButton;
    public JButton SpeedUpButton;
    public JButton ReeditButton;
      

    public MainFrame() {
        innerFrameArray = new ArrayList<Object>();
        Main.utils = new Utils();
        
        /***** Main window *****/
        //Main.f = new MainFrame();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /********************************************************************
        * Main pannels
        ********************************************************************/
        
        /***** Pannel for graph creation - multidocument interface *****/
        Main.graphPanel = new JPanel();
        Main.graphPanel.setLayout(new BorderLayout());
        
        
        //add multidocument panel to center panel area
        Main.desktopPanel = new JDesktopPane();
        
        
        /***** Pannel for controls inside graph *****/
        Main.controlsPanel = new JPanel();
        Main.controlsPanel.setLayout(new BorderLayout());
        Main.controlsPanel.setPreferredSize(new Dimension(300, 70));
        Main.controlsPanel.setLayout(new GridLayout(1,7));
        
        
        
        /***** Pannel on the right side *****/
        Main.mainPanel = new JPanel();
        Main.mainPanel.setBorder(new LineBorder(Color.BLACK));
        Main.mainPanel.setLayout(new BorderLayout());
        Main.mainPanel.setPreferredSize(new Dimension(350,350));

                    
        /**********************************************************
         * Inside Pannels
         *********************************************************/
        /***** Pannel for displying info about graph *****/
        Main.infoPanel = new JPanel();
        Main.infoPanel.setLayout(new BoxLayout(Main.infoPanel, BoxLayout.Y_AXIS));
        Main.infoPanel.setPreferredSize(new Dimension(100, 270));
        
        /***** Panel for buttons *****/
        Main.buttonPanel = new JPanel();
        Main.buttonPanel.setLayout(new GridLayout(5,1));
        Main.buttonPanel.setPreferredSize(new Dimension(100, 70));
        
        /***** Panel for start button *****/
        Main.startPanel = new JPanel();
        Main.startPanel.setLayout(new BorderLayout());
        Main.startPanel.setBorder(new EmptyBorder(15,20,15,20));
        Main.startPanel.setPreferredSize(new Dimension(100, 70));
          
        Main.mainPanel.add(Main.infoPanel, BorderLayout.PAGE_START);
        Main.mainPanel.add(Main.buttonPanel, BorderLayout.CENTER);
        Main.mainPanel.add(Main.startPanel, BorderLayout.PAGE_END);
        

        /******************************************************
         * Frames in infoPanel
         ******************************************************/
        Dimension basicDim = new Dimension(Integer.MAX_VALUE, 20);
        
        /*****  Frame INFO  *****/
        Main.action_performed = new JTextArea(4,1);//JTextArea(Document doc, String text, int rows, int columns)
        Main.action_performed.setSize(new Dimension(100,150));
        Main.action_performed.append("Chart inicialized");
        
        Main.scroll_area = new JScrollPane(Main.action_performed);
        
        
        /***** Vertex info *****/
        Main.vertex_text = new JTextArea();
        Main.vertex_text.setEditable(false);
        Main.vertex_text.setMaximumSize(basicDim);
        Main.vertex_text.append("Number of vertexes: 0");
        
        /***** Edge info *****/
        Main.edge_text = new JTextArea();
        Main.edge_text.setEditable(false);
        Main.edge_text.setMaximumSize(basicDim);
        Main.edge_text.append("Number of edges: 0");
        
        
 
       
//        /***** FWD button *****/
//        JButton FurtherButton = new JButton("->");
//        FurtherButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                double s = graph.getView().getScale();
//                Rectangle rect = graphComponent.getGraphControl().getVisibleRect();
//                rect.translate(50, 100);
//                graphComponent.getGraphControl().scrollRectToVisible(rect, true);
//                action_performed.setText(action_performed.getText()+"\n"+"Forward step.");
//            }
//        });


//        
//        /***** StepBackButton *****/
//        JButton StepBackButton = new JButton("STEP BACK!");
////StepBackButton.setSize(new Dimension(100, 80));
////try {
////    Image img = ImageIO.read(getClass().getResource("pokus.png"));
////    StepBackButton.setIcon(new ImageIcon(img.getScaledInstance(StepBackButton.getWidth(), StepBackButton.getHeight(), 0)));
////} catch (IOException ex) {
////}
//        
//        StepBackButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"One small step back...");
//            }
//        });
//        
//        /***** StepFwdButton *****/
//        JButton StepFwdButton = new JButton("FORWARD!");
//        StepFwdButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"One small step forward...");
//            }
//        });
//        
//        /***** SpeedUpButton *****/
//        JButton SpeedUpButton = new JButton("FASTER!");
//        SpeedUpButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"Faster! Faster!");
//            }
//        });
//        
//        /***** SlowDownButton *****/
//        JButton SlowDownButton = new JButton("SLOWER!");
//        SlowDownButton.addActionListener( new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                action_performed.setText(action_performed.getText()+"\n"+"Not so fast dude!");
//            }
//        });
        
        
        
        
        /******************************************************
         * Adding buttons to their locations
         *****************************************************/
        
        
        /* infoPanel */
        Main.infoPanel.add(Main.scroll_area);
        Main.infoPanel.add(Main.vertex_text);
        Main.infoPanel.add(Main.edge_text);

        /* ADD COMPONENTS TO FRAME AT THE END */
        Main.graphPanel.add(BorderLayout.CENTER,Main.desktopPanel);
        Main.graphPanel.add(BorderLayout.PAGE_END, Main.controlsPanel);
        this.getContentPane().add(BorderLayout.CENTER, Main.graphPanel);
        this.getContentPane().add(BorderLayout.EAST, Main.mainPanel);
        /* END OF ADDING */
        
        this.setVisible(true);
        
        /* REVALIDATE ALL JPANELS AND JFRAME AFTER INICIALIZATION */
        Main.graphPanel.revalidate();
        Main.graphPanel.repaint();
        Main.controlsPanel.revalidate();
        Main.controlsPanel.repaint();      
        Main.mainPanel.revalidate();
        Main.mainPanel.repaint();
        this.revalidate();
        this.repaint();
        /* END OF REVALIDATION */ 
    }
    
    public int getInnerFrameCount(){
        return this.innerFrameCount;
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {
    }
    
    @Override
    public void mouseEntered(MouseEvent me) {
    }
    
    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        System.out.println(mwe.getWheelRotation());
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void keyReleased(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}