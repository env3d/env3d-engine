/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.physics;

import env3d.advanced.EnvPhysicsNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvNode;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author jmadar
 */
public class GameBilliard {
    private EnvAdvanced env;
    private ArrayList<EnvNode> nodes = new ArrayList<EnvNode>();

    public void play() {
        env = new EnvAdvanced();
        env.setPhysicsEnabled(true);
        Node rootNode = env.getRootNode();


        EnvPhysicsNode gameLevelNode = new EnvPhysicsNode();
        gameLevelNode.setScale(20);
        gameLevelNode.setRestitution(0);
        gameLevelNode.setFriction(0.35);
        gameLevelNode.setTexture(null);
        gameLevelNode.setModel("/Users/jmadar/Documents/models/billiard/billiards_0.obj");
        env.addObject(gameLevelNode);


        env.addObject(gameLevelNode);
        env.setCameraXYZ(0, 10, 60);
        double strength = 0;
        ArrayList<EnvNode> deletedNodes = new ArrayList<EnvNode>();
        env.setCameraPitch(0);
        env.setCameraYaw(0);
        env.setDefaultControl(true);
        env.setMouseGrab(true);
        env.setShowStatus(true);
        env.enableLighting();


        while (true) {
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
                strength = strength > 50 ? 50 : strength + 1;
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
        n.setFriction(0.1f);
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
        n.getControl().setCollisionShape(new SphereCollisionShape(0.7f));
        n.setX(x); n.setY(y); n.setZ(z);
        n.setScale(0.7);
        n.setMass(5);
        n.setLinearVelocity(xv, yv, zv);
        n.setGravity(0, 1f, 0);
        n.setRestitution(0.5);
        n.setFriction(0.1f);
        nodes.add(n);
        env.addObject(n);
    }

    public static void main(String [] args) {
        (new GameBilliard()).play();
    }
}
