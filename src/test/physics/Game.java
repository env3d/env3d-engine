package test.physics;

import env3d.advanced.EnvPhysicsNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import env3d.advanced.EnvAbstractNode;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import env3d.advanced.EnvWater;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

public class Game extends env3d.EnvApplet {

    private EnvAdvanced env;
    private List<EnvAbstractNode> nodes = Collections.synchronizedList(new ArrayList<EnvAbstractNode>());
    private EnvTerrain terrain;
    private int strength = 0;
    private Church church;

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

//        EnvPhysicsNode terrain = new EnvPhysicsNode();
//
//        terrain.setModel("box 10 10 10");
//        terrain.setMass(0);
//        terrain.setTexture("textures/floor.png");
//        terrain.setRestitution(0.5f);
//        terrain.setScale(10);
//        terrain.setY(0);
//        terrain.setFriction(1);

        // All the textures required by the skybox
        String textureDir = "textures/skybox/default";

        // Create a the sky box.  The first 3 parameters specifies how far
        // the sky box extends in the x, y, and z direction.  This is for both
        // positive and negative direction on each axis.  The next 6 parameters
        // specifies the texture for each direction.
        //
        // Here, we create a skybox of size 256x256x256.  By default, the skybox's
        // center point is located at 0, 0, 0.
        EnvSkyRoom skyroom = new EnvSkyRoom(textureDir);

        // Move the skybox to the middle of the room
        skyroom.setXYZ(128, 128, 128);

        // EnvSkyRoom is a room, so we use env.setRoom() to set make it visible in our
        // environment.
        env.setRoom(skyroom);

        EnvWater water = new EnvWater();
        water.setRotateX(-90);
        water.setY(4);
        water.setX(-2500);
        water.setZ(2500);
        water.setWaveStrength(0.01);
        water.setWaterDepth(30);
        water.setScale(5000);
        env.addObject(water);

        terrain = new EnvTerrain("textures/terrain/termap1.png");
        terrain.setTexture("textures/mud.png");
        terrain.setScale(1, 0.1, 1);
//        terrain.getJme_node().getChild(0).addControl(new RigidBodyControl(0));
//        env.bulletAppState.getPhysicsSpace().addAll(terrain.getJme_node());
        env.addObject(new EnvPhysicsNode(terrain));

        church = new Church(terrain);
        env.addObject(church);

        env.setDisplayStr("+", 315, 245, 1, 1, 0, 0, 1);
        env.setDisplayStr("WASD to move, SPACE to fly up, C to fly down", 5, 80);
        env.setDisplayStr("1 to built tower, 2 to built wall, R to reset, Click to shoot", 5, 60);
        env.setDisplayStr("ESC to toggle Mouse Grab", 5, 40);

//        env.setCameraXYZ(40, terrain.getHeight(40, 120)+2, 120);
        env.setCameraXYZ(40, 30, 120);
        env.setCameraPitch(-5);
        env.setCameraYaw(20);
        env.setDefaultControl(true);
        //env.setMouseGrab(true);
        env.setShowStatus(true);
        reset();

        Thread t = new Thread() {

            @Override
            public void run() {
                while (true) {
                    if (env.isMouseGrabbed()) {
                        //firstPersonCamera();
                        synchronized (env) {
                            processInput();
                        }
                    }
                    try {
                        sleep(10);
                    } catch (Exception e) {
                    }
                }
            }
        };
        t.start();

