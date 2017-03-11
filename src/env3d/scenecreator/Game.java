/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.scenecreator;

import bluej.env3d.GameBObjectAdapter;
import bluej.extensions.BObject;
import env3d.Env;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvNode;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JMenuItem;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author jmadar
 */
public class Game {

    private Env env;
    private boolean control = false;
    private SceneNode activeObject;
    private double activeObjectDist;
    // A list to keep track of bluej specific objects
    private HashSet<SceneNode> nodes = new HashSet<SceneNode>();
    private HashMap<BObject, GameBObjectAdapter> bluejObj = new HashMap<BObject, GameBObjectAdapter>();
    private String skyBoxDir;
    private String terrainHeightMap = "textures/terrain/flat.png",
            terrainAlphaMap = "textures/terrain/alpha.png";
    private EnvTerrain terrain;
    private boolean finished;
    // Save the properties of last scene node of a particular class that was placed.
    private HashMap<String, String> sceneNodeProperties = new HashMap<String, String>();
    private UI ui;

    public Game(UI ui) {
        this.ui = ui;
        if (ui != null && ui.isSceneCreatorMode()) {
            env = new EnvAdvanced();
        } else {
            env = new Env();
        }
    }

    public void addAndPlaceObject(SceneNode n) {
        addObject(n);
        setActiveObject(n);
        // use the last distance as the activeObjectDist
        try {
            activeObjectDist = Double.parseDouble(sceneNodeProperties.get(n.getName()).split(",")[10]);
        } catch (Exception e) {
            // Just use the default
        }
    }

    public void play() {

        env.setDefaultControl(control);

        while (!finished) {

            if (!isEditMode()) {
                if (env.getMouseButtonClicked() == 0) {
                    // Select the active object if the user clicks on one
                    Object o = env.getPick(env.getMouseX(), env.getMouseY());
                    if (o instanceof EnvNode && nodes.contains(o)) {
                        setActiveObject((SceneNode) o);
                    }
                    setEditMode(true);

                }

                // Right click would bring up text editor
                if (env.getMouseButtonClicked() == 1) {
                    ui.save(save());

                    // Pop up context menu
                    final ContextMenu menu = new ContextMenu(this);
                    Point menuLocation = MouseInfo.getPointerInfo().getLocation();
                    menu.setLocation(menuLocation);

                    // Determine if the user has picked an object
                    Object o = env.getPick(env.getMouseX(), env.getMouseY());
                    if (ui.getMode() == UI.Mode.PYTHON) {
                        final UI _ui = this.ui;
                        if (o instanceof SceneNode && nodes.contains(o)) {
                            // Right clicked on a node, edit the node file
                            final SceneNode n = (SceneNode) o;
                            // Add a new menu item for editing class
                            menu.add(new JMenuItem("Edit class " + n.getName()) {
                                {
                                    addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent ae) {
                                            menu.setVisible(false);
                                            SCTextEditor editor = new SCTextEditor(_ui.getFrame(), _ui.getAssetDir(), n.getName(), false);
                                            _ui.getEditors().add(editor);
                                        }
                                    });
                                }
                            });
                        } else {
                            menu.add(new JMenuItem("Edit World Code") {
                                {
                                    addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent ae) {
                                            menu.setVisible(false);
                                            SCTextEditor editor = new SCTextEditor(_ui.getFrame(), _ui.getAssetDir(), "World", false);
                                            _ui.getEditors().add(editor);

                                        }
                                    });
                                }
                            });
                        }
                    }

                    menu.setVisible(true);
                }

                // In visualization mode, escape would exit
                if ((ui != null && !ui.isSceneCreatorMode()) && env.getKey() == 1) {
                    finished = true;
                }
            } else {
                objectEditMode();
            }

            // call the move method 
