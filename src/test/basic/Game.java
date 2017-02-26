/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.basic;

import env3d.Env;
import env3d.EnvVector;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author jmadar
 */
public class Game {


    public void play() {
        Env env = new Env();

        Doty d = new Doty();
        env.addObject(d);
        env.setDefaultControl(false);
        while (true) {
            
            Object o = (env.getPick(env.getMouseX(), env.getMouseY()));
            if (o instanceof Doty) {
                ((Doty) o).spin();
            }
            if (env.getKeyDown(Keyboard.KEY_ESCAPE)) {
                System.exit(0);
            }
            if (env.getKey() == Keyboard.KEY_G) {
                env.setMouseGrab(!env.isMouseGrabbed());
            }
            if (env.getKey() == Keyboard.KEY_F) {
                env.setFullscreen(!env.isFullScreen());
            }
            if (env.getKey() == Keyboard.KEY_Q) {
                env.setFullscreen(false);
            }
                        
            d.move();
            EnvVector coord = new EnvVector();
            env.getScreenCoordinate(d.x, d.y+1, d.z, coord);            
            env.setDisplayStr(null);
            env.setDisplayStr(env.getFrameRate()+"");            
            int x = env.getScreenCoordinateX(d.x, d.y+1, d.z);
            int y = env.getScreenCoordinateY(d.x, d.y+1, d.z);
            env.setDisplayStr("Hi", x, y);
            env.advanceOneFrame();
        }
    }
    public static void main(String[] args) {
        (new Game()).play();
    }
}
