package test.picking;

import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import env3d.Env;
import env3d.advanced.EnvNode;

/**
 * The phone object creates a quad with proper on-screen dimensions
 * that matches the physical dimensions of a real phone
 * @author jmadar
 */
public class Phone extends EnvNode
{
    // visible region on the 0,0 plane
    private float visiblePlaneWidth;
    private float visiblePlaneHeight;        
    
    public float getMonitorHeight() {
        return (float) Math.sqrt(Math.pow(Game.MONITOR_SIZE,2)-Math.pow(getMonitorWidth(),2));
    }
    
    public float getMonitorWidth() {
        float ratioW = Game.SCREEN_WIDTH/(float)Game.SCREEN_HEIGHT;
        return (float) Math.sqrt((Math.pow(Game.MONITOR_SIZE,2)*Math.pow(ratioW,2))/(Math.pow(ratioW,2)+Math.pow(1,2)));        
    }
        
    public Phone(Env env)            
    {        
        Camera cam = env.getCamera();
        // Determine the width and height of the 3d screen space by getting the
        // top_left and bottom_right vectors from screen coordinates 0,0 and 
        // screenWidth, screenHeight
        Vector3f topLeft = cam.getWorldCoordinates(new Vector2f(0, 0), 0);
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(0, 0), 1f).subtractLocal(topLeft).normalizeLocal();
        Ray ray = new Ray(topLeft, dir);        
        ray.intersectsWherePlane(new Plane(Vector3f.UNIT_Z,Vector3f.ZERO.dot(Vector3f.UNIT_Z)),topLeft);
        
        Vector3f bottomRight = cam.getWorldCoordinates(new Vector2f(env.getScreenWidth(), env.getScreenHeight()), 0);
        dir = cam.getWorldCoordinates(new Vector2f(env.getScreenWidth(), env.getScreenHeight()), 1f).subtractLocal(bottomRight).normalizeLocal();
        ray = new Ray(bottomRight, dir);                      
        ray.intersectsWherePlane(new Plane(Vector3f.UNIT_Z,Vector3f.ZERO.dot(Vector3f.UNIT_Z)),bottomRight);
        
        //System.out.println(topLeft+" "+bottomRight);
        
        // We now have the boundary coordinates for plane -- can calcuate
        // the visible part of the plane
        visiblePlaneWidth = bottomRight.x - topLeft.x;
        visiblePlaneHeight = bottomRight.y - topLeft.y;     
                
        // calculate the ratio for height and width        
        float h = Game.PHONE_HEIGHT;// / getMonitorHeight();
        float w = Game.PHONE_WIDTH;// / getMonitorWidth();                     
        
        // multipler to adjust for screen ratio
        float hMult = visiblePlaneHeight/ getMonitorHeight();
        float wMult = visiblePlaneWidth / getMonitorWidth();
        
        System.out.println("3D screen size " +visiblePlaneWidth+" "+visiblePlaneHeight);        
        
        // Allow alpha channel on the texture
        setTransparent(true);
        setTexture("textures/phone.png");        
        jme_node.detachAllChildren();    
        
        // Create the quad
        jme_node.attachChild(new Geometry("phone", new Quad(w * wMult, h * hMult)));        
    }
}
