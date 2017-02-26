import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvNode;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import env3d.util.Sysutil;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.net.*;
import java.util.*;

public class Game
{
    private EnvAdvanced env;
    private EnvTerrain terrain;
    
    private void setup()
    {
        env = new EnvAdvanced();
                
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
                    terrain.setTextureAlpha(fields[2]);
                    terrain.setTexture(fields[3], 1);
                    terrain.setTexture(fields[4], 2);
                    terrain.setTexture(fields[5], 3);
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
        setup();
        
        // Disable the default env3d control
        env.setDefaultControl(false);        
        // Do not hide the mouse
        env.setMouseGrab(false);
        
        while (env.getKey() != Keyboard.KEY_ESCAPE) {
            for (GameObject o : env.getObjects(GameObject.class)) {
                o.move();
            }
            env.advanceOneFrame(30);
        }
        env.exit();
    }
    
    public static void main(String [] args) {
        (new Game()).play();
    }
}
