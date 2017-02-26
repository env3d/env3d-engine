/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package env3d.advanced;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;

/**
 *
 * @author jmadar
 */
public class EnvWater extends EnvAbstractNode {

    protected SimpleWaterProcessor waterProcessor;
    private Vector3f waterNormal = new Vector3f();
    private Geometry waterGeom;
    private Mesh waterMesh;

    
    public EnvWater() {
        waterProcessor = new SimpleWaterProcessor(assetManager);
        waterMesh = new Quad(1,1);
        waterGeom = new Geometry("waterQuad", waterMesh);
        waterGeom.setMaterial(waterProcessor.getMaterial());

        jme_node.attachChild(waterGeom);
        updateWaterProcessor();
    }

    private void updateWaterProcessor() {
        waterNormal.set(0, 0, 1);
        jme_node.getLocalRotation().multLocal(waterNormal);
        waterNormal.normalizeLocal();
        waterProcessor.setPlane(jme_node.getLocalTranslation(), waterNormal);
    }

    public void setWaterDepth(double d) {
        waterProcessor.setWaterDepth((float) d);
    }

    public void setWaveStrength(double s) {
        waterProcessor.setDistortionScale((float) s);
    }

    public void setWaveSpeed(double s) {
        waterProcessor.setWaveSpeed((float) s);
    }

    public void setWaveSize(double s) {
        waterMesh.scaleTextureCoordinates(new Vector2f( (float) s, (float) s));
    }

    public SimpleWaterProcessor getWaterProcessor() {        
        return waterProcessor;
    }

    @Override
    public void updateRotation() {
        super.updateRotation();
        updateWaterProcessor();
    }

    @Override
    public void setX(double x) {
        super.setX(x);
        updateWaterProcessor();
    }

    @Override
    public void setY(double y) {
        super.setY(y);
        updateWaterProcessor();
    }

    @Override
    public void setZ(double z) {
        super.setZ(z);
        updateWaterProcessor();
    }


}
