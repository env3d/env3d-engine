package env3d.advanced;

import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

/**
 * EnvNode is a high performance version of EnvObject.  To use it,
 * an Env Object must have already been created.
 * @author Jmadar
 */
public class EnvNode extends EnvAbstractNode {

    private String texture, textureNormal, model;
    private boolean transparent;
    private Texture matTexture;

    /** Creates a new instance of EnvObject */
    public EnvNode() {
        transparent = false;

        //jme_node.setMaterial(mat);

        // Defaults
        jme_node.setLocalTranslation(Vector3f.ZERO);
        jme_node.setLocalScale(1);
        setTextureHelper("textures"+"/"+"earth.png");
        //setTextureHelper(null);
        setModelHelper("sphere");

    }

    private void setTextureHelper(String texture) {
        if (texture != null && (texture.length()==0 || texture.equalsIgnoreCase("null"))) {
            texture = null;
        }
        texture = registerLocator(texture);
        this.texture = texture;
        if (texture == null) {            
            jme_node.detachAllChildren();
            setModelHelper(model);
        } else {
            matTexture = assetManager.loadTexture(texture);
            matTexture.setWrap(Texture.WrapMode.Repeat);
            mat.setTexture("ColorMap", matTexture);
            mat_lit.setTexture("DiffuseMap", matTexture);
            if (jme_node.getChildren().size() > 0) {
                if (lighting) {
                    jme_node.getChild(0).setMaterial(mat_lit);
                } else {
                    jme_node.getChild(0).setMaterial(mat);
                }
            }
        }
    }

    private void setModelHelper(String model) {
        this.model = model;
        jme_node.detachAllChildren();
        if (model == null) return;

        try {
            if (model.equals("sphere")) {
                Sphere sphere = new Sphere(16, 16, 1);
                sphere.setTextureMode(Sphere.TextureMode.Projected);
                Geometry s = new Geometry(this.toString(), sphere);

                jme_node.attachChild(s);
                s.getLocalRotation().fromAngles(0, (float) Math.PI / 2, 0).multLocal((float) Math.sqrt(0.5), 0, 0, (float) Math.sqrt(0.5) * -1);
            } else if (model.startsWith("box")) {
                String[] params = model.split(" ");
                int width = 1, height = 1;
                if (params.length > 3) {
                    width = Integer.parseInt(params[1]);
                    height = Integer.parseInt(params[2]);
                }
                //model = new Box("box", new Vector3f(0, 0, 0), scale, scale, scale);
                Geometry b = drawSquare(width, height);
                b.getMesh().scaleTextureCoordinates(new Vector2f(width, height));
                if (params[0].equals("box")) {
                    b.getLocalRotation().fromAngleNormalAxis((float) (-1 * Math.PI / 2), Vector3f.UNIT_X);
                    b.setLocalTranslation(0, 0, height);
                } else if (params[0].equals("box_north")) {
                    // no need to rotate
                }
                jme_node.attachChild(b);
            } else {
                model = registerLocator(model);
                try {
                    Spatial s = objectCache.get(this.toString() + ":" + model);
                    if (s == null) {
                        s = assetManager.loadModel(model);
                        if (s instanceof Node) {
                            for (Spatial c : ((Node) s).getChildren()) {
                                c.setName(this.toString()+":"+c.getName());
                            }
                        }
                        s.setName(this.toString() +":"+ model);
                        objectCache.put(s.getName(), s);
                    }
                    if (this.texture == null) {
                        jme_node.setMaterial(null);
                        //jme_node = new Node();
                    }
                    jme_node.attachChild(s);
                } catch (java.lang.StringIndexOutOfBoundsException e) {
                    System.out.println("Error name:" + model);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jme_node.getChildren().size() == 1 && jme_node.getChild(0) instanceof Geometry) {
            if (texture != null) jme_node.getChild(0).setMaterial(mat);
        }
    }


    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        setTextureHelper(texture);
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        setModelHelper(model);
    }

    /**
     * returns the distance between this and another object
     * @param obj the other object
     * @return distance
     */
    public double distance(EnvNode obj) {
        double xdiff, ydiff, zdiff;
        xdiff = obj.getX() - getX();
        ydiff = obj.getY() - getY();
        zdiff = obj.getZ() - getZ();

        return Math.sqrt(xdiff * xdiff + ydiff * ydiff + zdiff * zdiff);
    }
    
    public double distance(double x, double y, double z) {
        double xdiff, ydiff, zdiff;
        xdiff = x - getX();
        ydiff = y - getY();
        zdiff = z - getZ();
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff + zdiff * zdiff);

    }    

    @Override
    public void useLightingMaterial(boolean lighting) {
        super.useLightingMaterial(lighting);
        if (this.texture != null) {
            if (lighting) {
                jme_node.getChild(0).setMaterial(mat_lit);
            } else {
                jme_node.getChild(0).setMaterial(mat);
            }
        }
    }    
        
    /**
     * @return the transparent
     */
    public boolean isTransparent() {
        return transparent;


    }

    /**
     * @param transparent the transparent to set.  Only works if the texture
     * has an alpha channel.
     */
    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
        if (transparent) {
            mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
            mat_lit.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
            jme_node.setQueueBucket(Bucket.Transparent);
        } else {
            mat.getAdditionalRenderState().setBlendMode(BlendMode.Off);
            mat_lit.getAdditionalRenderState().setBlendMode(BlendMode.Off);            
            jme_node.setQueueBucket(Bucket.Opaque);
        }
    }


}
