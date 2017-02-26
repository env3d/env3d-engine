/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.physics;

import com.jme3.math.Vector3f;
import env3d.advanced.EnvParticle;

/**
 *
 * @author jmadar
 */
public class Explosion extends EnvParticle {

    private int frame = 0;
    private int maxFrame = 300;
    private boolean dead = false;

    public Explosion(double x, double y, double z) {
        super(5000);
        setX(x); setY(y); setZ(z);
        pMesh.setGravity(0,5,0);
        pMesh.setVelocityVariation(1);
        pMesh.setParticlesPerSec(5000);
        pMesh.setLowLife(100);
        pMesh.setHighLife(100);
        pMesh.setInitialVelocity(new Vector3f(0, 20, 0));

    }

    public void update() {
        frame++;
        if (frame > maxFrame) {
            dead = true;
        }

    }

    /**
     * @return the dead
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * @param dead the dead to set
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
