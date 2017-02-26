package env3d;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.material.Material;
import com.jme3.material.MaterialList;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.OgreMeshKey;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Write a description of class GameObjectAdapter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GameObjectAdapter {

    // The assetManager is set by the system before the creation of any GameObjects
    public static AssetManager assetManager;
    public static HashSet<String> locator = new HashSet<String>();
    // Non-static fields
    private static final Logger logger = Logger.getLogger(GameObjectAdapter.class.getName());
    protected Object internalObject;
    protected Node jme_node = new Node(this.toString());
    protected Quaternion q, qX, qY, qZ;
    protected String currentModel = "", currentTexture = "", currentAction = "";
    protected Material mat, mat_lit;
    protected HashMap<String, Spatial> objectCache = new HashMap<String, Spatial>();
    
    protected boolean lighting = false;

    public GameObjectAdapter() {
        q = new Quaternion();
        qX = new Quaternion();
        qY = new Quaternion();
        qZ = new Quaternion();
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        mat_lit = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
//        mat_lit.setBoolean("UseMaterialColors", true);
//        mat_lit.setBoolean("UseAlpha", false);
        mat_lit.setColor("Diffuse", new ColorRGBA(0.2f, 0.2f, 0.2f, 1));
        mat_lit.setColor("Ambient", new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
        mat_lit.setColor("Specular", new ColorRGBA(0.2f, 0.2f, 0.2f, 1));
        mat_lit.setFloat("Shininess", 0.1f);
        mat_lit.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
    }

    public GameObjectAdapter(Object obj) {
        this();
        internalObject = obj;
    }

    public void update() {

        float x = castToFloat(getFieldFromObject("x"));
        float y = castToFloat(getFieldFromObject("y"));
        float z = castToFloat(getFieldFromObject("z"));
        float rotateX = castToFloat(getFieldFromObject("rotateX"));
        float rotateY = castToFloat(getFieldFromObject("rotateY"));
        float rotateZ = castToFloat(getFieldFromObject("rotateZ"));
        float scale = castToFloat(getFieldFromObject("scale"));

        Boolean transparent = getFieldFromObject("transparent");

        qY.fromAngleNormalAxis((float) Math.toRadians(rotateY), Vector3f.UNIT_Y);
        qX.fromAngleNormalAxis((float) Math.toRadians(rotateX), Vector3f.UNIT_X);
        qZ.fromAngleNormalAxis((float) Math.toRadians(rotateZ), Vector3f.UNIT_Z);

        String modelFile = getFieldFromObject("model");
        boolean modelChanged = false;
        Spatial modelSpatial = null;
        if (modelFile == null || modelFile.length() == 0) {
            if (jme_node != null && jme_node.getChildren() != null && jme_node.getChildren().size() > 0) {
                jme_node.detachAllChildren();
            }
        } else if (!currentModel.equals(modelFile)) {
            modelChanged = true;
            currentModel = modelFile;
            if (jme_node != null && jme_node.getChildren() != null && jme_node.getChildren().size() > 0) {
                jme_node.detachAllChildren();
            }
            try {
                if (currentModel.equals("sphere")) {
                    Sphere sphere = new Sphere(16, 16, 1);
                    sphere.setTextureMode(Sphere.TextureMode.Projected);
                    modelSpatial = new Geometry(this.toString(), sphere);

                    //jme_node.attachChild(s);
                    modelSpatial.getLocalRotation().fromAngles(0, (float) Math.PI / 2, 0).multLocal((float) Math.sqrt(0.5), 0, 0, (float) Math.sqrt(0.5) * -1);
                } else if (currentModel.startsWith("box")) {
                    String[] params = currentModel.split(" ");
                    int width = 1, height = 1;
                    if (params.length > 3) {
                        width = Integer.parseInt(params[1]);
                        height = Integer.parseInt(params[2]);
                    }
                    //model = new Box("box", new Vector3f(0, 0, 0), scale, scale, scale);
                    modelSpatial = drawSquare(width, height);
                    ((Geometry) modelSpatial).getMesh().scaleTextureCoordinates(new Vector2f(width, height));
                    if (params[0].equals("box")) {
                        modelSpatial.getLocalRotation().fromAngleNormalAxis((float) (-1 * Math.PI / 2), Vector3f.UNIT_X);
                        modelSpatial.setLocalTranslation(0, 0, height);
                    } else if (params[0].equals("box_north")) {
                        // no need to rotate
                    }
                    //jme_node.attachChild(b);
                } else {
                    modelFile = registerLocator(modelFile);
                    String path = modelFile.substring(0, modelFile.lastIndexOf("/"));
                    try {
                        modelSpatial = objectCache.get(this.toString() + ":" + modelFile);
                        if (modelSpatial == null) {
                            if (modelFile.endsWith("xml")) {
                                MaterialList matList = (MaterialList) assetManager.loadAsset(path+"/Scene.material");
                                OgreMeshKey key = new OgreMeshKey(modelFile, matList);
                                modelSpatial = assetManager.loadAsset(key);
                            } else {
                                modelSpatial = assetManager.loadModel(modelFile);
                                modelSpatial.setName(this.toString() + ":" + modelFile);
                            }
                            objectCache.put(modelSpatial.getName(), modelSpatial);
                        }

                        //jme_node.attachChild(s);
                    } catch (java.lang.StringIndexOutOfBoundsException e) {
                        System.out.println("Error name:" + modelFile);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        jme_node.setLocalTranslation(x, y, z);
        jme_node.setLocalScale(scale);
        jme_node.setLocalRotation(qY.mult(qX.mult(qZ, q), q));

        String textureFile = getFieldFromObject("texture");
        if (modelChanged || (currentTexture != null && !currentTexture.equals(textureFile))) {
            currentTexture = textureFile;            
            if (textureFile != null && textureFile.length() > 0) {
                if (modelChanged) jme_node.attachChild(modelSpatial);
                textureFile = registerLocator(textureFile);
                Texture texture = assetManager.loadTexture(textureFile);
                texture.setWrap(Texture.WrapMode.Repeat);
                mat.setTexture("ColorMap", texture);
                mat_lit.setTexture("DiffuseMap", texture);
                if (transparent) {
                    mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
                    mat_lit.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
                    jme_node.setQueueBucket(Bucket.Transparent);
                } else {
                    mat.getAdditionalRenderState().setBlendMode(BlendMode.Off);
                    mat_lit.getAdditionalRenderState().setBlendMode(BlendMode.Off);
                    jme_node.setQueueBucket(Bucket.Opaque);                    
                }
                if (lighting) {
                    jme_node.setMaterial(mat_lit);
                } else {
                    jme_node.setMaterial(mat);
                }
            } else {
                if (textureFile == null) {
                    jme_node.setMaterial(null);
                } else {
                    mat.setColor("Color", ColorRGBA.White);
                    jme_node.setMaterial(mat);
                }
                jme_node.attachChild(modelSpatial);
//                if (!(modelSpatial instanceof Geometry)) {
//                    System.out.println("Attaching model file " + modelSpatial);
//                } else {
//                }
            }
        }

//        if (modelSpatial != null)
//            jme_node.attachChild(modelSpatial);

    }

    protected String registerLocator(String asset) {
        if (asset != null) {            
            if (asset.startsWith("http")) {
                try {
                    URL url = new URL(asset);

                    String urlPath = url.getProtocol() + "://" + url.getHost();
                    //asset.substring(0, asset.indexOf("/"));
                    if (!locator.contains(urlPath)) {
                        locator.add(urlPath);
                        logger.log(Level.INFO, "Registering asset {0}", urlPath);
                        assetManager.registerLocator(urlPath, UrlLocator.class);
                    }
                    // Return only the file name
                    return url.getFile();
                    //asset.substring(asset.lastIndexOf("/"));

                } catch (Exception e) {
                }
            } else if (asset.matches("^[a-zA-Z]:.*")) {
                // Dos style drive letter.  Have to register each drive letter.
                String driveLetter = asset.substring(0, 2);
                String fileName = asset.substring(2);
                if (!locator.contains(driveLetter)) {
                    locator.add(driveLetter);
                    logger.log(Level.INFO, "Registering asset {0}", driveLetter);                    
                    assetManager.registerLocator(driveLetter, FileLocator.class);
                }
                return fileName;
            }
        }
        return asset;
    }

    // Generic method to get a field value.  Subclass can over-ride this method to wrap a different object type.
    @SuppressWarnings("unchecked")
    protected <T> T getFieldFromObject(String fname) {
        T fieldValue = null;
        Class c = internalObject.getClass();
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
        // Should have found the field by now

        if (field != null) {
            try {
                field.setAccessible(true);
                fieldValue = (T) field.get(internalObject);
            } catch (Exception e) {
                System.out.println("Error " + e);
                System.exit(1);
            }
        } else {
            // The defaults
            if (fname.equalsIgnoreCase("texture")) {
                fieldValue = (T) ("textures"+"/"+"earth.png");
            } else if (fname.equalsIgnoreCase("model")) {
                fieldValue = (T) "sphere";
            } else if (fname.equalsIgnoreCase("textureNormal")) {
                fieldValue = null;
            } else if (fname.equalsIgnoreCase("action")) {
                fieldValue = null;
            } else if (fname.equalsIgnoreCase("repeat")) {
                fieldValue = null;
            } else if (fname.equalsIgnoreCase("scale")) {
                fieldValue = (T) new Float(1);
            } else if (fname.equalsIgnoreCase("transparent")) {
                fieldValue = (T) (Boolean) false;
            } else {
                fieldValue = (T) new Float(0);
            }
        }
        return fieldValue;
    }

    public void useLightingMaterial(boolean lighting) {
        this.lighting = lighting;
    }
    
    private float castToFloat(Object obj) {
        float val;
        try {
            val = (Float) obj;
        } catch (java.lang.ClassCastException e) {
            val = ((Double) obj).floatValue();
        }
        return val;
    }

    /**
     * @return the jme_object
     */
    public Node getJme_node() {
        return jme_node;
    }

    public static Geometry drawSquare() {
        return drawSquare(1, 1);
    }

    /**
     * Draw a simple square.  Used in room and lots of other things ;)
     * @return
     */
    public static Geometry drawSquare(int width, int height) {
        Geometry geom = new Geometry("Wall", new Quad(width, height));
        return geom;
    }
}
