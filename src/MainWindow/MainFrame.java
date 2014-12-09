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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author Dan
 */
public class MainFrame extends JFrame implements MouseListener,MouseWheelListener,KeyListener{
// /***** "global" variables *****/
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
        Main.f = new JFrame();
        Main.f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Main.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        Main.controlsPanel.setBorder(new EmptyBorder(15,15,15,15));
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
        Main.infoPanel.setPreferredSize(new Dimension(100, 170));
        /***** Panel for buttons *****/
        Main.buttonPanel = new JPanel();
        Main.buttonPanel.setLayout(new GridLayout(5,1));
        Main.buttonPanel.setPreferredSize(new Dimension(100, 150));
        
        Main.viewPanel = new JPanel();
        Main.viewPanel.setLayout(new BorderLayout());
        Main.viewPanel.setBorder(new LineBorder(Color.BLACK));
        Main.viewPanel.setPreferredSize(new Dimension(100, 250));
        
        Main.centerPanel  = new JPanel();
        Main.centerPanel.setLayout(new BorderLayout());
        Main.centerPanel.add(Main.buttonPanel, BorderLayout.PAGE_START);
        Main.centerPanel.add(Main.viewPanel, BorderLayout.CENTER);
        
        
        /***** Panel for start button *****/
        Main.startPanel = new JPanel();
        Main.startPanel.setLayout(new BorderLayout());
        Main.startPanel.setBorder(new EmptyBorder(15,20,15,20));
        Main.startPanel.setPreferredSize(new Dimension(100, 70));
        Main.mainPanel.add(Main.infoPanel, BorderLayout.PAGE_START);
        Main.mainPanel.add(Main.centerPanel, BorderLayout.CENTER);
        Main.mainPanel.add(Main.startPanel, BorderLayout.PAGE_END);
        /******************************************************
         * Frames in infoPanel
         ******************************************************/
        Dimension basicDim = new Dimension(Integer.MAX_VALUE, 20);
        /***** Frame INFO *****/
        Main.action_performed = new JTextArea(4,1);//JTextArea(Document doc, String text, int rows, int columns)
        Main.action_performed.setSize(new Dimension(100,150));
        Main.action_performed.append("Chart inicialized");
        Main.action_performed.setEditable(false);
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
        
        
        /***** WindowListener to make sure window isnt closed prematurely *****/
        Main.f.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                int answer = JOptionPane.showConfirmDialog(Main.f, "Do you really want to quit the program?\n"
                        + "Any unsaved work will be lost!", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
                else{
                    Main.f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
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
        Main.f.getContentPane().add(BorderLayout.CENTER, Main.graphPanel);
        Main.f.getContentPane().add(BorderLayout.EAST, Main.mainPanel);
        /* END OF ADDING */
        Main.f.setVisible(true);
        /* REVALIDATE ALL JPANELS AND JFRAME AFTER INICIALIZATION */
        Main.graphPanel.revalidate();
        Main.graphPanel.repaint();
        Main.controlsPanel.revalidate();
        Main.controlsPanel.repaint();
        Main.mainPanel.revalidate();
        Main.mainPanel.repaint();
        Main.f.revalidate();
        Main.f.repaint();
        /* END OF REVALIDATION */
    }
    
    public void closeOperation(){
        
        
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
