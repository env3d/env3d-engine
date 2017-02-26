/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import java.io.File;

import java.lang.reflect.Field;

/**
 *
 * @author jmadar
 */
public class RoomAdapter extends GameObjectAdapter {

    private String textureBottom, textureTop, textureNorth, textureEast, textureSouth, textureWest;
    private float r, g, b;
    private float width, height, depth;
    private Object room;
    private ColorRGBA backgroundColor;

    /**
     * For internal use only
     */
    public RoomAdapter() {
        
    }

    public RoomAdapter(Object room) {
        this.room = room;

        // Get the dimensions of the room from the room object
        try {
            Field x = getFieldFromRoom("width");
            Field y = getFieldFromRoom("height");
            Field z = getFieldFromRoom("depth");
            x.setAccessible(true);
            y.setAccessible(true);
            z.setAccessible(true);

            width = (float) x.getDouble(room);
            height = (float) y.getDouble(room);
            depth = (float) z.getDouble(room);

        } catch (Exception e) {
            System.out.println("Error :" + e);
            width = 10;
            height = 10;
            depth = 10;
        }

        // Get the dimensions of the room from the room object
        try {
            Field rf = getFieldFromRoom("bgRed");
            Field gf = getFieldFromRoom("bgGreen");
            Field bf = getFieldFromRoom("bgBlue");
            rf.setAccessible(true);
            gf.setAccessible(true);
            bf.setAccessible(true);
            r = (float) rf.getDouble(room);
            g = (float) gf.getDouble(room);
            b = (float) bf.getDouble(room);

        } catch (Exception e) {
            r = 0;
            g = 0;
            b = 0;
        }
        backgroundColor = new ColorRGBA(r,g,b,1);
        
        // Get the textures of the room

        textureTop = getTextureFromRoom("Top");
        textureBottom = getTextureFromRoom("Bottom");
        textureNorth = getTextureFromRoom("North");
        textureEast = getTextureFromRoom("East");
        textureSouth = getTextureFromRoom("South");
        textureWest = getTextureFromRoom("West");

        // Create the trimeshes and add to the node

        Geometry northWall = drawWall("North");
        if (northWall != null) {
            northWall.setLocalScale(new Vector3f(width, height, 1));
            jme_node.attachChild(northWall);
        }
        
        Geometry southWall = drawWall("South");
        if (southWall != null) {
            southWall.setLocalTranslation(width,0,depth);
            southWall.getLocalRotation().fromAngleNormalAxis((float) Math.toRadians(180), Vector3f.UNIT_Y);
            southWall.setLocalScale(new Vector3f(width, height, 1));
            jme_node.attachChild(southWall);
        }

        Geometry eastWall = drawWall("East");
        if (eastWall != null) {
            eastWall.setLocalTranslation(width,0,0);
            eastWall.setLocalScale(new Vector3f(depth, height, 1));
            eastWall.getLocalRotation().fromAngleNormalAxis((float) Math.toRadians(-90), Vector3f.UNIT_Y);
            jme_node.attachChild(eastWall);
        }

        Geometry westWall = drawWall("West");
        if (westWall != null) {
            westWall.setLocalTranslation(0,0,depth);
            westWall.setLocalScale(new Vector3f(depth, height, 1));
            westWall.getLocalRotation().fromAngleNormalAxis((float) Math.toRadians(90), Vector3f.UNIT_Y);
            jme_node.attachChild(westWall);
        }

        Geometry topWall = drawWall("Top");
        if (topWall != null) {
            topWall.setLocalTranslation(0,height,0);
            topWall.setLocalScale(new Vector3f(width, depth, 1));
            topWall.getLocalRotation().fromAngleNormalAxis((float) Math.toRadians(90), Vector3f.UNIT_X);
            jme_node.attachChild(topWall);
        }

        Geometry bottomWall = drawWall("Bottom");
        if (bottomWall != null) {
            bottomWall.setLocalTranslation(0,0,depth);
            bottomWall.setLocalScale(new Vector3f(width, depth, 1));
            bottomWall.getLocalRotation().fromAngleNormalAxis((float) Math.toRadians(-90), Vector3f.UNIT_X);
            jme_node.attachChild(bottomWall);
        }


    }

    private Geometry drawWall(String direction) {

        String textureFile = getTextureFromRoom(direction);
        if (textureFile == null)
            return null;
        
        // Make sure that we can find the file
        textureFile = registerLocator(textureFile);
        
        Geometry m = drawSquare();
        m.setName(direction+" Wall");
        Material mat = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture(textureFile));
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        m.setMaterial(mat);

        return m;
    }

    @Override
    public void useLightingMaterial(boolean lighting) {
        for (Spatial s : jme_node.getChildren()) {
            if (s.getName().contains("Wall")) {
                String textureFile = "";
                if (s.getName().startsWith("North")) {
                    textureFile = textureNorth;
                } else if (s.getName().startsWith("East")) {
                    textureFile = textureEast;
                } else if (s.getName().startsWith("South")) {
                    textureFile = textureSouth;
                } else if (s.getName().startsWith("West")) {
                    textureFile = textureWest;
                } else if (s.getName().startsWith("Top")) {
                    textureFile = textureTop;
                } else if (s.getName().startsWith("Bottom")) {
                    textureFile = textureBottom;
                }
                if (lighting) {
                    Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
                    mat.setBoolean("UseMaterialColors", true);
                    mat.setTexture("DiffuseMap", assetManager.loadTexture(textureFile));
                    mat.setColor("Diffuse", new ColorRGBA(0.9f, 0.9f, 0.9f, 1));
                    mat.setColor("Ambient", new ColorRGBA(0.7f, 0.7f, 0.7f, 1));
                    mat.setColor("Specular", new ColorRGBA(0.2f, 0.2f, 0.2f, 1));
                    mat.setFloat("Shininess", 0.1f);
                    mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
                    s.setMaterial(mat);                    
                } else {
                    Material mat = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    mat.setTexture("ColorMap", assetManager.loadTexture(textureFile));
                    mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);                
                    s.setMaterial(mat);
                }
            }
        }
    }    


    protected String getTextureFromRoom(String direction) {
        String textureFile;


        try {
            Field textureField = getFieldFromRoom("texture" + direction);
            textureField.setAccessible(true);
            textureFile = (String) textureField.get(room);
            if (textureFile == null)
                return null;

        } catch (Exception e) {
            // Default
            try {
                if (direction.equals("Top")) {
                    textureFile = "textures"+"/"+"fence0.png";
                } else if (direction.equals("Bottom")) {
                    textureFile = "textures"+"/"+"floor.png";
                } else {
                    textureFile = "textures"+"/"+"fence1.png";
                }

            } catch (Exception ex) {

                textureFile = "texture"+"/"+"fence1.png";

            }
        }

        return textureFile;
    }

    public Object getRoom() {
        return room;
    }

    // Generic method to get a field value.  Subclass can over-ride this method to wrap a different object type.
    // This is just a copy of the same method from GameObjectAdapter.  Need to refactor later.
    protected Field getFieldFromRoom(String fname) throws Exception {
        Class c = room.getClass();
        Field field = null;

        // Search for the field in the inheritance tree
        while (field == null && c != null) {
            try {
                field = c.getDeclaredField(fname);
            } catch (NoSuchFieldException e) {
                field = null;
                c = c.getSuperclass();
            }
        }
        // Such have found the field by now

        if (field != null) {
            return field;
        } else {
            // The defaults
            throw new Exception("Cannot find field " + fname);
        }
    }

    /**
     * @return the backgroundColor
     */
    public ColorRGBA getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(ColorRGBA backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
