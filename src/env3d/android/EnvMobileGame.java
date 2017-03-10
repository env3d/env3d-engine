/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.android;

import env3d.android.EnvAndroid;

/**
 *
 * @author jmadar
 * @deprecated 
 */
public abstract class EnvMobileGame {
    
    protected EnvAndroid env;      
    
    /**
     * Instead of putting the setup code in the constructor,
     * put it in the setup() method.
     */
    abstract public void setup();   
    
    /**
     * The loop() method is called automatically before every
     * screen update.  No need to write an explicit loop.
     */
    abstract public void loop();

    /**
     * @return the env
     */
    public EnvAndroid getEnv() {
        return env;
    }

    /**
     * @param env the env to set
     */
    public void setEnv(EnvAndroid env) {
        this.env = env;
        env.setGame(this);
    }
    
    /**
     * Called by the env3d system.  Starts the game.
     */
    public void start() {
        if (env == null) {
            env = new EnvAndroid();
            env.setGame(this);
        }
        env.start();
    }
}
