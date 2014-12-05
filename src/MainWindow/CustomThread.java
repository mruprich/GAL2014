/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainWindow;

/**
 *
 * @author Formaiko
 */
public class CustomThread extends Thread {
    
    public CustomThread(){
        
    }
    
    @Override
    public void start(){
        
        InnerFrame newFrame = new InnerFrame(500,500,++Main.f.innerFrameCount, Main.f);
        
        
        Main.utils.createComp(newFrame);
                
        Main.action_performed.setText(Main.action_performed.getText() + "\n" + "chart" + Main.f.innerFrameCount + " created");
    }
}
