package test.picking;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import env3d.Env;
import org.lwjgl.input.Keyboard;

/**
 * This is where the main loop is.
 * @author jmadar
 */
public class Game {
    
    // Monitor size (in inches) and the aspect ratio
    public static double MONITOR_SIZE = 7;
    // screen resolution
    public static int SCREEN_WIDTH = 640;
    public static int SCREEN_HEIGHT = 480;
    
    // phone dimension
    public static float PHONE_HEIGHT = 5.27f;
    public static float PHONE_WIDTH = 2.7f;    
    
    public Game() {
    }

    public void play() {
        
        
        // A variable to determine if the game is finished
        boolean finished = false;

        // Create the env object so that we can manipulate
        // the 3D environment.
        Env env = new Env();
        
        env.setResolution(SCREEN_WIDTH, SCREEN_HEIGHT, 32);

        // The "room" just makes the background white.
        env.setRoom(new Room());
        
        // Place the camera
        env.setCameraXYZ(0, 0, 1);                        
        
        // Create the phone and put into environment
        Phone phone = new Phone(env);        
        env.addObject(phone);
        env.setDefaultControl(false);
        //env.setFullscreen(true);
        
        Double offX = null; Double offY = null;
        
        Camera cam = env.getCamera();
        while (!finished) {
            //env.setDisplayStr(env.getCameraZ()+"", 15, 35, 2, 0,0,0,1);
            // Gather the user input
            int key = env.getKey();

            // Terminate program when user press escape
            if (key == 1) {
                finished = true;
            }

            // Pre-made the vector for mouse location so we can reuse it.
            Vector3f mouseVec = new Vector3f();
            // When the button is clicked
            if (env.getMouseButtonDown(0)) {                                
                // Drag the phone around the plane
                cam.getWorldCoordinates(new Vector2f((float) env.getMouseX(), (float) env.getMouseY()), 0, mouseVec);
                Vector3f dir = cam.getWorldCoordinates(
                        new Vector2f((float) env.getMouseX(), env.getMouseY()), 1f).subtractLocal(mouseVec).normalizeLocal();
                Ray ray = new Ray(mouseVec, dir);
                // check where ray intersects with plane (with normal pointing to +z axis               
                ray.intersectsWherePlane(new Plane(Vector3f.UNIT_Z,Vector3f.ZERO.dot(Vector3f.UNIT_Z)),mouseVec);

                // Check collision to see if it collides with the phone object
                CollisionResults phoneCollision = new CollisionResults();
                phone.getJme_node().collideWith(ray, phoneCollision);
                if (phoneCollision.size() > 0) {                               

                    // Remember the offset
                    if (offX == null && offY == null) {
                        // Calculate the offset so the object drags under the
                        // mouse
                        offX = phone.getX() - mouseVec.x;
                        offY = phone.getY() - mouseVec.y;                        
                    }

                    // Move the phone to where the mouse pointer is
                    phone.setX(mouseVec.x + offX);
                    phone.setY(mouseVec.y + offY);
                }
            }
            
            // Reset the "offset" 
            if (!env.getMouseButtonDown(0)) {
                offX = null;
                offY = null;            
            }

            // allow 1st person control
            if (env.getKey() == Keyboard.KEY_P) {
                env.setDefaultControl(!env.isDefaultControl());
            }
            
            if (env.getKey() == Keyboard.KEY_F) {
                env.setFullscreen(!env.isFullScreen());
            }
            
            // Update the environment
            env.advanceOneFrame();
        }
        //env.setFullscreen(false);

        // Exit the program cleanly
        env.exit();
    }

    public static void main(String[] args) {
        (new Game()).play();
    }
}