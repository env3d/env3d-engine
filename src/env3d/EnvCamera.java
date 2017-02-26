/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package env3d;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.JoyInput;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author jmadar
 */
public class EnvCamera extends FlyByCamera {

    private Vector3f tmpVec = new Vector3f();
    private Vector3f initDir;

    public EnvCamera(Camera cam) {
        super(cam);
        initDir = cam.getDirection();
    }

    public void reset() {
        //rotateLeftRight(getCameraYaw());
        rotateUpDown(getPitch());
        rotateLeftRight(-1*getYaw());
    }

    public void setPitch(double angle) {
        rotateUpDown(getPitch()-angle);
    }
    
    public void setYaw(double angle) {
        rotateLeftRight(angle-getYaw());
    }

    public void rotateLeftRight(double angle) {
        // Reset and rotate
        //rotateCamera(-1*FastMath.DEG_TO_RAD*(float)getCameraYaw(), initialUpVec);
        rotateCamera(FastMath.DEG_TO_RAD*(float)angle, initialUpVec);
    }

    public void rotateUpDown(double angle) {
        //rotateCamera(-1*FastMath.DEG_TO_RAD*(float)getCameraPitch(), cam.getLeft());
        rotateCamera(FastMath.DEG_TO_RAD*(float)angle, cam.getLeft());
    }

    /**
     * Get the pitch (up and down) angle of the camera, in degrees.  This is
     * in relation to the default camera orientation (0, 0, -1)
     * @return the camera pitch in degrees
     */
    public double getPitch() {

        Vector3f dir = cam.getDirection();

        // Project direction onto the x-z plane
        tmpVec.x = dir.x;
        tmpVec.y = 0;
        tmpVec.z = dir.z;
        tmpVec.normalizeLocal();

        int d = dir.y > 0 ? 1 : -1;

        // return the angle between dir and v1
        return FastMath.RAD_TO_DEG*(dir.angleBetween(tmpVec)) * d;

    }

    /**
     * Get the yaw (up and down) angle of the camera, in degrees.  This is
     * in relation to the default camera orientation (0, 0, -1)
     * @return the camera yaw in degrees
     */
    public double getYaw() {
        Vector3f dir = cam.getDirection();

        // Project direction onto the x-z plane
        tmpVec.x = dir.x;
        tmpVec.y = 0;
        tmpVec.z = dir.z;
        tmpVec.normalizeLocal();

        int d = dir.x > 0 ? -1 : 1;

        // return the angle between dir and v1
        return FastMath.RAD_TO_DEG*(initDir.angleBetween(tmpVec) * d);
    }

    public void registerWithInput(InputManager inputManager){
        this.inputManager = inputManager;

        String[] mappings = new String[]{
            "FLYCAM_Left",
            "FLYCAM_Right",
            "FLYCAM_Up",
            "FLYCAM_Down",

            "FLYCAM_StrafeLeft",
            "FLYCAM_StrafeRight",
            "FLYCAM_Forward",
            "FLYCAM_Backward",

            "FLYCAM_ZoomIn",
            "FLYCAM_ZoomOut",
            "FLYCAM_RotateDrag",

            "FLYCAM_Rise",
            "FLYCAM_Lower"
        };

        // both mouse and button - rotation of cam
        inputManager.addMapping("FLYCAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true));

        inputManager.addMapping("FLYCAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false));

        inputManager.addMapping("FLYCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false));

        inputManager.addMapping("FLYCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        //inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_C));

        inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(dragToRotate);
    }

    @Override
    public void onAction(String name, boolean value, float tpf) {
        //super.onAction(name, value, tpf);
    }



}
