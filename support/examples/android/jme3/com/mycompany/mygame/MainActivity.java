package com.mycompany.mygame;

import android.os.Bundle;
import com.google.ads.*;
import com.jme3.app.AndroidHarness;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jme3.system.android.AndroidConfigChooser.ConfigType;
import env3d.android.EnvAndroid;

public class MainActivity extends AndroidHarness implements SensorEventListener {

    private SensorManager sensorManager;
    /**
     * Used for the advertising panel
     */
    private LinearLayout adLayout;

    /**
     * Note that you can ignore the errors displayed in this file, the android
     * project will build regardless. Install the 'Android' plugin under
     * Tools->Plugins->Available Plugins to get error checks and code completion
     * for the Android project files.
     */
    public MainActivity() {
        // Set the application class to run
        appClass = "com.mycompany.mygame.EnvHarness";
        // Try ConfigType.FASTEST; or ConfigType.LEGACY if you have problems
        eglConfigType = ConfigType.BEST;
        // Exit Dialog title & message
        exitDialogTitle = "Exit?";
        exitDialogMessage = "Press Yes";
        // Choose screen orientation
        screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        // Invert the MouseEvents X (default = true)
        mouseEventsInvertX = true;
        // Invert the MouseEvents Y (default = true)
        mouseEventsInvertY = true;
        //eglConfigType = ConfigType.BEST_TRANSLUCENT;
        splashPicID = R.drawable.splash;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double ax = event.values[0];
            double ay = event.values[1];
            double az = event.values[2];

            if (app != null) {
                if (getDeviceDefaultOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                    ((EnvAndroid) app).setTilt(-1*ax, ay, az);
                } else {
                    ((EnvAndroid) app).setTilt(ay, ax, az);
                }
            }

        }
    }

    protected void onResume() {

        super.onResume();

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    protected void onPause() {

        super.onPause();

        sensorManager.unregisterListener(this);
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Create the ad layout panel for display admob advertising
     *
     * @return the layout
     */
    public LinearLayout createAdLayout() {
        final LinearLayout layout = new LinearLayout(this);

        layout.setBackgroundColor(Color.BLACK);

        EnvAndroid env = (EnvAndroid) app;

        final AdView ad = new AdView(this, AdSize.BANNER, env.getAdUnitId());
        AdRequest adRequest = new AdRequest();

        // My test device id
        adRequest.addTestDevice("D6151C232508AA4B7B9C4F6E99D7EA3C");
        ad.loadAd(adRequest);

        final TextView closeText = new TextView(this);
        closeText.setText(" X ");
        closeText.setTextSize(30);
        closeText.setClickable(true);

        final MainActivity _this = this;
        closeText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        frameLayout.removeView(adLayout);
                    }
                });
            }
        });

        ad.setAdListener(new AdListener() {
            public void onReceiveAd(Ad _ad) {
                layout.addView(ad);
                layout.addView(closeText);
            }

            public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode error) {
            }

            public void onPresentScreen(Ad ad) {
            }

            public void onDismissScreen(Ad ad) {
            }

            public void onLeaveApplication(Ad ad) {
            }
        });

        return layout;
    }

    @Override
    public void layoutDisplay() {
        super.layoutDisplay(); //To change body of generated methods, choose Tools | Templates.
        if (adLayout == null) {
            adLayout = createAdLayout();
        }

    }

    /**
     * Check if we want to show advertising
     */
    @Override
    public void update() {
        // Every frame, check if we need to show ads
        if (app != null) {
            EnvAndroid env = (EnvAndroid) app;
            if (env.isShowAd()) {
                // Only show ad if not already shown
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        // display the adLayout
                        if (adLayout.getParent() == null) {
                            frameLayout.addView(adLayout, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT,
                                    Gravity.CENTER));
                        }
                    }
                });
            }
            env.setShowAd(false);
        }

        super.update();
    }

    public int getDeviceDefaultOrientation() {

        WindowManager lWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Configuration cfg = getResources().getConfiguration();
        int lRotation = lWindowManager.getDefaultDisplay().getRotation();

        if ((((lRotation == Surface.ROTATION_0) || (lRotation == Surface.ROTATION_180))
                && (cfg.orientation == Configuration.ORIENTATION_LANDSCAPE))
                || (((lRotation == Surface.ROTATION_90) || (lRotation == Surface.ROTATION_270))
                && (cfg.orientation == Configuration.ORIENTATION_PORTRAIT))) {

            return Configuration.ORIENTATION_LANDSCAPE;
        }

        return Configuration.ORIENTATION_PORTRAIT;
    }
}
