/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.model;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import env3d.Env;
import env3d.EnvObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author jmadar
 */
public class Game {


    public void play() {
        
        ColorRGBA d_strength=ColorRGBA.White.clone();
        ColorRGBA a_strength=ColorRGBA.White.clone();

        Env env = new Env();
        Logger.getLogger("com").setLevel(Level.ALL);
        Logger.getLogger("env3d").setLevel(Level.ALL);
        Node rootNode = env.getRootNode();

//        DirectionalLight dl = new DirectionalLight();
//        dl.setColor(d_strength);
//        dl.setDirection(new Vector3f(-1, -1, -1).normalize());
//        rootNode.addLight(dl);
//
//        AmbientLight am = new AmbientLight();
//        am.setColor(a_strength);
//        rootNode.addLight(am);

        EnvObject city = new EnvObject();
        city.setTexture(null);
        //city.setModel("/Users/jmadar/Documents/models/metro/metro2.obj");
        city.setModel("/Users/jmadar/Documents/models/billiard/billiards_0.obj");
        //city.setModel("/Users/jmadar/Documents/models/quakeLevel/main.meshxml");
        city.setScale(100);
        //env.addObject(city);
        

        Doty d = new Doty();
        env.addObject(d);
        //env.enableLighting();
        env.setDefaultControl(true);
        env.setShowStatus(true);
        while (true) {
            Object o = (env.getPick(env.getMouseX(), env.getMouseY()));
            if (o instanceof Doty) {
                System.out.println("Picked Doty");
                ((Doty) o).spin();
            }
            if (env.getKey()==Keyboard.KEY_1) {
                d_strength.r+=0.1;
                d_strength.g+=0.1;
                d_strength.b+=0.1;
                //dl.setColor(d_strength);
            }
            if (env.getKey()==Keyboard.KEY_2) {
                d_strength.r-=0.1;
                d_strength.g-=0.1;
                d_strength.b-=0.1;
                //dl.setColor(d_strength);
            }
            if (env.getKeyDown(Keyboard.KEY_ESCAPE)) {
                System.exit(0);
            }
            if (env.getKey() == Keyboard.KEY_W) {
                d.walk();
            }
            if (env.getKey() == Keyboard.KEY_T) {
                d.stand();
            }
            if (env.getKey() == Keyboard.KEY_L) {
                if (env.isLightingEnabled()) {
                    System.out.println("disable lights");
                    env.disableLighting();
                } else {
                    env.enableLighting();
                    System.out.println("enable lights");
                }
            }


            //d.move();
            env.advanceOneFrame();
        }
    }
    public static void main(String[] args) {
        (new Game()).play();
    }
}
