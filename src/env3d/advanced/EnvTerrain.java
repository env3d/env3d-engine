/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.advanced;

import com.jme3.asset.TextureKey;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.picking.BresenhamTerrainPicker;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.terrain.heightmap.RawHeightMap;
import com.jme3.texture.Texture;
import java.util.ArrayList;

/**
 *
 * @author jmadar
 */
public class EnvTerrain extends EnvAbstractNode {

    protected Material mat_terrain, mat_terrainLighting;
    protected TerrainQuad terrain;
    private Vector2f loc = new Vector2f();
    private BresenhamTerrainPicker picker;
    private String heightMapFile;
    private String alphaMapFile;
    // The different texture channels.  R, G, and B.  
    private String texture, textureG, textureB;

    public EnvTerrain(double[] heightMap) {
        float[] map = new float[heightMap.length];
        for (int i = 0; i < heightMap.length; i++) {
            map[i] = (float) heightMap[i];
        }
        AbstractHeightMap hm = new RawHeightMap(map);
        //hm.load();
        hm.smooth(0.9f, 1);
        generateTerrain(hm.getHeightMap());
        heightMapFile = null;
    }

    public EnvTerrain(String imageFile) {
        heightMapFile = imageFile;
        generateTerrain(getHeightMapFromFile(imageFile));
    }

    private float[] getHeightMapFromFile(String imageFile) {
        TextureKey tkey = new TerrainTextureKey(imageFile);        
        Texture heightMapImage =
                assetManager.loadTexture(tkey);
        ImageBasedHeightMap heightmap;
        try {
            Class c = Class.forName("com.mycompany.mygame.AndroidImageBasedHeightMap");
            heightmap = (ImageBasedHeightMap) c.newInstance();
            heightmap.setImage(heightMapImage.getImage());            
        } catch (Exception ex) {
            //ex.printStackTrace();
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        }
        heightmap.load();
        heightmap.smooth(0.9f, 1);
        return heightmap.getHeightMap();
    }

