package env3d.android;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class Env3DActivity extends Activity {
	
	private Env envR;
	private GestureDetector gestureDetecter;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create our Preview view and set it as the content of our
        // Activity
//        mGLSurfaceView = new GLSurfaceView(this);
        envR = new Env(this);
        gestureDetecter = new GestureDetector(envR);
        //envR.asset = this.getResources().getAssets();
        //mGLSurfaceView.setRenderer(envR);
        
        setContentView(envR);
    }

    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetecter.onTouchEvent(event);
	}

	@Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        envR.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        envR.onPause();
    }

    
    
    private GLSurfaceView mGLSurfaceView;
}