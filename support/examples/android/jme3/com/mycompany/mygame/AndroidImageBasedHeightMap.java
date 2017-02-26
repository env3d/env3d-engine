package com.mycompany.mygame;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.jme3.asset.AndroidImageInfo;
import com.jme3.math.ColorRGBA;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Image;
import java.nio.ByteBuffer;

/**
 * An android specific heightmap to fix the bug in the jME
 * implementation
 * 
 * @author jmadar
 */
public class AndroidImageBasedHeightMap extends ImageBasedHeightMap {

    public AndroidImageBasedHeightMap() {
        super(null);
    }
    
    public AndroidImageBasedHeightMap(Image colorImage) {
        super(colorImage);
    }

    public AndroidImageBasedHeightMap(Image colorImage, float heightScale) {
        super(colorImage, heightScale);
    }

//    @Override
//    protected float getHeightAtPostion(ByteBuffer buf, Image image, int position, ColorRGBA store) {
//        if (buf != null) {
//            return super.getHeightAtPostion(buf, image, position, store);
//        } else {
//            AndroidImageInfo imageInfo = (AndroidImageInfo) image.getEfficentData();
//            Bitmap bitmap = imageInfo.getBitmap();
//            int y = position / bitmap.getWidth();
//            int x = position - (y * bitmap.getWidth());
//            int color = bitmap.getPixel(x, y);
//            float h = Color.red(color) * heightScale;
//            //System.out.println(h);
//            return h;
//        }
//    }

}