        ArrayList<EnvAbstractNode> deletedNodes = new ArrayList<EnvAbstractNode>();
        ArrayList<EnvAbstractNode> newNodes = new ArrayList<EnvAbstractNode>();
        while (true) {
            //firstPersonCamera();
            if (env.getKey() == 1) {
                //env.exit();
                //env.setMouseGrab(!env.isMouseGrabbed());
                env.setDefaultControl(!env.isDefaultControl());
            }
            deletedNodes.clear();
            newNodes.clear();
            for (EnvAbstractNode n : nodes) {
                if (n instanceof Bomb) {
                    if (((Bomb) n).hasMoved()) {
                        //if (n.getRotateX() > 0.01 || n.getRotateY() > 0.01 || n.getRotateZ() > 0.01) {
                        //((EnvPhysicsNode)n).setCollisionShape(new BoxCollisionShape(new Vector3f(4,4,4)));
                        ((Bomb) n).explode();
                        newNodes.add(new Explosion(n.getX(), n.getY(), n.getZ()));
                        deletedNodes.add(n);

                    }
                }
                if (n instanceof Explosion) {
                    Explosion e = (Explosion) n;
                    if (e.isDead()) {
                        deletedNodes.add(e);
                    }
                }
            }
            if (deletedNodes.size() > 0) {
                // explosion effect
                for (int i = 0; i < 5; i++) {
                    env.advanceOneFrame();
                }
            }
            for (EnvAbstractNode n : deletedNodes) {
                env.removeObject(n);
                nodes.remove(n);
            }
            for (EnvAbstractNode n : newNodes) {
                env.addObject(n);
                nodes.add(n);
            }


            env.advanceOneFrame();
        }
    }

    private void firstPersonCamera() {
        double step = 0.2, camSpeed = 0.5;
        double new_x = 0, new_y = 0, new_z = 0;
        if (env.getKeyDown(Keyboard.KEY_W)) {
            new_x = env.getCameraX() - step * Math.sin(Math.toRadians(env.getCameraYaw()));
            new_z = env.getCameraZ() - step * Math.cos(Math.toRadians(env.getCameraYaw()));
        }

        if (env.getKeyDown(Keyboard.KEY_S)) {
            new_x = env.getCameraX() + step * Math.sin(Math.toRadians(env.getCameraYaw()));
            new_z = env.getCameraZ() + step * Math.cos(Math.toRadians(env.getCameraYaw()));
        }

        if (env.getKeyDown(Keyboard.KEY_A)) {
            new_x = env.getCameraX() - step * Math.cos(Math.toRadians(env.getCameraYaw()));
            new_z = env.getCameraZ() + step * Math.sin(Math.toRadians(env.getCameraYaw()));
        }
        if (env.getKeyDown(Keyboard.KEY_D)) {
            new_x = env.getCameraX() + step * Math.cos(Math.toRadians(env.getCameraYaw()));
            new_z = env.getCameraZ() - step * Math.sin(Math.toRadians(env.getCameraYaw()));
        }
        new_y = terrain.getHeight(new_x, new_z);

        if (env.getKeyDown(Keyboard.KEY_SPACE)) {
            env.setCameraXYZ(env.getCameraX(),
                    env.getCameraY() + step,
                    env.getCameraZ());
        }
        if (env.getKeyDown(Keyboard.KEY_C)) {
            env.setCameraXYZ(env.getCameraX(),
                    env.getCameraY() - step,
                    env.getCameraZ());
        }

        double newPitch = env.getCameraPitch() + camSpeed * env.getMouseDY();

        // Restrict the pitch to within a "reasonable" amount
        if (newPitch > 70) {
            env.setCameraPitch(70);
        } else if (newPitch < -50) {
            env.setCameraPitch(-50);
        } else {
            env.setCameraPitch(newPitch);
        }

        env.setCameraYaw(env.getCameraYaw() - camSpeed * env.getMouseDX());
        if (new_y > 0) {
            env.setCameraXYZ(new_x, new_y + 2, new_z);
        }
    }

    private void processInput() {

        if (env.getKey() == Keyboard.KEY_1) {
            double x = (env.getCameraX() - 2 * Math.sin(Math.toRadians(env.getCameraYaw())));
            double y = env.getCameraY();
            double z = (env.getCameraZ() - 2 * Math.cos(Math.toRadians(env.getCameraYaw())));

            makeTower(x, y, z);
        }
        if (env.getKey() == Keyboard.KEY_2) {
            double x = (env.getCameraX() - 2 * Math.sin(Math.toRadians(env.getCameraYaw())));
            double y = env.getCameraY();
            double z = (env.getCameraZ() - 2 * Math.cos(Math.toRadians(env.getCameraYaw())));

            makePyrimad(x, Math.max(y, 5), z);
        }
        if (env.getKey() == Keyboard.KEY_3) {
            double x = (env.getCameraX() - 2 * Math.sin(Math.toRadians(env.getCameraYaw())));
            double y = env.getCameraY();
            double z = (env.getCameraZ() - 2 * Math.cos(Math.toRadians(env.getCameraYaw())));

            Bomb n = new Bomb();
            n.setTexture("models/bomb/bomb.tga");
            n.setModel("models/bomb/bomb.obj");
            n.setMass(0.2);
            n.setScale(1);
            n.setGravity(0, -0.1f, 0);
            n.setCollisionShape(new BoxCollisionShape(new Vector3f(1f, 1f, 1f)));
            n.setFriction(0.5f);
            n.setLocation(x, y, z);
            nodes.add(n);
            env.addObject(n);
        }
        if (env.getKey() == Keyboard.KEY_R) {
            reset();
        }

        if (env.getMouseButtonDown(0)) {
            strength = strength > 100 ? 100 : strength + 5;
            env.setDisplayStr("Strength is " + strength);
        } else if (strength > 0) {
            double x = env.getCameraX();
            double y = env.getCameraY();
            double z = env.getCameraZ();
            double xv = -strength * Math.sin(Math.toRadians(env.getCameraYaw())) * Math.cos(Math.toRadians(env.getCameraPitch()));
            double yv = strength * Math.sin(Math.toRadians(env.getCameraPitch()));
            double zv = -strength * Math.cos(Math.toRadians(env.getCameraYaw())) * Math.cos(Math.toRadians(env.getCameraPitch()));
            shootBall(x, y, z, xv, yv, zv);
            strength = 0;
            env.setDisplayStr("");
        }

//        if (env.getMouseButtonDown(1)) {
//            Object pick =  env.getPick(env.getMouseX(), env.getMouseY());
//            if (pick instanceof EnvPhysicsNode) {
//                EnvPhysicsNode n = (EnvPhysicsNode) pick;
//                if (n.getModel().contains("bsc")) {
//                    if (n.getControl().getMass() > 0) {
//                        n.setMass(0);
//                        env.setDisplayStr("Mass is now 0");
//                    } else {
//                        n.setMass(20);
//                        env.setDisplayStr("Mass is now 20");
//                    }
//                }
//            }
//        }

        if (env.getKey() == Keyboard.KEY_M) {

            if (church.getControl().getMass() > 0) {
                church.setMass(0);
                church.setCollisionShape(new BoxCollisionShape(new Vector3f(11, 10, 10)));

                env.setDisplayStr("Mass is now 0");
            } else {
                church.setMass(30);
                church.setCollisionShape(new BoxCollisionShape(new Vector3f(11, 10, 10)));
                env.setDisplayStr("Mass is now 30");
            }
        }

        if (env.getKey() == Keyboard.KEY_X) {
            env.setShadowEnabled(!env.isShadowEnabled());
        }
    }

    private void makeTower(double x, double height, double z) {
        double baseHeight = terrain.getHeight(x, z);
        for (double y = baseHeight + 1; y < height; y += 2) {
//        for (int y = 1; y < height; y+=2) {
            makeBlock(x, y, z);
            //env.advanceOneFrame();
        }
    }

    private void makePyrimad(double x, double height, double z) {
        double baseHeight = terrain.getHeight(x, z);
        for (double y = baseHeight + 1; y < height; y += 2) {
//        for (int y = 1; y < height; y+=2) {
            for (int row = 0; row < (height - baseHeight); row += 2) {
                makeBlock(x + row, y, z);
                //env.advanceOneFrame();
            }
        }
    }

    private void makeBlock(double x, double y, double z) {
        EnvPhysicsNode n;

        double rand = Math.random();
        if (rand < 0.05) {
            n = new EnvPhysicsNode();
            n.setTexture("models/ske/ske.tga");
            n.setModel("models/ske/ske.obj");
        } else if (rand < 0.1) {
            n = new Bomb();
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
        synchronized (env) {
            for (EnvAbstractNode n : nodes) {
                env.removeObject(n);
            }
            nodes.clear();

            double x = -25 + Math.random() * 25;
            double z = -25;
            double y = 3 + Math.random() * 10;
            for (int i = 0; i < 4; i++) {
                x = 10 + Math.random() * 25;
                z += 20;
                y = terrain.getHeight(x, z) + (3 + Math.random() * 10);
                makePyrimad(x, y, z);
            }
            church.reset();
        }

    }

    private void shootBall(double x, double y, double z,
            double xv, double yv, double zv) {
        Ball b = new Ball(x, y, z, xv, yv, zv);
        nodes.add(b);
        env.addObject(b);
    }

    public static void main(String[] args) {
        (new Game()).play();
    }
}
