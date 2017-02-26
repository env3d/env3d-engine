/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.physics;

import env3d.advanced.EnvPhysicsNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import env3d.advanced.EnvTerrain;

/**
 *
 * @author jmadar
 */
public class Church extends EnvPhysicsNode {

    private EnvTerrain terrain;
    
    public Church(EnvTerrain terrain ) {
        this.terrain = terrain;
        setTexture(null);
        setModel("models/bsc/bsc.obj");
        setScale(10);        
        setX(-18);
        setZ(1);
        setY(terrain.getHeight(-18, 1) + getScale());
        setMass(30);
        //setGravity(0, -0.1, 0);
        setCollisionShape(new BoxCollisionShape(new Vector3f((float)(getScale()*1.1),
                (float)getScale(), (float)getScale())));
    }

    public void reset() {
        setX(-18);
        setZ(1);
        setY(terrain.getHeight(-18, 1) + getScale());
        float[] angles = {0,0,0};
        physicsControl.setPhysicsRotation(new Quaternion(angles));
    }

}
