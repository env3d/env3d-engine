/*
 * EnvObject.java
 *
 * Created on December 19, 2007, 5:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package env3d;

/**
 *
 * @author Jmadar
 */
public class EnvObject {
    
    private double x, y, z, scale;
    private double rotateX, rotateY, rotateZ;
    private String texture, textureNormal, model;
    private boolean transparent;
    
    /** Creates a new instance of EnvObject */
    public EnvObject() {
        // Defaults
        x = 0;
        y = 0;
        z = 0;
        rotateX = 0;
        rotateY = 0;
        rotateZ = 0;
        scale = 1;
        texture = "textures/earth.png";
        model = "sphere";
        transparent = false;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getRotateX() {
        return rotateX;
    }

    public void setRotateX(double rotateX) {
        this.rotateX = rotateX;
    }

    public double getRotateY() {
        return rotateY;
    }

    public void setRotateY(double rotateY) {
        this.rotateY = rotateY;
    }

    public double getRotateZ() {
        return rotateZ;
    }

    public void setRotateZ(double rotateZ) {
        this.rotateZ = rotateZ;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * returns the distance between this and another object
     * @param obj the other object
     * @return distance
     */
    public double distance(EnvObject obj) {
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
    }

    /**
     * @return the textureNormal
     */
    public String getTextureNormal() {
        return textureNormal;
    }

    /**
     * @param textureNormal the textureNormal to set
     */
    public void setTextureNormal(String textureNormal) {
        this.textureNormal = textureNormal;
    }
    
}
