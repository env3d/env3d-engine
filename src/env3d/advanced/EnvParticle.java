
package env3d.advanced;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class EnvParticle extends EnvAbstractNode {

    protected ParticleEmitter pMesh;

    public EnvParticle(int numParticles) {
        mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        setTexture("textures/particle/flare1.png");

        pMesh = new ParticleEmitter(this.toString()+"Emitter", Type.Triangle, numParticles);

        pMesh.setMaterial(mat);
        pMesh.setGravity(0, 0, 0);
        jme_node.detachAllChildren();
        jme_node.attachChild(pMesh);
    }

    /**
     * Sets the texture for this particle system.
     * Only works with mesh particles.
     *
     * @param textureFile
     */
    public void setTexture(String textureFile) {
        mat.setTexture("Texture", assetManager.loadTexture(textureFile));
    }

    /**
     * Returns the internal java monkey particle system.  Caller
     * must use this to customize the particle system
     * @return
     */
    public ParticleEmitter getJmeParticleSystem() {
        return pMesh;
    }
}
