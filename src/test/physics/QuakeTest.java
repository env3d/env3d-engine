/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.physics;

import env3d.advanced.EnvPhysicsNode;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.MaterialList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.ogre.OgreMeshKey;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvNode;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author jmadar
 */
public class QuakeTest {
    private EnvAdvanced env;
    private ArrayList<EnvNode> nodes = new ArrayList<EnvNode>();

    public void play() {
        env = new EnvAdvanced();
        env.setPhysicsEnabled(true);
        Node rootNode = env.getRootNode();

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White.clone().multLocal(1));
        dl.setDirection(new Vector3f(-1, -1, -1).normalize());
        rootNode.addLight(dl);
        
        AmbientLight am = new AmbientLight();
        am.setColor(ColorRGBA.White.mult(2));
        rootNode.addLight(am);

        AssetManager assetManager = env.getAssetManager();
        assetManager.registerLocator("quake3level.zip", ZipLocator.class);


        MaterialList matList = (MaterialList) assetManager.loadAsset("Scene.material");
        OgreMeshKey key = new OgreMeshKey("main.meshxml", matList);
        Node gameLevel = (Node) assetManager.loadAsset(key);
        gameLevel.setLocalScale(0.1f);

        System.out.println("Quake level loaded");

        // add a physics control, it will generate a MeshCollisionShape based on the gameLevel
        gameLevel.addControl(new RigidBodyControl(0));

        EnvPhysicsNode gameLevelNode = new EnvPhysicsNode();
        rootNode.attachChild(gameLevel);
        env.getBulletAppState().getPhysicsSpace().addAll(gameLevel);


        env.addObject(gameLevelNode);
        env.setCameraXYZ(60, 10, -60);
        double strength = 0;
        env.setDisplayStr("+", 315,245,1,1,0,0,1);
        env.setDisplayStr("WASD to move, SPACE to fly up, C to fly down", 5, 80);
        env.setDisplayStr("1 to built tower, 2 to built wall, R to reset, Click to shoot", 5, 60);
        env.setDisplayStr("ESC to toggle Mouse Grab", 5, 40);
        ArrayList<EnvNode> deletedNodes = new ArrayList<EnvNode>();
        env.setCameraXYZ(40, 25, 120);
        env.setCameraPitch(-25);
        env.setCameraYaw(20);
        env.setDefaultControl(true);
        env.setMouseGrab(true);
        env.setShowStatus(true);


