package env3d;

import com.jme3.app.Application;
import com.jme3.app.LegacyApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import de.lessvoid.nifty.Nifty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Env stands for environment and is the entry point to interface with the opengl engine class.
 * This class is written to provide students with a way to access a virtual environment.
 * Students can put any arbitrary objects into this world.  This class make exclusive use of
 * reflection so that any object can be added without the object having to extend another class
 * or implement an interface.  This makes teaching introduction courses much easier as students
 * do not have to be exposed to the idea of inheritance right away.
 *
 * This class also provide a way to access to the camera as well as access the keyboard.
 *
 * @author Jason Madar
 */
public class EnvBasic extends LegacyApplication {

    private static final Logger logger = Logger.getLogger(Application.class.getName());
    protected boolean init = false;
    protected Node rootNode = new Node("Root Node");
    protected Node guiNode = new Node("Gui Node");
    protected Node statusNode = new Node("Status Node");
    protected BitmapFont guiFont;
    protected float tpf;
    protected HashMap<Object, GameObjectAdapter> objectsMap = new HashMap<Object, GameObjectAdapter>();
    protected HashMap<String, Object> internalObjects = new HashMap<String, Object>();
    protected HashMap<String, AudioNode> audioMap = new HashMap<String, AudioNode>();
    protected RoomAdapter roomObject;
    protected EnvCamera flyCam;
    protected BitmapText statusText;

    private DirectionalLight directionalLight;
    private AmbientLight ambientLight;

    private boolean showStatus = false;
    private Nifty nifty;
    
    /**
     * Create an environment.  The default is to have the environment take 
     * control of the mouse.
     */
    public EnvBasic() {
        this(true);
    }

    /**
     * Creates a 3d environment.
     * @param defaultControl true to grab the mouse, false to release the mouse
     */
    public EnvBasic(boolean defaultControl) {
        
    }

    /**
     * Add an object to the environment
     * @param obj 
     */
    public void addObject(Object obj) {
        synchronized (this) {
            GameObjectAdapter ga;
            if (obj instanceof GameObjectAdapter) {
                ga = (GameObjectAdapter) obj;
            } else {
                ga = new GameObjectAdapter(obj);
            }
            objectsMap.put(obj, ga);
            internalObjects.put(ga.toString(), obj);
            if (isLightingEnabled()) {
                ga.useLightingMaterial(true);
            } else {
                ga.useLightingMaterial(false);
            }

            rootNode.attachChild(objectsMap.get(obj).getJme_node());
        }
    }

    /**
     * Removes an object from the environment
     * @param obj 
     */
    public void removeObject(Object obj) {
        synchronized (this) {
            GameObjectAdapter ga = objectsMap.get(obj);
            if (ga != null) {
                rootNode.detachChild(ga.getJme_node());
                internalObjects.remove(ga.toString());
                objectsMap.remove(obj);
            }
        }
    }

    /**
     * Get all the objects that has been added to the environment
     * @return 
     */
    public Collection getObjects() {
        return objectsMap.keySet();
    }
    /** Keeps a cache of all the collections of classes that has been requested, so
     *  there is no need to recreated them
     */
    private HashMap<Class, Object> classCollectionCache = new HashMap<Class, Object>();
    /** Keeps a cache of all the collections of classes that has been returned */
    private HashSet<Class> classCollectionReturned = new HashSet<Class>();

    /**
     * Returns all objects of class c
     * @param <T>
     * @param c
     * @return 
     */
    public <T> Collection<T> getObjects(Class<T> c) {
        if (c == null) {
            throw new NullPointerException("Class cannot be null");
        } else {
            HashSet<T> s = (HashSet<T>) classCollectionCache.get(c);
            if (s == null) {
                s = new HashSet<T>();
                classCollectionCache.put(c, s);
            } 
            
            // Do not clear!  We use HashSet.retainAll() to avoid concurrent
            // modification exception
//            else {
//                if (!classCollectionReturned.contains(c)) {
//                    s.clear();
//                }
//            }
            
            // If the collection has not been returned, re-construct the set
            // and return it.  Mark this set so we do not reconstruction it
            // if requested during the same frame.
            if (!classCollectionReturned.contains(c)) {
                s.retainAll(objectsMap.keySet());
                for (Object o : objectsMap.keySet()) {
                    if (c.isAssignableFrom(o.getClass())) {
                        s.add((T) o);
                    }
                }
                classCollectionReturned.add(c);
            }
            return s;
        }
    }

