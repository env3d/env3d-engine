import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvNode;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import env3d.util.Sysutil;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.net.*;
import java.util.*;

public class Game extends env3d.EnvApplet
{
    private EnvAdvanced env;
    private EnvTerrain terrain;
    
    private void setup()
    {                        
        env.setDefaultControl(false);
        // Load the game objects
        try {
            URL url = Sysutil.getURL("world.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[0].equalsIgnoreCase("skybox")) {
                    // The background
                    env.setRoom(new EnvSkyRoom(fields[1]));
                } else if (fields[0].equalsIgnoreCase("camera")) {
                    // The camera
                    env.setCameraXYZ(Double.parseDouble(fields[1]), Double.parseDouble(fields[2]), Double.parseDouble(fields[3]));
                    env.setCameraYaw(Double.parseDouble(fields[4]));
                    env.setCameraPitch(Double.parseDouble(fields[5]));
                } else if (fields[0].equalsIgnoreCase("terrain")) {
                    terrain = new EnvTerrain(fields[1]);
                    terrain.setTexture(fields[3]);
                    env.addObject(terrain);
                } else if (fields[0].equalsIgnoreCase("object")) {                    
                    GameObject n = (GameObject) Class.forName(fields[10]).newInstance();
                    n.setX(Double.parseDouble(fields[1]));
                    n.setY(Double.parseDouble(fields[2]));
                    n.setZ(Double.parseDouble(fields[3]));
                    n.setScale(Double.parseDouble(fields[4]));
                    n.setRotateX(Double.parseDouble(fields[5]));
                    n.setRotateY(Double.parseDouble(fields[6]));
                    n.setRotateZ(Double.parseDouble(fields[7]));
                    n.setTexture(fields[9]);
                    n.setModel(fields[8]);
                    n.setEnv(env);
                    env.addObject(n);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }

    public void play()
    {
        env = new EnvAdvanced();
        setup();
        
        // Disable the default env3d control
        env.setDefaultControl(false);        
        // Do not hide the mouse
        env.setMouseGrab(true);
        
        int transitionDelay = 150;                
        
        while (true) {           
            
            if (env.getKey() == Keyboard.KEY_ESCAPE) {
                env.setMouseGrab(!env.isMouseGrabbed());
            }
            
            if (!env.isMouseGrabbed()) {
                env.setDisplayStr("Game Paused.  Press ESCAPE to resume", 5, 470);
                env.advanceOneFrame();
                continue;
            } else {
                env.setDisplayStr("Press ESCAPE to pause game and release mouse", 5, 470);
            }
            
            int aliveZombies = 0;
            
            // Call move() on all GameObjects
            Iterator<GameObject> it = env.getObjects(GameObject.class).iterator();
            while (it.hasNext()) {
                GameObject o = it.next();
                o.move();
                if (o instanceof Zombie && !((Zombie)o).isDead()) {
                    aliveZombies++;
                }
            }
            
            // All zombies are dead, reset level with higher difficulty
            if (aliveZombies == 0) {
                transitionDelay--;
                env.setDisplayStr("Level Cleared!", 150, 300, 3, 1, 0, 0, 0.5);
                if (transitionDelay <= 0) {
                    env.setDisplayStr(null);
                    MiniMath.DIFFICULTY++;
                    setup();              
                    transitionDelay = 150;
                }
            }
            
            // If hunter is dead, reset level
            if (env.getObject(Hunter.class).getState() == Hunter.DIE) {
                transitionDelay--;                
                env.setDisplayStr("F A I L", 100, 360, 10, 1, 0, 0, 0.5);
                if (env.getCameraPitch() > -80) {
                    env.setCameraPitch(env.getCameraPitch()-4);                    
                }
                
                env.setCameraXYZ(env.getCameraX(), env.getCameraY()+0.1, env.getCameraZ());
                
                if (transitionDelay <= 0) {
                    env.setDisplayStr(null);
                    setup();
                    transitionDelay = 150;
                }
            }            
            
            env.advanceOneFrame(30);
        }
        //env.exit();
    }
    
    public static void main(String [] args) {
        (new Game()).play();
    }
}
