/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package env3d.advanced;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import env3d.GameObjectAdapter;
import java.util.ArrayList;

/**
 *
 * EnvAbstractNode provides a way to interact with the underlying JavaMonkey
 * scene graph.  This gives better performance and allows for more advanced
 * visual effects.
 *
 * @author jmadar
 */
abstract public class EnvAbstractNode extends GameObjectAdapter {

    protected float rotateX, rotateY, rotateZ, scale = 1;
    private Vector3f tmpVec;
    private ArrayList<EnvAbstractNode> attachments = new ArrayList<EnvAbstractNode>();

    @Override
    public void update() {
    }

    /**
     * Attach another EnvAbastractNode to this node
     * @param n
     */
    public void attach(EnvAbstractNode n) {
        attachments.add(n);
        jme_node.attachChild(n.getJme_node());
    }

    public void remove(EnvAbstractNode n) {
        attachments.remove(n);
        jme_node.detachChild(n.getJme_node());
    }
            
    /**
     * @return the x
     */
    public double getX() {
        return jme_node.getLocalTranslation().x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
//        tmpVec = jme_node.getLocalTranslation();
//        tmpVec.x = (float)x;
        jme_node.setLocalTranslation((float) x, (float)getY(), (float)getZ());
    }

    /**
     * @return the y
     */
    public double getY() {
        return jme_node.getLocalTranslation().y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
//        tmpVec = jme_node.getLocalTranslation();
//        tmpVec.y = (float)y;
//        jme_node.setLocalTranslation(tmpVec);
        jme_node.setLocalTranslation((float) getX(), (float)y, (float)getZ());
    }

    /**
     * @return the z
     */
    public double getZ() {
        return jme_node.getLocalTranslation().z;

    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
//        tmpVec = jme_node.getLocalTranslation();
//        tmpVec.z = (float)z;
//        jme_node.setLocalTranslation(tmpVec);
        jme_node.setLocalTranslation((float) getX(), (float)getY(), (float)z);

    }

    /**
     * @return the rotateX
     */
    public double getRotateX() {
        return rotateX;
    }

    /**
     * @param rotateX the rotateX to set
     */
    public void setRotateX(double rotateX) {
        this.rotateX = (float) rotateX;
        updateRotation();
    }

    /**
     * @return the rotateY
     */
    public double getRotateY() {
        return rotateY;
    }

    /**
     * @param rotateY the rotateY to set
     */
    public void setRotateY(double rotateY) {
        this.rotateY = (float) rotateY;
        updateRotation();
    }

    /**
     * @return the rotateZ
     */
    public double getRotateZ() {
        return rotateZ;
    }

    /**
     * @param rotateZ the rotateZ to set
     */
    public void setRotateZ(double rotateZ) {
        this.rotateZ = (float) rotateZ;
        updateRotation();
    }

    /**
     * @return the scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(double scale) {
        this.scale = (float) scale;
        jme_node.setLocalScale(this.scale);
    }
    
    public void updateRotation() {
        qY.fromAngleNormalAxis(rotateY*FastMath.DEG_TO_RAD, Vector3f.UNIT_Y);
        qX.fromAngleNormalAxis(rotateX*FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
        qZ.fromAngleNormalAxis(rotateZ*FastMath.DEG_TO_RAD, Vector3f.UNIT_Z);
        jme_node.setLocalRotation(qY.mult(qX.mult(qZ, q), q));
    }

}
