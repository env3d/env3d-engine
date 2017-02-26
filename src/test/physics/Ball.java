/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.physics;

import env3d.advanced.EnvPhysicsNode;
import com.jme3.math.ColorRGBA;
import env3d.advanced.EnvParticle;

/**
 *
 * @author jmadar
 */
public class Ball extends EnvPhysicsNode {
    
    public Ball(double x, double y, double z, double xv, double yv, double zv) {
        setX(x); setY(y); setZ(z);

        //setModel("models/cube/cube.obj");
        //setTexture("models/cube/cube.tga");

        //jme_node.setLocalScale(3, 3, 1);
        setScale(1);
        // Attach a trail particle system
        setMass(5);
        setLinearVelocity(xv, yv, zv);
        setGravity(0, 10f, 0);
        setRestitution(0.1f);
        setFriction(0.1f);

        EnvParticle p = new EnvParticle(600);
        p.getJmeParticleSystem().setStartColor(ColorRGBA.Red);

        p.getJmeParticleSystem().setParticlesPerSec(600);
        p.getJmeParticleSystem().setGravity(0,0,0);
        p.getJmeParticleSystem().setLowLife(0.5f);
        p.getJmeParticleSystem().setHighLife(1);

        attach(p);
    }

}
