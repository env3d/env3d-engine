// Import the Env class to work with the 3D Environment 
import env3d.Env;
import org.lwjgl.input.Keyboard;
 
/**
 * Game class, the main controller class
 */
public class Game
{
    // This game controlls exactly 1 Doty object
    private Monster m;
    private Ball b;
     
    /**
     * Put the doty at default location
     */ 
    public Game()
    { 
        // Initial monster location
        m = new Monster(5,1,1);
        // Initial ball location - hidden
        b = new Ball(100, 0, 0);
    }
     
    /**
     * The play method is the main entry point of the entire 
     * game.  It contains some setup code and a game loop 
     */
    public void play()
    {
        // Initial setup code
        boolean finished;
        finished = false;
         
        Env env;
        // Create the environment.  This next line will cause a 
        // blank window to appear
        env = new Env();
         
        // Add objects to the environment
        env.addObject(m);
        env.addObject(b);        
         
        // Camera control.  We fix the camera and
        // not allow users to change it.
        env.setCameraXYZ(5,5,13);
        env.setCameraPitch(-25);
        env.setDefaultControl(false);
         
        // The main game loop
        while (finished == false)
        {
            // --- Process user input ---
             
            // Keyboard input
            // Track a complete key press (key up)
            int key = env.getKey();
            // Track a key down only
            int keyDown = env.getKeyDown();
             
            if (key == Keyboard.KEY_ESCAPE) {
                finished = true;
            } else if (key == Keyboard.KEY_S) {
                m.swipe();
                b.setDir(m.getRotateY());
                b.setXYZ(m.getX(), m.getY(), m.getZ());                
            } 
             
            // Use key down to move the monster
            if (keyDown == Keyboard.KEY_RIGHT) {
                m.moveX(0.1);
            } else if (keyDown == Keyboard.KEY_LEFT) {
                m.moveX(-0.1);
            } else if (keyDown == Keyboard.KEY_UP) {
                m.moveZ(-0.1);
            } else if (keyDown == Keyboard.KEY_DOWN) {
                m.moveZ(0.1);
            } else if (keyDown == Keyboard.CHAR_NONE) {
                if (m.getState().equals("run")) {
                    m.idle();
                }
            }
             
            
            // --- finish user input ---
             
            // Change the object state
            m.move();
            b.move();
             
            // Update display
            env.advanceOneFrame();
        }
 
        // Cleanup code
        env.exit();
    }   
}