    /**
     * Returns the first object that matches class c
     * @param <T>
     * @param c
     * @return 
     */
    public <T> T getObject(Class<T> c) {
        for (Object o : objectsMap.keySet()) {
            if (c.isAssignableFrom(o.getClass())) {
                return (T) o;
            }
        }
        return null;
    }

    /**
     * Sets the current room.  Calling this will remove all the objects in 
     * the current room.
     * @param o the room object
     */
    public void setRoom(Object o) {
        // Reset all nodes
        synchronized (this) {
            rootNode.detachAllChildren();
            objectsMap.clear();
            internalObjects.clear();
            //internalObjects.clear();

            // Now set the room
            if (o instanceof RoomAdapter) {
                roomObject = (RoomAdapter) o;
                //roomObject.setCurrentDisplay(display);
            } else {
                roomObject = new RoomAdapter(o);
            }

            if (isLightingEnabled()) {
                roomObject.useLightingMaterial(true);
            } else {
                roomObject.useLightingMaterial(false);
            }
            rootNode.attachChild(roomObject.getJme_node());
        }
    }

    /**
     * Put the camera at a particular location in 3D space.  The camera
     * looks down the z axis by default.  i.e. (0, 0, -1)
     * @param x x coordinate of the camera
     * @param y y coordinate of the camera
     * @param z z coordinate of the camera
     */
    public void setCameraXYZ(double x, double y, double z) {
        cam.getLocation().x = (float) x;
        cam.getLocation().y = (float) y;
        cam.getLocation().z = (float) z;
        cam.update();
    }

    /**
     *
     * @return the x location of the camera
     */
    public double getCameraX() {
        return cam.getLocation().x;
    }

    /**
     *
     * @return the y location of the camera
     */
    public double getCameraY() {
        return cam.getLocation().y;
    }

    /**
     *
     * @return the z location of the camera
     */
    public double getCameraZ() {
        return cam.getLocation().z;
    }

    /**
     * Get the pitch (up and down) angle of the camera, in degrees.  This is
     * in relation to the default camera orientation (0, 0, -1)
     * @return the camera pitch in degrees
     */
    public double getCameraPitch() {
        return flyCam.getPitch();
    }

    /**
     * Get the yaw (up and down) angle of the camera, in degrees.  This is
     * in relation to the default camera orientation (0, 0, -1)
     * @return the camera yaw in degrees
     */
    public double getCameraYaw() {
        return flyCam.getYaw();
    }

    /**
     * Sets the camera pitch (up and down) in degrees in relation to
     * the default camera orientation (0, 0, -1)
     * @param angle the angle to set the pitch to, in degrees
     */
    public void setCameraPitch(double angle) {
        flyCam.setPitch(angle);
        //deltaPitch = (float) angle;
    }

    /**
     * Sets the camera yaw (left and right) in degrees in relation to
     * the default camera orientation (0, 0, -1)
     * @param angle the angle to set the pitch to, in degrees
     */
    public void setCameraYaw(double angle) {
        //deltaYaw = (float) angle;
        flyCam.setYaw(angle);
    }
    
    /**
     * Returns the framerate
     * @return frame rate in integer
     */
    public int getFrameRate() {
        return (int)(1/tpf);
    }

