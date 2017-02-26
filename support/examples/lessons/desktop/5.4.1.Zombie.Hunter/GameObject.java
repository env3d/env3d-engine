import env3d.advanced.EnvNode;
import env3d.Env;
import org.lwjgl.input.Keyboard;

/**
 * Base class for all objects in our game.  
 */
abstract public class GameObject extends EnvNode
{   
    // Every GameObject has access to the environment
    protected Env env;
        
    /**
     * The move method is called by the game class
     * every frame.  Sub-class should override this method.
     */
    public void move()
    {
    }
    
    /**
     * Sets the env, used by the game class
     * @param env 
     */
    public void setEnv(Env env)  
    {
        this.env = env;
    }
    
    /**
     * Returns the environment object
     * @return 
     */
    public Env getEnv() 
    {
        return env;
    }
    
    /**
     * Turn this object to face another game object
     * @param gameObj to object to face 
     */
    public void turnToFace(GameObject gameObj) 
    {
        setRotateY(Math.toDegrees(Math.atan2(gameObj.getX()-this.getX(),gameObj.getZ()-this.getZ())));
        
        // Uncomment this next line if you want to look up and down as well
        // setRotateX(Math.toDegrees(Math.asin((this.getY()-gameObj.getY())/this.distance(gameObj))));        
    }
    
    /**
     * Moves forward at a certain speed, negative speed to move backwards
     * @param speed
     */
    public void moveForward(double speed)
    {
        this.setX(this.getX()+speed*Math.sin(Math.toRadians(this.getRotateY())));
        this.setZ(this.getZ()+speed*Math.cos(Math.toRadians(this.getRotateY())));        
    }
    
    /**
     * Moves to the right at a certain speed, negative to move left
     * @param speed 
     */
    public void moveRight(double speed)
    {
        this.setX(this.getX()-speed*Math.cos(Math.toRadians(this.getRotateY())));
        this.setZ(this.getZ()+speed*Math.sin(Math.toRadians(this.getRotateY())));                    
    }
    
    /**
     * Put the camera behind this object, provide the x, y and z offset 
     * relative to the object
     */
    protected void followCam(double followDist, double offX, double offY, double offZ) 
    {
        env.setCameraXYZ( (this.getX() + offX) - followDist * Math.sin(Math.toRadians(this.getRotateY()))* Math.cos(Math.toRadians(env.getCameraPitch())),
        (this.getY() + offY) - followDist * Math.sin(Math.toRadians(env.getCameraPitch())),
        (this.getZ() + offZ) - followDist * Math.cos(Math.toRadians(this.getRotateY()))* Math.cos(Math.toRadians(env.getCameraPitch())) );
        
        env.setCameraYaw(this.getRotateY() - 180);        
    }            
    
    /**
     * Standard FPS control scheme - camera follow this object.  
     * W,A,S,D for movement, Mouse to look
     */
    protected void simpleFPSControl(double speed)
    {
        // Hide the mouse
        if (!env.isMouseGrabbed()) env.setMouseGrab(true);
        
        // Put the camera behind this object, you may need to customize this
        followCam(2, 0, 1, 0);
        
        // The standard keyboard controls
        if (env.getKeyDown(Keyboard.KEY_W) || env.getKeyDown(Keyboard.KEY_UP)) {
            // Forward
            moveForward(speed);
        }
        if (env.getKeyDown(Keyboard.KEY_A)) {
            // Left strafe 
            moveRight(-speed);
        }
        if (env.getKeyDown(Keyboard.KEY_S) || env.getKeyDown(Keyboard.KEY_DOWN)) {
            // Reverse
            moveForward(-speed);
        }
        if (env.getKeyDown(Keyboard.KEY_D)) {
            // Right strafe
            moveRight(speed);
        }
        
        if (env.getKeyDown(Keyboard.KEY_LEFT)) {
            this.setRotateY(this.getRotateY()+2);
        } 
        if (env.getKeyDown(Keyboard.KEY_RIGHT)) {
            this.setRotateY(this.getRotateY()-2);
        } 
        
        // Set the camera properly if using keyboard control
        if (env.getKeyDown(Keyboard.KEY_UP) || env.getKeyDown(Keyboard.KEY_DOWN) ||
        env.getKeyDown(Keyboard.KEY_LEFT) || env.getKeyDown(Keyboard.KEY_RIGHT)) {
            env.setCameraPitch(-20);        
        }
        
        // Mouse look
        this.setRotateY(this.getRotateY()-0.1*env.getMouseDX());
        env.setCameraPitch(env.getCameraPitch()+0.1*env.getMouseDY());        
    }
            
}