    private void generateTerrain(float[] heightMap) {
        int side = (int) Math.sqrt(heightMap.length);
        terrain = new TerrainQuad("my terrain", 65, side + 1, heightMap);

        mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        mat_terrainLighting = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        mat_terrainLighting.setColor("Diffuse", new ColorRGBA(0.2f, 0.2f, 0.2f, 1));
        mat_terrainLighting.setColor("Ambient", new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
        mat_terrainLighting.setColor("Specular", new ColorRGBA(0.2f, 0.2f, 0.2f, 1));
        mat_terrainLighting.setFloat("Shininess", 0.1f);
        
        // Use the default alpha texture
        setTextureAlpha("textures/terrain/alpha.png");

        if (lighting) {
            terrain.setMaterial(mat_terrainLighting);
        } else {
            terrain.setMaterial(mat_terrain);
        }
        //terrain.setMaterial(mat);

        jme_node.detachAllChildren();
        jme_node.setMaterial(null);
        jme_node.attachChild(terrain);
        picker = new BresenhamTerrainPicker(terrain);
    }

    public void updateHeightMap(String imageFile) {
        heightMapFile = imageFile;
        float[] heightMapArray = getHeightMapFromFile(imageFile);
        ArrayList<Vector2f> coordList = new ArrayList<Vector2f>(heightMapArray.length);
        ArrayList<Float> heightList = new ArrayList<Float>(heightMapArray.length);
        int width = (int) Math.sqrt(heightMapArray.length);
        int offset = width / 2;
        for (int i = 0; i < heightMapArray.length; i++) {
            coordList.add(new Vector2f( (i % width) - offset, (i / width) - offset));
            heightList.add(heightMapArray[i]);
        }

        terrain.setHeight(coordList, heightList);
        terrain.updateModelBound();
        terrain.updateGeometricState();
    }


    public void setTexture(String texture) {
        this.texture = texture;
        this.textureG = texture;
        this.textureB = texture;

        Texture t1 = assetManager.loadTexture(texture);
        t1.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", t1);
        mat_terrain.setFloat("Tex1Scale", 64f);

        mat_terrainLighting.setTexture("DiffuseMap", t1);
        mat_terrainLighting.setFloat("DiffuseMap_0_scale", 64f);

        Texture t2 = assetManager.loadTexture(texture);
        t2.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", t2);
        mat_terrain.setFloat("Tex2Scale", 64f);

        Texture t3 = assetManager.loadTexture(texture);
        t3.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", t3);
        mat_terrain.setFloat("Tex3Scale", 64f);


        //mat.setTexture("ColorMap", t1);
    }

    public void setTextureAlpha(String texture) {
        alphaMapFile = texture;
        TextureKey tk = new TerrainTextureKey(texture);
        Texture alpha = assetManager.loadTexture(tk);
        alpha.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Alpha", alpha);
        mat_terrainLighting.setTexture("AlphaMap", alpha);
    }

    /**
     * Sets the texture channel for splat texture on this terrain.
     * 
     * @param texture
     * @param channel 1 is red, 2 is blue, 3 is green
     */
    public void setTexture(String texture, int channel) {
        Texture t = assetManager.loadTexture(texture);
        t.setWrap(Texture.WrapMode.Repeat);
        String textParam = null;
        String textParamScale = null;
        String textParamLighting = null;
        String textParamScaleLighting = null;
        if (channel == 1) {
            textParam = "Tex1";
            textParamLighting = "DiffuseMap";
            textParamScale = "Tex1Scale";
            textParamScaleLighting = "DiffuseMap_0_scale";
            this.texture = texture;
        } else if (channel == 2) {
            textParam = "Tex2";
            textParamLighting = "DiffuseMap_1";
            textParamScale = "Tex2Scale";
            textParamScaleLighting = "DiffuseMap_1_scale";
            this.textureG = texture;
        } else {
            textParam = "Tex3";
            textParamLighting = "DiffuseMap_2";
            textParamScale = "Tex3Scale";
            textParamScaleLighting = "DiffuseMap_2_scale";
            this.textureB = texture;
        }
        mat_terrain.setTexture(textParam, t);
        mat_terrain.setFloat(textParamScale, 64f);
        mat_terrainLighting.setTexture(textParamLighting, t);
        mat_terrainLighting.setFloat(textParamScaleLighting, 64f);
    }

    public void setScale(double x, double y, double z) {
        terrain.setLocalScale((float) x, (float) y, (float) z);
    }

    /**
     * Returns the height of a particular point of the terrain
     * @param x
     * @param z
     * @return 
     */
    public float getHeight(double x, double z) {

        // Bypass the internal getHeight method.  Does not work when terrain is 
        // translated.
        //        loc.x = (float)x;
        //        loc.y = (float)z;
        //        return terrain.getHeight(loc);

        // create a vertical ray that intersects with the terrain,
        // then pick the triangle and return the height.
        Ray ray = new Ray(new Vector3f((float) x, Float.MIN_VALUE, (float) z), Vector3f.UNIT_Y);
        CollisionResults results = new CollisionResults();
        picker.getTerrainIntersection(ray, results);
        if (results.size() > 0) {
            return results.getClosestCollision().getContactPoint().y;
        } else {
            return 0;
        }

    }

    /**
     * Returns the normal vector for a point on the terrain
     *
     * @return 
     */
    public Vector3f getNormal(double x, double z) {
        // create a vertical ray that intersects with the terrain,
        // then pick the triangle and return the normal.
        Ray ray = new Ray(new Vector3f((float) x, Float.MIN_VALUE, (float) z), Vector3f.UNIT_Y);
        CollisionResults results = new CollisionResults();
        picker.getTerrainIntersection(ray, results);
        if (results.size() > 0) {
            return results.getClosestCollision().getContactNormal();
        } else {
            return null;
        }

    }

    @Override
    public void useLightingMaterial(boolean lighting) {
        super.useLightingMaterial(lighting);
        if (lighting) {
            terrain.setMaterial(mat_terrainLighting);
        } else {
            terrain.setMaterial(mat_terrain);
        }
    }    
    
    /**
     * @return the heightMapFile
     */
    public String getHeightMapFile() {
        return heightMapFile;
    }
    
    public String getAlphaMapFile() {
        return alphaMapFile;
    }

    /**
     * @return the texture
     */
    public String getTexture() {
        return texture;
    }
    
    public String getTextureB() {
        return textureB;
    }
    
    public String getTextureG() {
        return textureG;
    }

    public TerrainQuad getTerrainQuad() {
        return terrain;
    }
    
    private class TerrainTextureKey extends TextureKey {

        public TerrainTextureKey(String name) {
            super(name);
        }

        public boolean shouldCache() {
            return false;
        }
       
    }
}
