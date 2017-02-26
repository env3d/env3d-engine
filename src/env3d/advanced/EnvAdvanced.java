/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.advanced;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer.CompareMode;
import com.jme3.shadow.PssmShadowRenderer.FilterMode;
import env3d.Env;

/**
 * The EnvAdvanced class allows the use of advanced features such as
 * skybox, water, shadow, physics, and gui.
 * 
 * @author jmadar
 */
public class EnvAdvanced extends Env {

    private BulletAppState bulletAppState;
    private PssmShadowRenderer shadowRenderer;
    private Node skyNode = new Node("SkyNode");

    public EnvAdvanced() {
        super(true);

        // Skybox
        rootNode.detachAllChildren();
        skyNode.setCullHint(CullHint.Never);
        rootNode.attachChild(skyNode);

        // Physics and shadows
//        bulletAppState = new BulletAppState();
//        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
//        stateManager.attach(bulletAppState);
//        bulletAppState.setEnabled(false);

//        shadowRenderer = new BasicShadowRenderer(assetManager, 256);
        shadowRenderer = new PssmShadowRenderer(assetManager, 1024, 3);
        shadowRenderer.setDirection(new Vector3f(1, -0.5f, 1).normalizeLocal());
        shadowRenderer.setLambda(0.55f);
        shadowRenderer.setShadowIntensity(0.6f);
        shadowRenderer.setCompareMode(CompareMode.Hardware);
        shadowRenderer.setFilterMode(FilterMode.Bilinear);
        //pssmRenderer.displayDebug();
        rootNode.setShadowMode(ShadowMode.Off);

        //viewPort.attachScene(skyNode);

    }

    /**
     * Enable physics?
     * @param enabled 
     */
    public void setPhysicsEnabled(boolean enabled) {
        getBulletAppState().setEnabled(enabled);
    }

    @Override
    public void setRoom(Object room) {
        if (room instanceof EnvSkyRoom) {
            // Reset all nodes
            synchronized (this) {
                rootNode.detachAllChildren();
                skyNode.detachAllChildren();
                objectsMap.clear();
                internalObjects.clear();

                roomObject = (EnvSkyRoom) room;
                skyNode.attachChild(((EnvSkyRoom) room).getJme_node());
                rootNode.attachChild(skyNode);
            }
        } else {
            super.setRoom(room);
        }

    }

    @Override
    public void addObject(Object obj) {
        if (obj instanceof EnvWater) {
            EnvWater water = (EnvWater) obj;
            water.getWaterProcessor().setReflectionScene(rootNode);
            viewPort.addProcessor(water.getWaterProcessor());
        }

        if (obj instanceof EnvNode) {
            if (obj instanceof EnvWater || obj instanceof EnvTerrain) {
                ((EnvNode) obj).getJme_node().setShadowMode(ShadowMode.Receive);
            } else {
                ((EnvNode) obj).getJme_node().setShadowMode(ShadowMode.CastAndReceive);
            }
        }
        if (obj instanceof EnvPhysicsNode) {
            getBulletAppState().getPhysicsSpace().addAll(((EnvPhysicsNode) obj).getJme_node());
        }

        super.addObject(obj);
    }

    @Override
    public void removeObject(Object obj) {
        if (obj instanceof EnvWater) {
            EnvWater water = (EnvWater) obj;
            viewPort.removeProcessor(water.getWaterProcessor());
        }
        if (obj instanceof EnvPhysicsNode) {
            getBulletAppState().getPhysicsSpace().removeAll(((EnvPhysicsNode) obj).getJme_node());
        }
        super.removeObject(obj);
    }

    @Override
    public void update() {
        skyNode.setLocalTranslation(cam.getLocation());
        super.update();
    }

    /**
     * Turn shadows on and off
     * @param mode 
     */
    public void setShadowEnabled(boolean mode) {
        if (mode) {
            synchronized (this) {
                viewPort.addProcessor(shadowRenderer);
            }
        } else {
            synchronized (this) {
                viewPort.removeProcessor(shadowRenderer);
            }
        }
    }

    /**
     * Checks if shadow is enabled
     * @return 
     */
    public boolean isShadowEnabled() {
        return viewPort.getProcessors().contains(shadowRenderer);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }



    /**
     * @return the bulletAppState
     */
    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }
}
