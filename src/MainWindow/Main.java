package MainWindow;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//TODO
/*
-hrany musi koncit v uzlech!! - nejlepe aby to neslo vytvorit mimo uzly ty hrany??!? jestli to jde

-u prvniho new chart neni krizek ani resize

-
*/

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JDesktopPane;

/**
 *
 * @author Formaiko
 */
public class Main {
    
    /*****   swing components *****/
    static public JFrame f;
    
    static public JPanel graphPanel;
    static public JPanel mainPanel;
    
    static public JDesktopPane desktopPanel;
        
    static public JPanel infoPanel;
    static public JPanel controlsPanel;
    static public JPanel buttonPanel;
    static public JPanel startPanel;
    static public JPanel viewPanel;
    static public JPanel centerPanel;
    
    static public JTextArea vertex_text;
    static public JTextArea edge_text;
    static public JScrollPane scroll_area;
    static public JTextArea chart_title;
    static public JTextArea action_performed;
    
    static public Utils utils;
    static public InnerActions controls;
    

    public static void main(String[] args) {
        MainFrame f = new MainFrame();
        Actions buttonActions = new Actions(f);
        controls = new InnerActions(f, desktopPanel);
        
        
        
        //f.setVisible(true);
    }
}