    /**
     * Put a string in a particular location on screen. 
     * @param displayStr
     * @param x x location of the screen (2D)
     * @param y y location of the screen (2D)
     * @param size the size of the font
     * @param r red component.  Value from 0 - 1.
     * @param g green component.  Value from 0 - 1.
     * @param b blue component.  Value from 0 - 1.
     * @param a alpha component.  Value from 0 - 1. 0 is invisible. 1 is opaque.
     */
    public void setDisplayStr(String displayStr, int x, int y, double size, double r, double g, double b, double a) {

        BitmapText text;

        // setting the text to null will result in weird screen 
        // artifacts, so we set it to a blank instead
        if (displayStr == null || displayStr == "") displayStr = " ";
        
        text = (BitmapText) guiNode.getChild("displayStr" + x + y);
        if (text == null) {
            text = new BitmapText(guiFont, false);
            text.setName("displayStr" + x + y);
            text.setLocalScale((float) size);
            text.setLocalTranslation(x, y, 0);
        }

        text.setText(displayStr);
        ColorRGBA color = text.getColor();
        if (color == null) {
            color = new ColorRGBA();
        }
        color.set((float) r, (float) g, (float) b, (float) a);
        text.setColor(color);
        text.setCullHint(CullHint.Never);
        synchronized (this) {
            guiNode.attachChild(text);
        }

    }

    /**
     * Puts a string at a particular location on screen
     * @param displayStr
     * @param x
     * @param y 
     * @see Env#setDisplayStr(java.lang.String, int, int, double, double, double, double, double) 
     */
    public void setDisplayStr(String displayStr, int x, int y) {
        setDisplayStr(displayStr, x, y, 1, 1, 1, 1, 1);
    }

    /**
     * Display a string in the middle of the screen
     * @param displayStr 
     * @see Env#setDisplayStr(java.lang.String, int, int, double, double, double, double, double) 
     */
    public void setDisplayStr(String displayStr) {
        if (displayStr == null) {
            guiNode.detachAllChildren();
        } else {
            setDisplayStr(displayStr, 5, 240, 1, 1, 1, 1, 1);
        }
    }

    /**
     * Show the current fps (frames per second) on screen
     * @param show true to show fps, false to hide it.
     */
    public void setShowStatus(boolean show) {
        showStatus = show;
        if (showStatus) {
            statusNode.attachChild(statusText);
        } else {
            statusNode.detachAllChildren();
        }
    }

    /**
     * Activate the first person control scheme.  Uses W, A, S, D for movement
     * and the mouse to look around.  SPACE and C are used to move the camera
     * up and down.
     * @param firstPersonControl true to enable first person control, false to
     * disable it.
     */
    public void setDefaultControl(boolean firstPersonControl) {
        flyCam.setEnabled(firstPersonControl);
        if (firstPersonControl) {
            inputManager.setCursorVisible(false);
        }
        //Mouse.setGrabbed(firstPersonControl);
    }

    public boolean isDefaultControl() {
        return flyCam.isEnabled();
    }

    public void setResolution(int width, int height, int bpp) {
        settings.setWidth(width);
        settings.setHeight(height);
        settings.setBitsPerPixel(bpp);
        context.setSettings(settings);
        context.restart();
    }

    /**
     * Get the current sound level
     * @return the sound level, a value between 0 and 1.
     */
    public double getVolume() {
        return listener.getVolume();
    }

    /**
     * Sets the current sound level
     * @param level a number between 0 and 1.
     */
    public void setVolume(double level) {
        listener.setVolume((float) level);
    }

    /**
     * Play a sound file at a particular location
     * @param soundFile the file to play
     * @param x x location of the sound
     * @param y y location of the sound
     * @param z z location of the sound
     * @param cutoff_distance the distance in which sound will be cut off
     */
    public void soundPlay(String soundFile, double x, double y, double z, double cutoff_distance) {
        //soundManager.play(soundFile, (float) x, (float) y, (float) z, SoundSystemConfig.ATTENUATION_LINEAR, (float) cutoff_distance);

        AudioNode clip = audioMap.get(soundFile);
        if (clip == null) {
            //clip = new AudioNode(audioRenderer, assetManager, soundFile, false);
            clip = new AudioNode(assetManager, soundFile, false);
            audioMap.put(soundFile, clip);
        }
        if (clip.getStatus() != AudioSource.Status.Playing) {
            clip.setLooping(false);
            clip.setPositional(true);
            clip.setLocalTranslation((float) x, (float) y, (float) z);
            clip.setMaxDistance((float) cutoff_distance);
            audioRenderer.playSource(clip);
        }
    }

