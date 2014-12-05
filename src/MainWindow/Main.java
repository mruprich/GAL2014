package MainWindow;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//TODO
/*
-dopsat Fleuryho algoritmus aspon pseudokodem na git
-ukladani obrazku - chtelo by to aspon nejaky padding
-pridat kontrolu zavreni celeho programu!!
-kdyz upravim uzel - jeho id - a pak kliknu jinam tak se vytvori dalsi uzel
-spravne jmena grafu pri load a save

-load musi splnovat .xml - a hlavne loadovat graphml!!

-zkusit obarvit nejaky uzly
-chyba pri loadu nacteneho grafu a pridani fci(na to se podivam ja)
-muzes si vypsat vsechny uzly a priradit k nim hrany, do nejake Mapy
-dva pruchody v jednom si projit vsecky uzly a dat do mapy v druhem vsechny hrany a prirazovat true false

-umoznit mazani uzlu a hran!! - pri editaci nejdou smazat uzly ktery omylem vytvorim
-hrany musi koncit v uzlech!! - nejlepe aby to neslo vytvorit mimo uzly ty hrany??!? jestli to jde
-jediny co by melo zustat je moznost po startu zvolit nejaky uzel kde se zacne Fleury

-pri close nejak kontrolovat zda je graf nejak upravenej od posledniho savu?!?! jde to vubec??
-u prvniho new chart neni krizek ani resize

-pri startu zakazat tlacitka dole!
-melo by to vytvaret posloupnost uzlu ktery se prochazely
-zkusit nejaky graphml

-kdyz zavru samotny okynko uvnitr tak se musi prehodit aktivace buttonu

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
