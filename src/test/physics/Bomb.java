/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.physics;

import env3d.advanced.EnvPhysicsNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.math.Vector3f;
import env3d.advanced.EnvParticle;

/**
 *
 * @author jmadar
 */
public class Bomb extends EnvPhysicsNode {

    private Vector3f prevLocation = new Vector3f();


    public Bomb() {
        setTexture("models/bomb/bomb.tga");
        setModel("models/bomb/bomb.obj");
        prevLocation.set(jme_node.getLocalTranslation());
    }

    public boolean hasMoved() {
        boolean moved = prevLocation.distance(physicsControl.getPhysicsLocation()) > 0.3;
        prevLocation.set(physicsControl.getPhysicsLocation());
        return moved;
    }

    public void explode() {
        setMass(0);
        setModel(null);
        setCollisionShape(new BoxCollisionShape(new Vector3f(5,5,5)));
    }


    @Override
    public void setLocation(double x, double y, double z) {
        physicsControl.setPhysicsLocation(new Vector3f((float)x, (float)y, (float)z));
        prevLocation.set((float)x, (float)y, (float)z);

    }

}