    /**
     * Play a sound file without position.
     *
     * @param soundFile
     */
    public void soundPlay(String soundFile) {
        //soundManager.loop(soundFile);
        AudioNode clip = audioMap.get(soundFile);
        if (clip == null) {
            //clip = new AudioNode(audioRenderer, assetManager, soundFile);
            clip = new AudioNode(assetManager, soundFile);
            
            clip.setLooping(false);
            clip.setPositional(false);
            audioMap.put(soundFile, clip);
        }

        if (clip.getStatus() != AudioSource.Status.Playing) {
            audioRenderer.playSource(clip);
        }
    }

    /**
     * Loop a sound file.  This method is meant for background music and it
     * streams the sound from the source.
     *
     * @param soundFile
     */
    public void soundLoop(String soundFile) {
        //soundManager.loop(soundFile);
        AudioNode clip = audioMap.get(soundFile);
        if (clip == null) {
            //clip = new AudioNode(audioRenderer, assetManager, soundFile);
            clip = new AudioNode(assetManager, soundFile);
            clip.setLooping(true);
            clip.setPositional(false);
            audioMap.put(soundFile, clip);
        }

        if (clip.getStatus() != AudioSource.Status.Playing) {
            audioRenderer.playSource(clip);
        }
    }

    /**
     * Stop a sound file.
     *
     * @param soundFile
     */
    public void soundStop(String soundFile) {
        //soundManager.stop(soundFile);
        AudioNode clip = audioMap.get(soundFile);
        if (clip != null) {
            audioRenderer.stopSource(clip);
        }
    }

    /**
     * Load sound into memory
     * @param soundFile the file to load
     */
    public void soundLoad(String soundFile) {
        AudioNode clip = audioMap.get(soundFile);
        if (clip == null) {
            clip = new AudioNode(audioRenderer, assetManager, soundFile);
            audioMap.put(soundFile, clip);
        }
    }
    // Temporary fields related to picking
    private Ray ray = new Ray();
    private CollisionResults results = new CollisionResults();
    private List<EnvPickResult> envResults = new ArrayList<EnvPickResult>();

