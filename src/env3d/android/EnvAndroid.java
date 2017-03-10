/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.android;

import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import env3d.EnvBasic;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The base class for all android based games.
 * 
 * @author jmadar
 * @deprecated 
 */
public class EnvAndroid extends EnvBasic {

    private double tiltX, tiltY, tiltZ;
    private int touchX, touchY, touchDX, touchDY;
    private int screenHeight;
    private int screenWidth;
    private boolean touchEvent = false;
    private boolean showController;
    private boolean showAd = false;
    private String adUnitId = "a151648305dff97";
    protected EnvMobileGame game;
    protected boolean emulatorMode = false;

    protected EnvDefaultController controller;

    public EnvAndroid() {
        Logger.getLogger("com").setLevel(Level.SEVERE);
        Logger.getLogger("env3d").setLevel(Level.SEVERE);
        
        try {
            Class.forName("com.mycompany.mygame.MainActivity");
        } catch (ClassNotFoundException ex) {
            emulatorMode = true;
            // Workaround for BlueJ
//            try {
//                Thread.currentThread().setContextClassLoader(Class.forName("Game").getClassLoader());
//            } catch (ClassNotFoundException ex1) {
//                Logger.getLogger(EnvAndroid.class.getName()).log(Level.SEVERE, null, ex1);
//            }
        }
        settings = new AppSettings(true);
        settings.setFrameRate(30);

        
    }

    public void setTilt(double x, double y, double z) {
        tiltX = x;
        tiltY = y;
        tiltZ = z;
    }

    @Override
    public void update() {
        super.update();
        game.loop();
    }

    @Override
    public void initialize() {
        super.initialize();
        setDefaultControl(false);

        // Make sure that the screen is always portrait
        screenHeight = settings.getHeight();
        screenWidth = settings.getWidth();
        if (screenWidth < screenHeight) {
            screenHeight = settings.getWidth();
            screenWidth = settings.getHeight();
        }
        EnvDefaultController.assetManager = assetManager;
        controller = new EnvDefaultController();
        
        setShowController(true);        
        game.setup();
    }

    public boolean getKeyDown(String keycode) {
        return controller.getKeyDown(keycode);
    }

    /**
     * @return the showController
     */
    public boolean isShowController() {
        return showController;
    }

    /**
     * @param showController the showController to set
     */
    public void setShowController(boolean showController) {
        this.showController = showController;
        if (showController) {
            // Show the png for the current controller
            inputManager.addRawInputListener(inputListener);
            Picture controllerPic = new Picture("Controller");
                        
            controllerPic.setImage(assetManager, controller.getLayout(), true);
            
            controllerPic.setWidth(getScreenWidth());
            controllerPic.setHeight(getScreenHeight());

            controllerPic.setPosition(0, 0);
            guiNode.attachChild(controllerPic);

        } else {
            guiNode.detachChildNamed("Controller");
        }
    }

    /**
     * @return the tiltX
     */
    public double getTiltX() {
        return tiltX;
    }

    /**
     * @return the tiltY
     */
    public double getTiltY() {
        return tiltY;
    }

    /**
     * @return the tiltZ
     */
    public double getTiltZ() {
        return tiltZ;
    }

    /**
     * @return the game
     */
    public EnvMobileGame getGame() {
        return game;
    }

    /**
     * @param game the game to set
     */
    public void setGame(EnvMobileGame game) {
        this.game = game;
    }

    /**
     * @return the touchX
     */
    public int getTouchX() {
        return touchX;
    }

    /**
     * @return the touchY
     */
    public int getTouchY() {
        return touchY;
    }

    /**
     * @return the screenHeight
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * @return the screenWidth
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * @return the touchEvent
     */
    public boolean isTouchEvent() {
        return touchEvent;
    }

    /**
     * @return the touchDX
     */
    public int getTouchDX() {
        return touchDX;
    }

    /**
     * @return the touchDY
     */
    public int getTouchDY() {
        return touchDY;
    }
    
