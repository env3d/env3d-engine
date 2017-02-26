package env3d.android;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//import env3d.android.mygame.Game;
import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public class Env extends GLSurfaceView implements Renderer, OnGestureListener {

	public AssetManager asset;
	private ArrayList<EnvObject> objList;
	private int keyUp, keyDown;
	
	private Object game;
	private Method playMethod;
	
	private Vector3f cam, eye, up, side, camUp;
	private boolean touch;
	private float touchX, touchY;
	
	public Env(Context context) {
		super(context);
		//Set this as Renderer
		this.setRenderer(this);
		//Request focus, otherwise buttons won't react
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		asset = context.getAssets();		
		
		objList = new ArrayList<EnvObject>();
		
		// Create the game object for user application
		
		cam = new Vector3f(0,0,0);
		eye = new Vector3f(0,0,-1);
		up = new Vector3f(0,1,0);
		camUp = new Vector3f(0,1,0);
		side = new Vector3f(1,0,0);
		
		setRoom(new EnvRoom());		
		//game = new Game(this);
		
		try {
			Class gameClass = Class.forName("Game");				
			Constructor gameConstructor = gameClass.getDeclaredConstructor(this.getClass());
			game = gameConstructor.newInstance(this);
			playMethod = gameClass.getDeclaredMethod("play");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get the last key pressed
	 * @return the last key pressed
	 */
	public int getKey() {
		return keyUp;
	}

	public int getKeyDown() {
		return keyDown;
	}
	
	public boolean getTouch() {
		return touch;
	}
	
	public float getTouchX() {
		return touchX;
	}
	
	public float getTouchY() {
		return touchY;
	}
	
	public void addObject(EnvObject obj) {
		objList.add(obj);
	}
	
	public void removeObject(EnvObject obj) {
		objList.remove(obj);
	}
	
	/**
	 * Clears the room and reset the walls
	 * @param room
	 */
	public void setRoom(EnvRoom room) {
		objList.clear();
		for (EnvObject o : room.getObjects()) {
			objList.add(o);
		}
		
	}
	
	public void setCameraXYZ(double x, double y, double z) {
		cam.x = (float) x; cam.y = (float) y; cam.z = (float) z;
	}
	
	public void setCameraPitch(double theta) {
		//rotate eye and up along the x axis
		
		rotate(eye, (float)theta, side);
		//rotate(up, (float)theta, side);
	}

	public void setCameraYaw(double theta) {
		//rotate eye and up along the up axis
		
		rotate(eye, (float)theta, up);
		rotate(side, (float)theta, up);
	}
	
	public float getCameraX() {
		return cam.x;
	}
	
	public float getCameraY() {
		return cam.y;
	}
	
	public float getCameraZ() {
		return cam.z;
	}
	
	/**
	 * Called once per frame to update the screen
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		
		//game.play();
		try {
			playMethod.invoke(game);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		keyUp = KeyEvent.KEYCODE_UNKNOWN;
		
		/*
		 * Usually, the first thing one might want to do is to clear the screen.
		 * The most efficient way of doing this is to use glClear().
		 */
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		/*
		 * Now we're ready to draw some 3D objects
		 */
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		//gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glLoadIdentity();
		//FadiGluLookAt(gl, cam.x, cam.y, cam.z, eye.x, eye.y, eye.z, up.x, up.y, up.z);		
		GLU.gluLookAt(gl, cam.x, cam.y, cam.z, cam.x+eye.x, cam.y+eye.y, cam.z+eye.z, up.x, up.y, up.z);
		// Iterate over the objList and render each one
		try {
			for (EnvObject envObj : objList) {
				envObj.draw(asset, gl);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		TextureManager.clear();
		gl.glViewport(0, 0, width, height);

		/*
		 * Set our projection matrix. This doesn't have to be done each time we
		 * draw, but usually a new projection needs to be set when the viewport
		 * is resized.
		 */

		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 500);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {


		/*
		 * By default, OpenGL enables features that improve quality but reduce
		 * performance. One might want to tweak that especially on software
		 * renderer.
		 */
		gl.glDisable(GL10.GL_DITHER);

		/*
		 * Some one-time OpenGL initialization can be made here probably based
		 * on features of this particular context
		 */
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		// Remember the key
		keyUp = keyCode;
	
		keyDown = KeyEvent.KEYCODE_UNKNOWN;
		//We handled the event
		return true;
	}	
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		keyDown = keyCode;
		
		return true;
		// TODO Auto-generated method stub
		//return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			touch = true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			touch = false;
		}
		
		touchX = event.getRawX();
		touchY = event.getRawY();
		//System.out.println("Touch "+event.getRawX()+" "+event.getRawY());
		
		// returning false will allow the event to filter up.
		return false;
		// TODO Auto-generated method stub
		//return super.onTouchEvent(event);
	}

	/* rotate camera along another vector */
    protected void rotate(Vector3f p, float theta, Vector3f r) {
        // Rotate vector p about the vector r
        // POST: vector p is mutated to it's new location
        
        double costheta,sintheta;
        float x=0,y=0,z=0;
        
        costheta = Math.cos(theta*0.0174532925);
        sintheta = Math.sin(theta*0.0174532925);
        
        x += (costheta + (1 - costheta) * r.x * r.x) * p.x;
        x += ((1 - costheta) * r.x * r.y - r.z * sintheta) * p.y;
        x += ((1 - costheta) * r.x * r.z + r.y * sintheta) * p.z;
        
        y += ((1 - costheta) * r.x * r.y + r.z * sintheta) * p.x;
        y += (costheta + (1 - costheta) * r.y * r.y) * p.y;
        y += ((1 - costheta) * r.y * r.z - r.x * sintheta) * p.z;
        
        z += ((1 - costheta) * r.x * r.z - r.y * sintheta) * p.x;
        z += ((1 - costheta) * r.y * r.z + r.x * sintheta) * p.y;
        z += (costheta + (1 - costheta) * r.z * r.z) * p.z;
        
        p.x = x;
        p.y = y;
        p.z = z;
    }

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Down");
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		setCameraPitch(distanceY);
		setCameraYaw(distanceX);

		System.out.println("Scrolling");
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("untoch");
		touch = false;
		return true;
	}

}
