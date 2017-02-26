package env3d.advanced;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainQuad;
import env3d.advanced.EnvAbstractNode;
import env3d.advanced.EnvNode;
import env3d.advanced.EnvTerrain;

/**
 *
 * @author jmadar
 */
public class EnvPhysicsNode extends EnvNode {
   
    protected RigidBodyControl physicsControl;
    private Vector3f location = new Vector3f();
    private float [] angles = new float[3];  
    
    public EnvPhysicsNode() {
        physicsControl = new RigidBodyControl(0);
        jme_node.addControl(physicsControl);
    }

    public EnvPhysicsNode(EnvAbstractNode n) {
        super();                
        setTexture(null);
        jme_node.detachAllChildren();
        physicsControl = new RigidBodyControl(0);
        if (n instanceof EnvTerrain) {
            jme_node.setMaterial(null);
            TerrainQuad terrainQuad = (TerrainQuad) n.getJme_node().getChild(0);
            jme_node.attachChild(terrainQuad);
            terrainQuad.addControl(physicsControl);
        } else {
            jme_node.attachChild(n.getJme_node().getChild(0));
            jme_node.addControl(physicsControl);
        }
    }
        
    public void setMass(double mass) {
        physicsControl.setMass((float) mass);
        resetCollisionShape();
    }
    
    public void setGravity(double x, double y, double z) {
        physicsControl.setGravity(new Vector3f((float)x, (float)y, (float) z));
    }

    public void setRestitution(double r) {
        physicsControl.setRestitution((float)r);
    }
    
    public void setFriction(double f) {
        physicsControl.setFriction((float)f);
    }
    
    public void setLinearVelocity(double x, double y, double z) {
        physicsControl.setLinearVelocity(new Vector3f((float)x, (float)y, (float) z));
    }
    
    public void setX(double x) {
        physicsControl.getPhysicsLocation(location);
        location.x = (float)x;
        physicsControl.setPhysicsLocation(location);
    }
    
    public void setY(double y) {
        physicsControl.getPhysicsLocation(location);
        location.y = (float)y;
        physicsControl.setPhysicsLocation(location);
    }
    
    public void setZ(double z) {
        physicsControl.getPhysicsLocation(location);
        location.z = (float)z;
        physicsControl.setPhysicsLocation(location);
    }

    public void setScale(double x, double y, double z) {
        jme_node.setLocalScale((float) x, (float) y, (float) z);
        resetCollisionShape();
    }
    
    public void setScale(double scale) {
        super.setScale(scale);
        resetCollisionShape();
    }
    
    public void setModel(String model) {
        super.setModel(model);
        resetCollisionShape();
    }
    
    public Vector3f getLocation() {
        return physicsControl.getPhysicsLocation();
    }
    
    public void setLocation(double x, double y, double z) {
        physicsControl.setPhysicsLocation(new Vector3f((float)x, (float)y, (float)z));
    }    
    
    public double getRotateX() {
        updatePhysicsRotation();
        return angles[0];
    }
    
    public double getRotateY() {
        updatePhysicsRotation();
        return angles[1];
    }
    
    public double getRotateZ() {
        updatePhysicsRotation();
        return angles[2];
    }
        
    public void setCollisionShape(CollisionShape s) {
        physicsControl.setCollisionShape(s);
    }
    
    private void updatePhysicsRotation() {        
        physicsControl.getPhysicsRotation().toAngles(angles);
    }
    
    private void resetCollisionShape() {
        if (physicsControl.getMass() > 0) {
            physicsControl.setCollisionShape(CollisionShapeFactory.createDynamicMeshShape(jme_node));
        } else {
            physicsControl.setCollisionShape(CollisionShapeFactory.createMeshShape(jme_node));
        }        
    }        
    
    /**
     * @return the control
     */
    public RigidBodyControl getControl() {
        return physicsControl;
    }    
    
}
