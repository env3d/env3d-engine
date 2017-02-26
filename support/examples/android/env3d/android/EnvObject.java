package env3d.android;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;

public class EnvObject {
	protected double x, y, z, scale;
	protected double rotateX, rotateY, rotateZ;
	protected String texture, model;
	
	/**
	 * Draw this object in opengl.
	 * 
	 * @param gl the opengl context to draw
	 * @throws Exception 
	 */
	public void draw(AssetManager asset, GL10 gl) throws Exception {
		gl.glPushMatrix();
		gl.glColor4f(1, 1, 1, 1);
		gl.glTranslatef((float) x, (float) y, (float) z);
		gl.glRotatef((float) rotateY, 0, 1, 0);
		gl.glRotatef((float) rotateX, 1, 0, 0);
		gl.glRotatef((float) rotateZ, 0, 0, 1);
		gl.glScalef((float)scale, (float)scale, (float)scale);
		int textureId = TextureManager.loadGLTexture(gl, asset, texture);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		ModelData3D model3D = ObjImporter.importObj(asset, model);
		//TDModel model3D = OBJParser.loadObj(asset, model);
		model3D.draw(gl);
		gl.glPopMatrix();	
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public double getX() {
		return x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getY() {
		return y;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public double getZ() {
		return z;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getScale() {
		return scale;
	}
	public void setRotateX(double rotateX) {
		this.rotateX = rotateX;
	}
	public double getRotateX() {
		return rotateX;
	}
	public void setRotateY(double rotateY) {
		this.rotateY = rotateY;
	}
	public double getRotateY() {
		return rotateY;
	}
	public void setRotateZ(double rotateZ) {
		this.rotateZ = rotateZ;
	}
	public double getRotateZ() {
		return rotateZ;
	}
	public void setTexture(String texture) {
		this.texture = texture;
	}
	public String getTexture() {
		return texture;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModel() {
		return model;
	}
	public double distanceTo(EnvObject obj) {
		double xdiff = x-obj.x;
		double ydiff = y-obj.y;
		double zdiff = z-obj.z;
		double dist = Math.sqrt(xdiff*xdiff+ydiff*ydiff+zdiff*zdiff);
		return dist;
	}
	
}
