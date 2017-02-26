/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.node;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import env3d.Env;
import env3d.EnvObject;
import env3d.advanced.EnvNode;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author jmadar
 */
public class Game {


    public void play() {
        Env env = new Env();
        env.enableLighting();
        
        EnvNode d = new EnvNode();
        d.setX(5); d.setY(0.3); d.setZ(5);
        d.setScale(3);
//        d.setModel("models/oto/Oto.mesh.xml");
//        d.setTexture("models/oto/Oto.png");
//        d.setModel("models/tux/tux.obj");
//        d.setTexture("models/tux/tux.png");

        //d.setTexture("textures/doty.png");
        //d.setTexture("/Users/jmadar/Documents/models/Mech/Mech4camo2.png");
        //d.setTexture(null);
        //d.setModel("/Users/jmadar/Documents/models/Mech/Mech4_final.obj");
        //d.setModel("/Users/jmadar/Documents/models/metro/metro2.obj");
        d.setTexture(null);
        d.setModel("/Users/jmadar/Documents/models/spider/spider.blend");
        AnimControl c = d.getJme_node().getControl(AnimControl.class);
        for (String n : c.getAnimationNames()) {
            System.out.println(n);
        }
        //d.setTexture("/Users/jmadar/Documents/models/spider/spidertex.tga");
        env.addObject(d);
        //env.setDefaultControl(false);

//        AnimControl control = d.getJme_node().getChild(0).getControl(AnimControl.class);
//        for (String anim : control.getAnimationNames()) { System.out.println(anim); }
//        AnimChannel channel = control.createChannel();
//        channel.setAnim("pull");
//        channel.setSpeed(0.1f);
//        channel.setLoopMode(LoopMode.Loop);


        while (true) {
            Object o = env.getPick(env.getMouseX(), env.getMouseY());
            if (o instanceof EnvNode) {
                //((EnvNode) o).setRotateY(((EnvNode)o).getRotateY()-2);
            } else {
                //d.setRotateY(d.getRotateY()+2);
            }
            if (env.getKeyDown(Keyboard.KEY_ESCAPE)) {
                env.exit();
            }
            if (env.getKey() == Keyboard.KEY_G) {
                env.setMouseGrab(!env.isMouseGrabbed());
                env.setDefaultControl(env.isMouseGrabbed());
                if (env.isMouseGrabbed()) {
                    env.enableLighting();
                } else {
                    env.disableLighting();
                }
                //d.useLightingMaterial(true);

            }
            if (env.getKey() == Keyboard.KEY_F) {
                env.setFullscreen(!env.isFullScreen());
            }
            if (env.getKey() == Keyboard.KEY_Q) {
                env.setFullscreen(false);
            }

            
            env.advanceOneFrame();
        }
    }
    public static void main(String[] args) {
        (new Game()).play();
    }
}
