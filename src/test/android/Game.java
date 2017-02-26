/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.android;

import com.jme3.input.controls.AnalogListener;
import env3d.android.EnvAndroid;
import env3d.android.EnvMobileGame;


/**
 *
 * @author jmadar
 */
public class Game extends EnvMobileGame {


    @Override
    public void setup() {
        env.setRoom(new Room(10,13,10,"A Room"));
        
        env.addObject(new Doty(5, 1, 5));
        
        // Position the camera
        env.setCameraXYZ(5, 13, 9);        
        env.setCameraPitch(-75);         
    }

    @Override
    public void loop() {
        
    }    
    
    public static void main(String args[]) {
        Game g = new Game();
        g.setEnv(new EnvAndroid());
        g.start();        
    }
}
