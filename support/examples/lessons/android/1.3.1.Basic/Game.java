import env3d.android.EnvMobileGame;
 
/**
 * A basic template for an Android game in Env3D
 * Need to implement at least 2 methods:  setup() and loop()
 * 
 * This template also illustrates the basic difference between
 * the android and desktop versions of Env3D.
 */
public class Game extends EnvMobileGame
{
    // Put a doty object into our environment
    private Doty doty;   
    
    /**
     * This is where you do all your setup
     */
    public void setup() 
    {
        // The Android version of Env does not include a default room
        // So we setup one here
        env.setRoom(new SimpleRoom());
        
        doty = new Doty(5,1,1);
        env.addObject(doty);
        
        // Place the camera at the correct place
        env.setCameraXYZ(5, 1.8, 9.9);
    }

    /**
     * The loop method is called every frame by the android system
     * In the android version, the programmer does not have access
     * to write the main loop.
     */
    public void loop() 
    {
        // At every frame, we want to activate the default first person control
        processInput();
    }
    
    /**
     * In the android version, there is no default controller, so we have to implement our own basic control
     */
    private void processInput()
    {
        // The local variable step determines how fast the camera moves.
        double step = 0.2;
        
        // Check the different key presses and move the camera accordingly
        if (env.getKeyDown("up")) {
            env.setCameraXYZ(env.getCameraX()-step*Math.sin(Math.toRadians(env.getCameraYaw())),
                                env.getCameraY(),
                                env.getCameraZ()-step*Math.cos(Math.toRadians(env.getCameraYaw())));
        }
         
        if (env.getKeyDown("down")) {
            env.setCameraXYZ(env.getCameraX()+step*Math.sin(Math.toRadians(env.getCameraYaw())),
                                env.getCameraY(),
                                env.getCameraZ()+step*Math.cos(Math.toRadians(env.getCameraYaw())));
        }
         
        if (env.getKeyDown("left")) {
            env.setCameraXYZ(env.getCameraX()-step*Math.cos(Math.toRadians(env.getCameraYaw())),
                                env.getCameraY(),
                                env.getCameraZ()+step*Math.sin(Math.toRadians(env.getCameraYaw())));
        }
        if (env.getKeyDown("right")) {
            env.setCameraXYZ(env.getCameraX()+step*Math.cos(Math.toRadians(env.getCameraYaw())),
                                env.getCameraY(),
                                env.getCameraZ()-step*Math.sin(Math.toRadians(env.getCameraYaw())));
        }
         
        
        // Use the screen touch sensor for "mouse look"
        double newPitch = env.getCameraPitch()+env.getTouchDY();         
        // Restrict the pitch to within a "reasonable" amount
        if (newPitch > 70) {
            env.setCameraPitch(70);
        } else if (newPitch < -50) {
            env.setCameraPitch(-50);
        } else {
            env.setCameraPitch(newPitch);
        }         
        env.setCameraYaw(env.getCameraYaw()-env.getTouchDX());        
    }
    

    /**
     * A simple launcher to run the game locally in emulation mode     
     * @param args 
     */
    public static void main(String[] args) {
        Game g = new Game();
        g.start();
    }

}