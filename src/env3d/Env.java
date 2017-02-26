/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.system.JmeSystem;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 *
 * @author jmadar
 */
public class Env extends EnvBasic {
    
    private static final Logger logger = Logger.getLogger(Application.class.getName());    
    private boolean fullScreen = false;
    // variables to store the last key pressed
    private int lastKey, lastKeyDown;
    // variables to store the mouse buttons
    private int lastButton = -1, lastButtonDown = -1;
    
    public Env() {
        this(true);
    }
    
    public Env(boolean defaultControl) {
        super();
        Logger.getLogger("com").setLevel(Level.SEVERE);
        Logger.getLogger("env3d").setLevel(Level.INFO);

        // This is a workaround to set the BlueJ classloader properly.
        try {
            Thread.currentThread().setContextClassLoader(Class.forName("Game").getClassLoader());
            Class.forName("bluej.extensions.BlueJ");
            // We will manage our own extraction of native libraries
            JmeSystem.setLowPermissions(true);
            logger.log(Level.INFO, "Running in BlueJ mode");
        } catch (Throwable e) {
            try {
                Class.forName("bluej.extensions.BlueJ");
                JmeSystem.setLowPermissions(true);
                logger.log(Level.INFO, "Running in BlueJ Extension mode");                
            } catch (Throwable ex) {
                logger.log(Level.INFO, "Running in jMonkey mode");
            }
        }

        if (System.getProperty("javawebstart.version") != null) {
            JmeSystem.setLowPermissions(true);
            logger.log(Level.INFO, "Running in webstart mode");
        }

        settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        start();
        while (!init) {
            // Wait until initalized
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }

        setCameraXYZ(5, 1.8, 9.9);
        try {
            setRoom(new SimpleRoom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultControl(defaultControl);
    }
    
    private void processKeys() {
        lastKey = 0;
        lastButton = -1;
        if (Keyboard.getEventKey() != 0) {

            if (Keyboard.getEventKeyState()) {
                lastKeyDown = Keyboard.getEventKey();
            } else {
                lastKey = lastKeyDown;
                lastKeyDown = 0;
            }

        }

        if (Mouse.getEventButton() != -1) {
            if (Mouse.getEventButtonState()) {
                lastButtonDown = Mouse.getEventButton();
            } else {
                lastButton = lastButtonDown;
                lastButtonDown = -1;
            }
        }

    }

    /**
     * Return the last key that was released (down and then up).  Note
     * that this method only detects one key at a time -- may miss keys
     * if 2 keys are released at the same time.  Use getKeyDown(int) to
     * detect multiple keys.
     * @return the key that was released
     */
    public int getKey() {
        return lastKey;
    }

    /**
     * Returns the last key that was held down.  Note that this method
     * only returns the latest key down if 2 keys are down at the same time.
     * use getKeyDown(int) if you want to detect multiple key downs.
     * @return the key that is being held down
     * @see Env#getKeyDown(int) 
     */
    public int getKeyDown() {
        return lastKeyDown;
    }

    /**
     * Test to see if a key is pressed downed.  This method allows testing
     * of multiple keys being held down.
     * @param keycode
     * @return true if the keycode is being held down
     */
    public boolean getKeyDown(int keycode) {
        return Keyboard.isKeyDown(keycode);
    }

    /**
     * Get the X mouse coordinate
     *
     * @return x coordinate of the mouse
     */
    public int getMouseX() {
        return Mouse.getX();
    }

    /**
     * Get the Y mouse coordinate
     *
     * @return y coordinate of the mouse
     */
    public int getMouseY() {
        return Mouse.getY();
    }

    /**
     * Get the difference along the x-axis in mouse movement
     * since the last call
     *
     * @return the difference in the mouse's x-axis movement
     */
    public int getMouseDX() {
        return Mouse.getDX();
    }

    /**
     * Get the difference along the y-axis in mouse movement
     * since the last call
     *
     * @return the difference in the mouse's y-axis movement
     */
    public int getMouseDY() {
        return Mouse.getDY();
    }

    /**
     * Have the Env window gain control of the mouse pointer (hide it).
     *
     * @param grab true to hide mouse pointer, false to release it
     * to the OS.
     */
    public void setMouseGrab(boolean grab) {
        Mouse.setGrabbed(grab);
    }

    /**
     * Get the status of mouse grab
     *
     * @return true if mouse is grabbed, false otherwise.
     */
    public boolean isMouseGrabbed() {
        return Mouse.isGrabbed();
    }

    /**
     * Checks to see if a particular mouse button is being held down.
     * @param button
     * @return 
     */
    public boolean getMouseButtonDown(int button) {
        return Mouse.isButtonDown(button);
    }

    /**
     * Returns the last mouse button that was clicked
     * @return 
     */
    public int getMouseButtonClicked() {
        return lastButton;
    }
    
    /**
     * Allow switching to fullscreen.
     * @param fs true to switch to fullscreen.  False to switch to window mode.
     */
    public void setFullscreen(boolean fs) {
        if (fullScreen != fs) {
            settings.setFullscreen(fs);
            settings.setFrequency(0);

            context.setSettings(settings);
            context.restart();
            fullScreen = fs;

            // Reset the keyboard buffer
            lastKey = 0;
            lastKeyDown = 0;
            // Somehow, the mouse grab is screwed up after the
            // switch, so we had to restore it by flipping it
            // a couple of times.
            setMouseGrab(!isMouseGrabbed());
            advanceOneFrame();
            setMouseGrab(!isMouseGrabbed());
            advanceOneFrame();

        }
    }

    @Override
    public void setResolution(int width, int height, int bpp) {
        super.setResolution(width, height, bpp);
        // Somehow, the mouse grab is screwed up after the
        // switch, so we had to restore it by flipping it
        // a couple of times.
        setMouseGrab(!isMouseGrabbed());
        advanceOneFrame();
        setMouseGrab(!isMouseGrabbed());
        advanceOneFrame();

    }
    
    /**
     * Returns the current screen height
     * @return 
     */
    public int getScreenHeight() {
        return settings.getHeight();
    }

    /**
     * Returns the current screen height
     * @return 
     */
    public int getScreenWidth() {
        return settings.getWidth();
    }
 
    /**
     * Are we in full screen mode
     * @return 
     */
    public boolean isFullScreen() {
        return fullScreen;
    }  
    
    /**
     * Render the current screen.  Maximum of 60 frames per second
     * @see Env#advanceOneFrame(int) 
     */
    public void advanceOneFrame() {
        advanceOneFrame(60);
    }
    
    /**
     * Render the current screen
     * @param targetFrameRate the maximum frame rate
     */
    public void advanceOneFrame(int targetFrameRate) {
        processKeys();
        ((EnvDisplay) context).runLoop();

        Display.sync(targetFrameRate);
    }
 
    
    @Override
    public void start(Type contextType, boolean waitFor) {
        if (context != null && context.isCreated()) {
            logger.warning("start() called when application already created!");
            return;
        }

        if (settings == null) {
            settings = new AppSettings(true);
        }

        logger.log(Level.FINE, "Starting application: {0}", getClass().getName());
        //JmeSystem.showSettingsDialog(settings, true);
        //context = JmeSystem.newContext(settings, contextType);
        settings.setTitle("Env3D");

        JmeSystem.initialize(settings);
        context = new EnvDisplay();
        context.setSettings(settings);
        context.setSystemListener(this);
        context.create(false);        
    }
    
}