        while (true) {
            dl.setDirection(env.getCamera().getDirection());
            //firstPersonCamera();
            if (env.getKey() == 1) {
                //env.exit();
                //env.setMouseGrab(!env.isMouseGrabbed());
                env.setDefaultControl(!env.isDefaultControl());
            }
            if (env.getKey() == Keyboard.KEY_1) {
                double x = (env.getCameraX() - 2*Math.sin(Math.toRadians(env.getCameraYaw())));
                double y = env.getCameraY();
                double z = (env.getCameraZ() - 2*Math.cos(Math.toRadians(env.getCameraYaw())));

                makeTower(x, y, z);
            }
            if (env.getKey() == Keyboard.KEY_2) {
                double x = (env.getCameraX() - 2*Math.sin(Math.toRadians(env.getCameraYaw())));
                double y = env.getCameraY();
                double z = (env.getCameraZ() - 2*Math.cos(Math.toRadians(env.getCameraYaw())));

                makePyrimad(x, Math.max(y, 5), z);
            }
            if (env.getKey() == Keyboard.KEY_R) {
                reset();
            }

            if (env.getMouseButtonDown(0)) {
                strength = strength > 150 ? 150 : strength + 5;
                env.setDisplayStr("Strength is "+strength);
            } else if (strength > 0) {
                double x = env.getCameraX();
                double y = env.getCameraY();
                double z = env.getCameraZ();
                double xv = -strength*Math.sin(Math.toRadians(env.getCameraYaw()))*Math.cos(Math.toRadians(env.getCameraPitch()));
                double yv = strength*Math.sin(Math.toRadians(env.getCameraPitch()));
                double zv = -strength*Math.cos(Math.toRadians(env.getCameraYaw()))*Math.cos(Math.toRadians(env.getCameraPitch()));
                shootBall(x, y, z, xv, yv, zv);
                strength = 0;
                env.setDisplayStr("");
            }

            deletedNodes.clear();
            for (EnvNode n: nodes) {
                if (n instanceof Bomb) {
                    if ( ((Bomb)n).hasMoved() ) {
                    //if (n.getRotateX() > 0.01 || n.getRotateY() > 0.01 || n.getRotateZ() > 0.01) {
                        ((EnvPhysicsNode)n).setCollisionShape(new BoxCollisionShape(new Vector3f(4,4,4)));
                        //n.setScale(5);
                        deletedNodes.add(n);
                        for (int i = 0; i < 10; i++) {
                            env.advanceOneFrame();
                        }

                    }
                }
            }
            for (EnvNode n : deletedNodes) {
                env.removeObject(n);
                nodes.remove(n);
            }

            env.advanceOneFrame();
        }
    }

    private void firstPersonCamera() {
        double step = 0.2;
        if (env.getKeyDown(Keyboard.KEY_W)) {
            env.setCameraXYZ(env.getCameraX()-step*Math.sin(Math.toRadians(env.getCameraYaw())),
                                env.getCameraY(),
                                env.getCameraZ()-step*Math.cos(Math.toRadians(env.getCameraYaw())));
        }

        if (env.getKeyDown(Keyboard.KEY_S)) {
            env.setCameraXYZ(env.getCameraX()+step*Math.sin(Math.toRadians(env.getCameraYaw())),
                                env.getCameraY(),
                                env.getCameraZ()+step*Math.cos(Math.toRadians(env.getCameraYaw())));
        }

        if (env.getKeyDown(Keyboard.KEY_A)) {
            env.setCameraXYZ(env.getCameraX()-step*Math.cos(Math.toRadians(env.getCameraYaw())),
                                env.getCameraY(),
                                env.getCameraZ()+step*Math.sin(Math.toRadians(env.getCameraYaw())));
        }
        if (env.getKeyDown(Keyboard.KEY_D)) {
            env.setCameraXYZ(env.getCameraX()+step*Math.cos(Math.toRadians(env.getCameraYaw())),
                                env.getCameraY(),
                                env.getCameraZ()-step*Math.sin(Math.toRadians(env.getCameraYaw())));
        }
        if (env.getKeyDown(Keyboard.KEY_SPACE)) {
            env.setCameraXYZ(env.getCameraX(),
                                env.getCameraY()+step,
                                env.getCameraZ());
        }
        if (env.getKeyDown(Keyboard.KEY_C)) {
            env.setCameraXYZ(env.getCameraX(),
                                env.getCameraY()-step,
                                env.getCameraZ());
        }
        double camSpeed = 0.5;
        double newPitch = env.getCameraPitch()+camSpeed*env.getMouseDY();

        // Restrict the pitch to within a "reasonable" amount
        if (newPitch > 70) {
            env.setCameraPitch(70);
        } else if (newPitch < -50) {
            env.setCameraPitch(-50);
        } else {
            env.setCameraPitch(newPitch);
        }

        env.setCameraYaw(env.getCameraYaw()-camSpeed*env.getMouseDX());
    }

    private void makeTower(double x, double height, double z) {
        for (int y=1; y<height; y+=2) {
            makeBlock(x, y, z);
            env.advanceOneFrame();
        }
    }

    private void makePyrimad(double x, double height, double z) {
        for (double y=1; y<height; y+=2) {
            for (int row=0; row<height; row+=2) {
                makeBlock(x+row, y, z);
                //env.advanceOneFrame();
            }
        }
    }


    private void makeBlock(double x, double y, double z) {
        EnvPhysicsNode n;

        double rand = Math.random();
        if (rand < 0.05) {
            n = new EnvPhysicsNode();
//            n.setTexture("models/tux/tux.png");
//            n.setModel("models/tux/tux.obj");
            n.setTexture("models/ske/ske.tga");
            n.setModel("models/ske/ske.obj");
        } else if (rand < 0.1) {
            n = new Bomb();
            n.setTexture("models/bomb/bomb.tga");
            n.setModel("models/bomb/bomb.obj");
        } else {
            n = new EnvPhysicsNode();
            n.setModel("models/cube/cube.obj");
            n.setTexture("models/cube/cube.tga");
        }

        n.setMass(0.2);
        n.setScale(1);
        n.setGravity(0, -0.1f, 0);
        n.setCollisionShape(new BoxCollisionShape(new Vector3f(1f, 1f, 1f)));
        n.setFriction(0.5f);
        n.setLocation(x, y, z);
        nodes.add(n);
        env.addObject(n);

    }
    private void reset() {
        for (EnvNode n : nodes) {
            env.removeObject(n);
        }
        nodes.clear();

        double x = 5+Math.random()*25;
        double z = 5;
        double y = 3+Math.random()*10;
        for (int i=0; i<4; i++) {
            x = 5+Math.random()*25;
            z+= 20;
            y = 3+Math.random()*10;
            makePyrimad(x, y, z);
        }
    }
    private void shootBall(double x, double y, double z,
            double xv, double yv, double zv) {
        EnvPhysicsNode n = new EnvPhysicsNode();
        n.setX(x); n.setY(y); n.setZ(z);

        n.setMass(1);
        n.setLinearVelocity(xv, yv, zv);
        n.setGravity(0, 0.3f, 0);
        n.setRestitution(0.1f);
        n.setFriction(0.1f);
        nodes.add(n);
        env.addObject(n);
    }

    public static void main(String [] args) {
        (new QuakeTest()).play();
    }
}