    private RawInputListener inputListener = new RawInputListener() {

        private int timer = 0;
        
        public void beginInput() {
        }

        public void endInput() {
            if (touchDX != 0 || touchDY != 0) {
                if (timer < 2) {
                    timer++;
                } else {
                    touchDX = 0;
                    touchDY = 0;
                    timer = 0;
                }
            }
        }

        public void onJoyAxisEvent(JoyAxisEvent evt) {
        }

        public void onJoyButtonEvent(JoyButtonEvent evt) {
        }

        public void onMouseMotionEvent(MouseMotionEvent evt) {
            if (!emulatorMode) {
                return;
            }
            
            if (touchEvent) {
                touchDX = evt.getDX();
                touchDY = evt.getDY();
            }
            
            if (showController && touchEvent) {
                touchX = evt.getX();
                touchY = evt.getY();
                for (KeyMap key : controller.getKeyMappings().values()) {
                    if (key.pressed(touchX / (double) screenWidth, touchY / (double) screenHeight)) {
                        key.down = true;                            
                    } else {
                        key.down = false;
                    }
                }
            }            
        }

        public void onMouseButtonEvent(MouseButtonEvent evt) {
            if (!emulatorMode) {
                return;
            }

            if (evt.isPressed()) {
                touchEvent = true;
                touchX = evt.getX();
                touchY = evt.getY();

                if (showController) {
                    for (KeyMap key : controller.getKeyMappings().values()) {
                        if (key.pressed(touchX / (double) screenWidth, touchY / (double) screenHeight)) {
                            key.down = true;                            
                        } else {
                            key.down = false;
                        }
                    }
                }

            }
            if (evt.isReleased()) {                
                touchEvent = false;
                touchX = evt.getX();
                touchY = evt.getY();
                touchDX = 0;
                touchDY = 0;

                if (showController) {
                    for (KeyMap key : controller.getKeyMappings().values()) {
                        key.down = false;
                    }
                }
                
                touchX = -1;
                touchY = -1;

            }

        }

        public void onKeyEvent(KeyInputEvent evt) {
        }

        public void onTouchEvent(TouchEvent evt) {
//            System.out.println(evt.getPointerId()+" "+evt.getType()+" "+evt.getX()+" "+evt.getY()+" "+
//                    evt.getDeltaX()+" "+evt.getDeltaY());

            double x = evt.getX() / (double) getScreenWidth();
            double y = evt.getY() / (double) getScreenHeight();

            boolean buttonTouched = false;

            if (showController) {
                for (KeyMap key : controller.getKeyMappings().values()) {
                    if (key.pressed(x, y)) {
                        buttonTouched = true;
                        if ((evt.getType() == TouchEvent.Type.DOWN || evt.getType() == TouchEvent.Type.MOVE)) {
                            if (key.pointerId == -1) {
                                key.down = true;
                                key.pointerId = evt.getPointerId();
                            }

                            Picture pointer = (Picture) guiNode.getChild("Pointer " + evt.getPointerId());
                            if (pointer == null) {
                                pointer = new Picture("Pointer " + evt.getPointerId());
                                pointer.setImage(assetManager, "textures/red.png", true);
                                pointer.setHeight(getScreenHeight() / 20);
                                pointer.setWidth(getScreenWidth() / 20);
                                guiNode.attachChild(pointer);
                            }
                            pointer.setPosition(evt.getX() - (getScreenWidth() / 40), evt.getY());
                        }
                    } else {
                        if (evt.getType() == TouchEvent.Type.MOVE
                                && key.pointerId == evt.getPointerId()) {
                            key.down = false;
                            key.pointerId = -1;
                            guiNode.detachChildNamed("Pointer " + evt.getPointerId());
                        }
                    }
                }

                if (evt.getType() == TouchEvent.Type.UP) {
                    for (KeyMap key : controller.getKeyMappings().values()) {
                        if (key.pointerId == evt.getPointerId()) {
                            key.down = false;
                            key.pointerId = -1;
                            guiNode.detachChildNamed("Pointer " + evt.getPointerId());
                        }
                    }
                }
            }

            // Track single touch movement
            if (!buttonTouched) {
                if (evt.getType() == TouchEvent.Type.DOWN) {
                    touchEvent = true;
                    touchX = (int) evt.getX();
                    touchY = (int) evt.getY();
                    touchDX = 0;
                    touchDY = 0;
                } else if (evt.getType() == TouchEvent.Type.UP) {
                    touchEvent = false;
                    touchDX = 0;
                    touchDY = 0;
                } else if (evt.getType() == TouchEvent.Type.MOVE) {
//                    touchDX = (int) evt.getX() - touchX;
//                    touchDY = (int) evt.getY() - touchY;  
                    touchDX = (int) evt.getDeltaX();
                    touchDY = (int) evt.getDeltaY();
                    touchX = (int) evt.getX();
                    touchY = (int) evt.getY();
                } 
            }
        }
    };

    /**
     * @return the showAd
     */
    public boolean isShowAd() {
        return showAd;
    }

    /**
     * Shows an AdMob advertisement banner on screen.  The banner
     * will have an "x" button so users can dismiss it.
     * @param showAd the showAd to set
     */
    public void setShowAd(boolean showAd) {
        this.showAd = showAd;
    }

    /**     
     * @return the adUnitId
     */
    public String getAdUnitId() {
        return adUnitId;
    }

    /**
     * Set your own Ad Unit Id so you can start collecting revenue.  You must
     * set this in the setup method for this to take effect.
     * 
     * Note: The default ad unit id uses the env3d test account.
     * @param adUnitId the adUnitId to set
     */
    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }
}
