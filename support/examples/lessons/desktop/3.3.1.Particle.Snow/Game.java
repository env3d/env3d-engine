import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvParticle;
import env3d.Env;

import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;
   
public class Game
{
    public Game() {        
    }
       
    public void play() {        
        // Create a blank environment
        Env env = new EnvAdvanced();
         
        // Create a particle system with 10000 particles
        EnvParticle particle = new EnvParticle(10000);

        // emit in the negative y direction
        particle.getJmeParticleSystem().getParticleInfluencer().setInitialVelocity(new Vector3f(0, -5, 0));
        
        // size of each particle
        particle.getJmeParticleSystem().setStartSize(0.05f);
        particle.getJmeParticleSystem().setEndSize(0.01f);
        // how long does each particle last        
        particle.getJmeParticleSystem().setLowLife(100);
        particle.getJmeParticleSystem().setHighLife(10000);
        // The start and end color, color is (r, g, b, alpha).  
        // alpha = 1 means visible, alpha = 0 means transparent
        particle.getJmeParticleSystem().setStartColor(new ColorRGBA(1, 1, 1, 1));
        particle.getJmeParticleSystem().setEndColor(new ColorRGBA(1, 1, 1, 0.8f));

        // Sets the texture to be a snowfake 
        particle.setTexture("textures/particle/snowflake.png");        
        // put the particle system much higher than the camera
        particle.setY(50);
         
        // add the particle to the environment. 
        // The default center point is 0, 0, 0.
        env.addObject(particle);
                 
        // position the camera to be lower than the particle
        // source
        env.setCameraXYZ(0, 0, 0);
        
        // Exit when the escape key is pressed
        while (env.getKey() != 1) {
            env.advanceOneFrame();
        }
        env.exit();
    }
     
    public static void main(String [] args) {
        (new Game()).play();
    }
}