package MainWindow;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//TODO
/*

chyba pri loadu nacteneho grafu a pridani fci(na to se podivam ja)
vybrani umisteni na ulozeni obrazku
*/

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
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
    
    /*****  "global" variables  *****/
//    public int x,y;
//    public int vertex_id = 0;
//    public String xml;
//    static public File soubor = null;
//    public int vertex_num, edge_num = 0;
//    private boolean edge_style = false; //false = unoriented, true = oriented
    
    /*****   swing components *****/
    static public JFrame f;
    
    static public JPanel graphPanel;
    static public JPanel mainPanel;
    
    static public JDesktopPane desktopPanel;
        
    static public JPanel infoPanel;
    static public JPanel controlsPanel;
    static public JPanel buttonPanel;
    static public JPanel startPanel;
    
    static public JTextArea vertex_text;
    static public JTextArea edge_text;
    static public JScrollPane scroll_area;
    static public JTextArea chart_title;
    static public JTextArea action_performed;
    
    static public Utils utils;
    
    
    
//    public mxGraphComponent graphComponent;
//    public mxGraph graph;



    
    
    
    
    
    
    public static void main(String[] args) {
        MainFrame f = new MainFrame();
        Actions buttonActions = new Actions(f);
        InnerActions controls = new InnerActions(f, desktopPanel);
        
        
        
        //f.setVisible(true);
    }
}
