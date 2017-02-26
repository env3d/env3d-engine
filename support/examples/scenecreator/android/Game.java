
import env3d.android.EnvMobileGame;
import env3d.android.EnvAndroid;
import env3d.advanced.EnvNode;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import env3d.util.Sysutil;

import java.io.*;
import java.net.*;
import java.util.*;

public class Game extends EnvMobileGame
{    
    public void setup()
    {
                
        // Load the game objects
        try {
            BufferedReader br;
            try {
                URL url = Sysutil.getURL("world.txt");
                br = new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (FileNotFoundException e) {
                URL url = Sysutil.getURL("assets/world.txt");
                br = new BufferedReader(new InputStreamReader(url.openStream()));
            }

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
                    EnvNode terrain = new EnvNode();
                    terrain.setTexture(fields[3]);
                    // Terrain will be a flat surface, 300x300 units big.
                    terrain.setModel("box 30 30 30");
                    terrain.setScale(10);
                    terrain.setX(-150);
                    terrain.setZ(-150);
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

    public void loop()
    {
        for (GameObject o : env.getObjects(GameObject.class)) {
            o.move();
        }                
    }

    
    public static void main(String [] args) {
        Game g = new Game();        
        g.start();
    }
}

