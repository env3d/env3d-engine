package env3d.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class TextureManager {

	private static HashMap<String, Integer> textureCache = new HashMap<String, Integer>();
	
	/**
	 * Load the textures
	 * 
	 * @param gl - The GL Context
	 * @param context - The Activity context
	 */
	public static int loadGLTexture(GL10 gl, AssetManager asset, String fileName) {
		
		if (textureCache.get(fileName) != null) {
			return textureCache.get(fileName);
		}
		
		int[] textures = new int[1];
		//Get the texture from the Android resource directory
		InputStream is = null;
		try {
			is = asset.open(fileName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);			
		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//Generate there texture pointer
		gl.glGenTextures(1, textures, 0);

		//Create Nearest Filtered Texture and bind it to texture 0
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

//		//Create Linear Filtered Texture and bind it to texture 1
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
//
//		//Create mipmapped textures and bind it to texture 2
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
		/*
		 * This is a change to the original tutorial, as buildMipMap does not exist anymore
		 * in the Android SDK.
		 * 
		 * We check if the GL context is version 1.1 and generate MipMaps by flag.
		 * Otherwise we call our own buildMipMap implementation
		 */
//		if(gl instanceof GL11) {
//			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
//			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
//			
//		//
//		} else {
//			buildMipmap(gl, bitmap);
//		}		
		
		//Clean up
		bitmap.recycle();
		textureCache.put(fileName, textures[0]);
		return textures[0];
	}
	
	/**
	 * Our own MipMap generation implementation.
	 * Scale the original bitmap down, always by factor two,
	 * and set it as new mipmap level.
	 * 
	 * Thanks to Mike Miller (with minor changes)!
	 * 
	 * @param gl - The GL Context
	 * @param bitmap - The bitmap to mipmap
	 */
	private static void buildMipmap(GL10 gl, Bitmap bitmap) {
		//
		int level = 0;
		//
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		//
		while(height >= 1 || width >= 1) {
			//First of all, generate the texture from our bitmap and set it to the according level
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			
			//
			if(height == 1 || width == 1) {
				break;
			}

			//Increase the mipmap level
			level++;

			//
			height /= 2;
			width /= 2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
			
			//Clean up
			bitmap.recycle();
			bitmap = bitmap2;
		}
	}
	
	public static void clear() {
		textureCache = new HashMap<String, Integer>();
	}
}
