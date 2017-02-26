/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.advanced;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.math.ColorRGBA;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvNode;
import env3d.advanced.EnvParticle;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import env3d.advanced.EnvWater;
import org.lwjgl.input.Keyboard;


/**
 *
 * @author jmadar
 */
public class Game {


    public void play() {
        EnvAdvanced env = new EnvAdvanced();

        // All the textures required by the skybox
        String textureDir = "textures/skybox/";
        String north = textureDir+"north.png";
        String east = textureDir+"east.png";
        String south = textureDir+"south.png";
        String west = textureDir+"west.png";
        String top = textureDir+"top.png";
        String bottom = textureDir+"bottom.png";

        // Create a the sky box.  The first 3 parameters specifies how far
        // the sky box extends in the x, y, and z direction.  This is for both
        // positive and negative direction on each axis.  The next 6 parameters
        // specifies the texture for each direction.
        //
        // Here, we create a skybox of size 256x256x256.  By default, the skybox's
        // center point is located at 0, 0, 0.
        EnvSkyRoom skyroom = new EnvSkyRoom(
                north, east, south, west, top, bottom);

        // Move the skybox to the middle of the room
        //skyroom.setXYZ(128, 128, 128);

        env.setRoom(skyroom);

        

        EnvNode d = new EnvNode();
        d.setX(5); d.setY(10); d.setZ(-5);
        d.setScale(0.3);
        d.setModel("models/oto/Oto.mesh.xml");
        d.setTexture("models/oto/Oto.png");
        env.addObject(d);
        env.setDefaultControl(true);

        AnimControl control = d.getJme_node().getChild(0).getControl(AnimControl.class);
        for (String anim : control.getAnimationNames()) { System.out.println(anim); }
        AnimChannel channel = control.createChannel();
        channel.setAnim("pull");
        channel.setSpeed(1f);
        channel.setLoopMode(LoopMode.Loop);

        EnvWater water = new EnvWater();
        water.setWaterDepth(100);
        water.getWaterProcessor().setWaterColor(ColorRGBA.Red);
        water.getWaterProcessor().setWaterTransparency(0f);
        env.addObject(water);

        water.setScale(100);
        //water.setY(-10);
        double [] heightMap = { 0,0,0,0,0,0,0,0,
                                0,0,0,0,0,0,0,0,
                                0,0,0,0,0,0,0,0,
                                0,0,0,1,1,1,0,0,
                                0,0,0,1,1,1,0,0,
                                0,0,0,0,0,0,0,0, 
                                0,0,0,0,0,0,0,0, 
                                0,0,0,0,0,0,0,0
        };

        double waveSize = 32;
        double waveStrength = 0.5;
        double waveSpeed = 0.05;
        water.setWaveSize(waveSize);
        water.setWaveStrength(waveStrength);
        water.setWaveSpeed(waveSpeed);

        EnvParticle p = new EnvParticle(300);
        env.addObject(p);
//        EnvTerrain terrain = new EnvTerrain(heightMap);
//        terrain.setY(10);
//        env.addObject(terrain);
        //water.setY(-10);
        while (true) {
            if (env.getKeyDown(Keyboard.KEY_ESCAPE)) {
                System.exit(0);
            }
            if (env.getKey() == Keyboard.KEY_P) {
                channel.setTime(0);
                channel.setAnim("pull");
            }
            if (env.getKey() == (Keyboard.KEY_DOWN)) {
                p.setY(p.getY()-1);

            }
            if (env.getKey() == (Keyboard.KEY_UP)) {
                p.setY(p.getY()+1);

            }
            if (env.getKeyDown(Keyboard.KEY_LEFT)) {
                water.setRotateY(water.getRotateY()+1);
            }
            if (env.getKeyDown(Keyboard.KEY_RIGHT)) {
                water.setRotateY(water.getRotateY()-1);
            }
            if (env.getKeyDown(Keyboard.KEY_Q)) {
                water.setRotateX(water.getRotateX()+1);
            }
            if (env.getKeyDown(Keyboard.KEY_Z)) {
                water.setRotateX(water.getRotateX()-1);
            }

            if (env.getKeyDown(Keyboard.KEY_COMMA)) {
                waveSpeed+=0.001;
                water.setWaveSpeed(waveSpeed);
            }
            if (env.getKeyDown(Keyboard.KEY_PERIOD)) {
                waveSpeed-=0.001;
                water.setWaveSpeed(waveSpeed);
            }


            env.advanceOneFrame();
        }
    }
    public static void main(String[] args) {
        (new Game()).play();
    }
}
