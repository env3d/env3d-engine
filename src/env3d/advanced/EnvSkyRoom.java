/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.advanced;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import env3d.RoomAdapter;
import env3d.util.Sysutil;

/**
 * Creates a sky box
 * @author jmadar
 */
public class EnvSkyRoom extends RoomAdapter {

    private Spatial skyBox;
    private double width, height, depth;

    /**
     * Constructs a skybox from the directory.
     * Assumes that the following six files are present inside that
     * directory:
     *   top.png, bottom.png, north.png, east.png, south.png, west.png
     *
     * @param skyRoomDirectory
     */
    public EnvSkyRoom(String skyRoomDirectory) {
        // Strip last "/" character
        if (skyRoomDirectory.lastIndexOf("/") == skyRoomDirectory.length()-1) {
            skyRoomDirectory = skyRoomDirectory.substring(0, skyRoomDirectory.length()-1);
        }    
        CreateSkyRoom(500,500,500,
                skyRoomDirectory + "/north.png",
                skyRoomDirectory + "/east.png",
                skyRoomDirectory + "/south.png",
                skyRoomDirectory + "/west.png",
                skyRoomDirectory + "/top.png",
                skyRoomDirectory + "/bottom.png");
    }

    /**
     * Constructor for the SkyRoom.  Must provide all the necessary information
     * and cannot be changed after it has been created.
     *
     * @param north
     * @param east
     * @param south
     * @param west
     * @param top
     * @param bottom
     */
    public EnvSkyRoom(String north, String east, String south, String west, String top, String bottom) {
        CreateSkyRoom(500, 500, 500, north, east, south, west, top, bottom);
    }

    @Deprecated
    /**
     * Constructor for the SkyRoom.  Must provide all the necessary information
     * and cannot be changed after it has been created.
     * 
     * @param width
     * @param height
     * @param depth
     * @param north
     * @param east
     * @param south
     * @param west
     * @param top
     * @param bottom
     */
    public EnvSkyRoom(double width, double height, double depth,
            String north, String east, String south, String west, String top, String bottom) {
        
        CreateSkyRoom(width, height, depth, north, east, south, west, top, bottom);
    }

    
    private void CreateSkyRoom(double width, double height, double depth,
            String north, String east, String south, String west, String top, String bottom) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        north = registerLocator(north);
        east = registerLocator(east);
        south = registerLocator(south);
        west = registerLocator(west);
        top = registerLocator(top);
        bottom = registerLocator(bottom);

        skyBox = SkyFactory.createSky(assetManager,
                assetManager.loadTexture(west),
                assetManager.loadTexture(east),
                assetManager.loadTexture(north),
                assetManager.loadTexture(south),
                assetManager.loadTexture(top),
                assetManager.loadTexture(bottom));
        //new Vector3f((float)width, (float)height, (float)depth));
        skyBox.setLocalScale(100);        
        jme_node.setCullHint(Spatial.CullHint.Never);
        jme_node.attachChild(skyBox);
    }
    
    public void setXYZ(double x, double y, double z) {
        skyBox.setLocalTranslation((float) x, (float) y, (float) z);
        //skyBox.updateRenderState();
    }

    /**
     * @return the skyBox
     */
    public Spatial getJmeSkybox() {
        return skyBox;
    }

    /**
     * Set the background color
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public void setBackgroundColor(double r, double g, double b, double a) {
        super.setBackgroundColor(new ColorRGBA((float) r, (float) g, (float) b, (float) a));
    }

    /**
     *
     * @return the width of the room
     */
    public double getWidth() {
        return width;
    }

    /**
     *
     * @return the height of the room
     */
    public double getHeight() {
        return height;
    }

    /**
     *
     * @return the depth of the room
     */
    public double getDepth() {
        return depth;
    }
}
