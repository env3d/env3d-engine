/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.scenecreator;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import env3d.advanced.EnvNode;
import java.util.List;

/**
 *
 * @author jmadar
 */
public class SceneNode extends EnvNode {

    private String name;

    public void setName(String className) {
        name = className.substring(0, 1).toUpperCase() + className.substring(1);
    }

    public String getName() {
        return name;
    }

    public void setWireFrame(boolean wireFrame) {
        mat.getAdditionalRenderState().setWireframe(wireFrame);
        setWireFrameHelper(wireFrame, jme_node.getChildren());
    }

    public void setWireFrameHelper(boolean wireFrame, List<Spatial> children) {
        for (Spatial c : children) {
            if (c instanceof Geometry) {
                Material m = ((Geometry) c).getMaterial();
                m.getAdditionalRenderState().setWireframe(wireFrame);
            } else if (c instanceof Node) {
                setWireFrameHelper(wireFrame, ((Node) c).getChildren());
            }
        }
    }

    public String toSaveString() {
        return getX() + "," + getY() + "," + getZ() + "," 
                + getScale() + "," + getRotateX() + "," + getRotateY() + "," + getRotateZ()
                + "," + getModel() + "," + getTexture() + "," + name;
    }
}
