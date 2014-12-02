package MainWindow;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//TODO
/*
-dodelat osetreni kdyz neni vybrany graf
-load musi splnovat .xml - a hlavne loadovat graphml!!

-zkusit obarvit nejaky uzly
-chyba pri loadu nacteneho grafu a pridani fci(na to se podivam ja)
-vybrani umisteni na ulozeni obrazku
-muzes si vypsat vsechny uzly a priradit k nim hrany, do nejake Mapy
-dva pruchody v jednom si projit vsecky uzly a dat do mapy v druhem vsechny hrany a prirazovat true false

-umoznit mazani uzlu a hran!! - pri editaci nejdou smazat uzly ktery omylem vytvorim
-hrany musi koncit v uzlech!! - nejlepe aby to neslo vytvorit mimo uzly ty hrany??!? jestli to jde
-jediny co by melo zustat je moznost po startu zvolit nejaky uzel kde se zacne Fleury

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
    

    public static void main(String[] args) {
        MainFrame f = new MainFrame();
        Actions buttonActions = new Actions(f);
        InnerActions controls = new InnerActions(f, desktopPanel);
        
        
        
        //f.setVisible(true);
    }
}