    private void pickHelper(int x, int y) {
        Vector2f pos = new Vector2f(x, y);
        Vector3f origin = cam.getWorldCoordinates(pos, 0.0f);
        Vector3f direction = cam.getWorldCoordinates(pos, 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        ray.setOrigin(origin);
        ray.setDirection(direction);
        results.clear();
        envResults.clear();

        rootNode.collideWith(ray, results);
    }

    /**
     * Get the closest object that is under the current mouse position.
     * Use the getMousePicList() method to return the entire list of objects
     * under the mouse, as well as the 3D position of each intersection.
     *
     * @return the first object directly under the mouse position, null if no object is picked
     */
    public Object getPick(int x, int y) {
        pickHelper(x, y);
        if (results.size() > 0) {
            String objName = results.getClosestCollision().getGeometry().getName();
            return internalObjects.get(objName.split(":")[0]);
        } else {
            return null;
        }
    }

    public List<EnvPickResult> getPickList(int x, int y) {
        pickHelper(x, y);
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                Vector3f contactPoint = results.getCollision(i).getContactPoint();
                String objName = results.getClosestCollision().getGeometry().getName();
                Object o = internalObjects.get(objName.split(":")[0]);
                if (o != null) {
                    EnvPickResult r = new EnvPickResult(contactPoint, o);
                    envResults.add(r);
                }
            }
        }
        return envResults;
    }
    private EnvRay envPickRay = new EnvRay();

    public EnvRay getPickRay(int x, int y) {
        Vector2f pos = new Vector2f(x, y);
        Vector3f origin = cam.getWorldCoordinates(pos, 0.0f);
        Vector3f direction = cam.getWorldCoordinates(pos, 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        envPickRay = new EnvRay();
        envPickRay.setDirection(direction);
        envPickRay.setOrigin(origin);
        return envPickRay;
    }
    
    private EnvVector screenCoord = new EnvVector();
    private Vector3f tempVec = new Vector3f(), retVec = new Vector3f();

    /**
     * returns the screen coordinate given a world coordinate
     * @param x
     * @param y
     * @param z
     * @param screenVector the content of this vector will contain the screen coordinate, 
     * the z value will always be 0
     */
    synchronized public void getScreenCoordinate(double x, double y, double z, EnvVector screenVector) {
        tempVec.set((float)x, (float)y, (float)z);
        cam.getScreenCoordinates(tempVec, retVec);
        screenVector.setX(retVec.x);
        screenVector.setY(retVec.y);
        screenVector.setZ(0);
        
    }    
    
    /**
     * Given a 3d point, find out the x coordinate on the 2d screen
     * @param x
     * @param y
     * @param z
     * @return the x coordinate on the 2d screen
     * @see #getScreenCoordinateY(double, double, double) 
     */
    public int getScreenCoordinateX(double x, double y, double z) {
        getScreenCoordinate(x, y, z, screenCoord);
        return (int)screenCoord.getX();
    }

    /**
     * Given a 3d point, find out the x coordinate on the 2d screen
     * @param x
     * @param y
     * @param z
     * @return the y coordinate on the 2d screen
     * @see #getScreenCoordinateX(double, double, double) 
     */
    public int getScreenCoordinateY(double x, double y, double z) {
        getScreenCoordinate(x, y, z, screenCoord);
        return (int)screenCoord.getY();
    }
    
    /**
     * Enable the lighting effect.  The light is a directional light
     * that comes from the camera
     */
    public void enableLighting() {
        if (!isLightingEnabled()) {
            directionalLight = new DirectionalLight();
            directionalLight.setColor(new ColorRGBA(0.8f, 0.8f, 0.8f, 1));
            directionalLight.setDirection(new Vector3f(0, -1, 0).normalize());
            rootNode.addLight(directionalLight);

            ambientLight = new AmbientLight();
            ambientLight.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
            rootNode.addLight(ambientLight);

            // Light all the objects
            for (GameObjectAdapter adapter : objectsMap.values()) {
                adapter.useLightingMaterial(true);
            }
            roomObject.useLightingMaterial(true);
        }
    }

    /**
     * Disable light effect
     */
    public void disableLighting() {
        if (directionalLight != null) {
            rootNode.removeLight(directionalLight);
            directionalLight = null;
        }
        if (ambientLight != null) {
            rootNode.removeLight(ambientLight);
            ambientLight = null;
        }
        for (GameObjectAdapter adapter : objectsMap.values()) {
            adapter.useLightingMaterial(false);
        }
        roomObject.useLightingMaterial(false);
    }

    /**
     * Check if lighting is enabled or not
     * @return true if lighting is currently enabled, false otherwise.
     */
    public boolean isLightingEnabled() {
        return (directionalLight != null && ambientLight != null);
    }

    /**
     * Returns the root node of the underlying scene graph.
     * @return 
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * Returns the nifty gui object for GUI programming
     * @return 
     */
    public Nifty getNiftyGUI() {

        if (nifty == null) {
            NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                    inputManager,
                    audioRenderer,
                    guiViewPort);
            nifty = niftyDisplay.getNifty();
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");

            guiViewPort.addProcessor(niftyDisplay);
        }
        // disable verbose logging
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE); 
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);         
        
        return nifty;
    }       
    
    /**
     * Exit the program cleanly.
     */
    public void exit() {
        destroy();
        System.exit(0);
    }
    // Internal jME3 related methods.

    @Override
    public void start(JmeContext.Type contextType) {

        if (context != null && context.isCreated()) {
            logger.warning("start() called when application already created!");
            return;
        }

        if (settings == null) {
            settings = new AppSettings(true);
        }

        logger.log(Level.FINE, "Starting application: {0}", getClass().getName());
        settings.setTitle("Env3D");
        //JmeSystem.showSettingsDialog(settings, true);
        context = JmeSystem.newContext(settings, contextType);
        context.setSystemListener(this);
        context.create(false);
    }

    @Override
    public void initialize() {
        super.initialize();

        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.1f, 1000f);
        GameObjectAdapter.assetManager = assetManager;
        assetManager.registerLocator(System.getProperty("user.dir"), FileLocator.class);
        assetManager.registerLocator("/", FileLocator.class);
        if (System.getProperty("org.lwjgl.librarypath") != null) {
            assetManager.registerLocator(System.getProperty("org.lwjgl.librarypath"), FileLocator.class);
        }
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        statusText = new BitmapText(guiFont, false);
        statusText.setName("status");
        statusText.setLocalTranslation(0, 20, 0);
        
        
        guiNode.setQueueBucket(Bucket.Gui);
        guiNode.setCullHint(CullHint.Never);
        guiViewPort.attachScene(guiNode);

        statusNode.setQueueBucket(Bucket.Gui);
        statusNode.setCullHint(CullHint.Never);
        guiViewPort.attachScene(statusNode);

        // attach box to display in primary viewport
        viewPort.attachScene(rootNode);

        init = true;

        if (inputManager != null) {
            flyCam = new EnvCamera(cam);
            flyCam.setMoveSpeed(10f);
            flyCam.setRotationSpeed(1);
            flyCam.registerWithInput(inputManager);
        }

    }

    @Override
    public void update() {

        //updateBlock.acquireUninterruptibly();
        synchronized (this) {
            super.update();
            // reset the collections that are returned by getObjects()
            classCollectionReturned.clear();            

            if (directionalLight != null) {
                directionalLight.setDirection(cam.getDirection());
            }

            // Show the FPS information
            if (showStatus) {
                statusText.setText("FPS: " + (int) timer.getFrameRate());
            }

            // Set the audio listener location to be where the camera is
            listener.setLocation(cam.getLocation());

            tpf = timer.getTimePerFrame();
            for (GameObjectAdapter gameObject : objectsMap.values()) {
                gameObject.update();
            }

            //tpf = timer.getTimePerFrame();
            stateManager.update(1);
            if (roomObject != null) {
                viewPort.setBackgroundColor(roomObject.getBackgroundColor());
            }
            // dont forget to update the scenes
            rootNode.updateLogicalState(tpf);
            rootNode.updateGeometricState();
            guiNode.updateLogicalState(tpf);
            guiNode.updateGeometricState();
            statusNode.updateLogicalState(tpf);
            statusNode.updateGeometricState();

            // render states
            stateManager.render(renderManager);
            if (context.isRenderable()) {
                renderManager.render(1, context.isRenderable());
            }
            stateManager.postRender();            // render the viewports
        }

        //updateBlock.release();
    }

    @Override
    public void destroy() {
        //super.destroy();
        // Copy the code from Application.destroy().  Have to eliminate the 
        // AudioRenderer cleanup, as it locks up computers at school.
        // School runs windows 7
        stateManager.cleanup();

        destroyInput();
//        if (audioRenderer != null)
//            audioRenderer.cleanup();

        timer.reset();
        // Somehow the super.destroy method does not destroy the display.
        //Display.destroy();
        logger.log(Level.INFO, "Env Destroy");
    }

    /**
     * Need to stop all audio before stopping the app.  Otherwise
     * looping music will keep playing in android.
     * 
     * @param waitFor 
     */
    @Override
    public void stop(boolean waitFor) {
        for (AudioNode node : audioMap.values()) {
            if (node.getStatus() == AudioSource.Status.Playing) {
                audioRenderer.stopSource(node);
            }
        }
        super.stop(waitFor);
    }
    
    
}
