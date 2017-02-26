package env3d;

import com.jme3.math.Vector3f;

/**
 * Encapsulate the result from a mouse pick.
 * It contains the intersection point of the
 * pick operation, as well as the pick object itself.
 *
 * @author jmadar
 */
public class EnvPickResult {
    private EnvVector intersectionPoint = new EnvVector();
    private Object pickObject;

    public EnvPickResult(Vector3f intesection, Object obj) {
        intersectionPoint.setX(intesection.x);
        intersectionPoint.setY(intesection.y);
        intersectionPoint.setZ(intesection.z);
        pickObject = obj;
    }

    /**
     * @return the pickObject
     */
    public Object getPickObject() {
        return pickObject;
    }

    /**
     * @param pickObject the pickObject to set
     */
    public void setPickObject(Object pickObject) {
        this.pickObject = pickObject;
    }

    /**
     * @return the intersectionPoint
     */
    public EnvVector getIntersectionPoint() {
        return intersectionPoint;
    }

    /**
     * @param intersectionPoint the intersectionPoint to set
     */
    public void setIntersectionPoint(EnvVector intersectionPoint) {
        this.intersectionPoint = intersectionPoint;
    }

}