//            for (GameBObjectAdapter obj : bluejObj.values()) {
//                obj.callMoveFromObject();
//            }

            switch (env.getKey()) {
                case Keyboard.KEY_ESCAPE:
                    setEditMode(false);
                    break;
            }


            env.advanceOneFrame(30);
        }
    }

    /**
     * Allow objects to be selected and moved.
     */
    private void objectEditMode() {
        if (env.getMouseButtonClicked() == 0) {

            if (activeObject == null) {
                // Select the object
                Object o = env.getPick(320, 240);
                if (o == null) {
                    // Add the current select object
                    SceneNode n = ui.createSceneNode();
                    if (n != null) {
                        addAndPlaceObject(n);
                    }
                } else {
                    if (nodes.contains(o) && o instanceof SceneNode) {
                        // Something is picked
                        setActiveObject((SceneNode) o);
                    }
                }
            } else {
                // place the object
                activeObject.setWireFrame(false);
                sceneNodeProperties.put(activeObject.getName(), activeObject.toSaveString() + "," + activeObjectDist);
                unSetActiveObject();
                //setEditMode(false);
            }
        }

        if (activeObject != null) {
            double xv = (int) (getEnv().getCameraX() - activeObjectDist * Math.sin(Math.toRadians(getEnv().getCameraYaw())) * Math.cos(Math.toRadians(getEnv().getCameraPitch())));
            double yv = (int) (getEnv().getCameraY() + activeObjectDist * Math.sin(Math.toRadians(getEnv().getCameraPitch())));
            double zv = (int) (getEnv().getCameraZ() - activeObjectDist * Math.cos(Math.toRadians(getEnv().getCameraYaw())) * Math.cos(Math.toRadians(getEnv().getCameraPitch())));

            // move the object along with the camera
            activeObject.setX(xv);
            activeObject.setY(yv);
            activeObject.setZ(zv);
            double angle = Math.atan2(xv - env.getCameraX(), zv - env.getCameraZ());

            switch (env.getKey()) {
                case Keyboard.KEY_UP:
                    activeObject.setScale(activeObject.getScale() + 0.1);
                    break;
                case Keyboard.KEY_DOWN:
                    activeObject.setScale(activeObject.getScale() - 0.1);
                    break;
                case Keyboard.KEY_LEFT:
                    activeObject.setRotateY(activeObject.getRotateY() - 90);
                    break;
                case Keyboard.KEY_RIGHT:
                    activeObject.setRotateY(activeObject.getRotateY() + 90);
                    break;
                case Keyboard.KEY_DELETE:
                    nodes.remove(activeObject);
                    env.removeObject(activeObject);
                    unSetActiveObject();
                    break;
                case Keyboard.KEY_ESCAPE:
                    ((SceneNode) activeObject).setWireFrame(false);
                    unSetActiveObject();
                    break;
            }

        }
    }

    public Env getEnv() {
        return env;
    }

    /**
     * @return the activeObject
     */
    public EnvNode getActiveObject() {
        return activeObject;
    }

    /**
     * @param activeObject the activeObject to set
     */
    public void setActiveObject(SceneNode activeObject) {
        this.activeObject = activeObject;
        activeObjectDist = activeObject.distance(env.getCameraX(), env.getCameraY(), env.getCameraZ());
        this.activeObject.setWireFrame(true);
        String activeObjectProperty = sceneNodeProperties.get(this.activeObject.getName());
        if (activeObjectProperty != null) {
            String[] prop = activeObjectProperty.split(",");

            this.activeObject.setScale(Double.parseDouble(prop[3]));
            this.activeObject.setRotateX(Double.parseDouble(prop[4]));
            this.activeObject.setRotateY(Double.parseDouble(prop[5]));
            this.activeObject.setRotateZ(Double.parseDouble(prop[6]));
        }
        env.setDisplayStr("DELETE to delete object", 200, 290, 1, 1, 1, 1, 0.5);
        env.setDisplayStr("UP and DOWN arrows to scale", 200, 270, 1, 1, 1, 1, 0.5);
        env.setDisplayStr("LEFT and RIGHT arrows to rotate", 200, 220, 1, 1, 1, 1, 0.5);
    }

    /**
     * Sets the active object to null
     */
    public void unSetActiveObject() {
        activeObject = null;
        env.setDisplayStr(null, 200, 290);
        env.setDisplayStr(null, 200, 270);
        env.setDisplayStr(null, 200, 220);
    }

    public void addObject(SceneNode node) {

        //System.out.println("Node at "+node.getJme_node().getLocalTranslation());

        nodes.add(node);
        env.addObject(node);
    }

    /**
     * Adds a bluej object.
     * @param obj 
     */
    public void addObject(BObject obj) {
        if (bluejObj.get(obj) == null) {
            bluejObj.put(obj, new GameBObjectAdapter(obj));
            env.addObject(bluejObj.get(obj));
        }
    }
    
    public void updateBObjects() {
        bluejObj.values().stream().forEach((o) -> {
            o.update();
        });
    }

    public void removeObject(BObject obj) {
        if (bluejObj.get(obj) != null) {
            bluejObj.remove(obj);
            env.removeObject(obj);
        }
    }

    public void setTerrainTexture(String texture) {
        if (texture == null) {
            //env.removeObject(terrain);
            terrain = null;
        } else {
            if (terrain == null) {
                // Use a flat terrain for now.
                terrain = new EnvTerrain("textures/terrain/flat.png");
                env.addObject(terrain);
            } else {
                //terrain.updateHeightMap("textures/terrain/flat.png");
            }
            terrain.setTexture(texture);
        }
    }

    public void setTerrainTexture(String textureR, String textureG, String textureB) {
        setTerrainTexture(textureR);
        terrain.setTexture(textureG, 2);
        terrain.setTexture(textureB, 3);
    }

    public void setRoom(String dir) {
        skyBoxDir = dir;

        env.setRoom(new EnvSkyRoom(skyBoxDir));
        if (terrain != null) {
            env.addObject(terrain);
        }
        for (EnvNode node : nodes) {
            env.addObject(node);
        }

        for (GameBObjectAdapter obj : bluejObj.values()) {
            env.addObject(obj);
        }
    }

    public void setEditMode(boolean editMode) {
        control = editMode;
        env.setDefaultControl(control);
        // create the mouse pick cursor
        if (isEditMode()) {
            // create the mouse pick cursor
            if (ui.isSceneCreatorMode()) {
                env.setDisplayStr("Press ESCAPE to exit Edit Mode", 5, 475);
            } else {
                env.setDisplayStr("Press ESCAPE to exit FPS Mode", 5, 475);
            }
            env.setDisplayStr("+", 315, 245);
        } else {
            env.setDisplayStr(null, 5, 475);
            env.setDisplayStr(null, 315, 245);
        }

    }

    public boolean isEditMode() {
        return control;
    }

    /**
     * Build a string that represents the current game
     * @return 
     */
    public String save() {
        StringBuilder sb = new StringBuilder();

        // The background
        sb.append("skybox,").append(skyBoxDir).append("\n");

        // The camera position
        sb.append("camera,").append(env.getCameraX()).append(",").append(env.getCameraY()).append(",").append(env.getCameraZ()).append(",").append(env.getCameraYaw()).append(",").append(env.getCameraPitch()).append("\n");

        // The terrain
        if (terrain != null) {
            sb.append("terrain,").append(terrain.getHeightMapFile()).append(",").append(terrain.getAlphaMapFile()).append(",").append(terrain.getTexture()).append(",").append(terrain.getTextureG()).append(",").append(terrain.getTextureB()).append("\n");
        }

        // All the objects
        for (SceneNode n : nodes) {
            sb.append("object,").append(n.toSaveString()).append("\n");
        }

        return sb.toString();
    }

    public void clearNodes() {
        nodes.clear();
    }

    public UI getUI() {
        return ui;
    }

    public EnvTerrain getEnvTerrain() {
        return terrain;
    }

    public void exit() {
        finished = true;
    }
}
