/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d;

import com.jme3.math.Vector3f;

/**
 *
 * @author jmadar
 */
public class EnvRay {

    private EnvVector origin = new EnvVector(), direction = new EnvVector();

    /**
     * @return the origin
     */
    public EnvVector getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(EnvVector origin) {
        this.origin = origin;
    }

    /**
     * @return the direction
     */
    public EnvVector getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(EnvVector direction) {
        this.direction = direction;
    }

    /**
     * For internal use
     * @param dir
     */
    public void setDirection(Vector3f dir) {
        this.direction.setX(dir.x);
        this.direction.setY(dir.y);
        this.direction.setZ(dir.z);
    }

    /** For internal use.
     * @param origin the origin to set
     */
    public void setOrigin(Vector3f origin) {
        this.origin.setX(origin.x);
        this.origin.setY(origin.y);
        this.origin.setZ(origin.z);
    }
}
