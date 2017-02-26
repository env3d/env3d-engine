/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.terrain;

import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvTerrain;

/**
 *
 * @author jmadar
 */
public class Basic {
    public void play() {
        double [] map = {
            0,0,0,0,
            0,3,3,0,
            0,3,3,0,
            0,0,0,0
        };
        EnvAdvanced env = new EnvAdvanced();
        //EnvTerrain terrain = new EnvTerrain(map);
        EnvTerrain terrain = new EnvTerrain("textures/terrain/termap1.png");
        terrain.setTexture("textures/mud.png");
        terrain.useLightingMaterial(true);
        Node rootNode = env.getRootNode();
        
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White.clone().multLocal(1));
        dl.setDirection(new Vector3f(-1, -1, -1).normalize());
        rootNode.addLight(dl);        
        
        terrain.setScale(1, 0.2, 1);
        env.addObject(terrain);
        env.setCameraXYZ(env.getCameraX(), 
                terrain.getHeight(env.getCameraX(), env.getCameraZ())+2, 
                env.getCameraZ());
        while (env.getKey() != 1) {
            
            env.advanceOneFrame();
        }
        env.exit();
    }
    
    public static void main(String args[]) {
        (new Basic()).play();
    }
}

