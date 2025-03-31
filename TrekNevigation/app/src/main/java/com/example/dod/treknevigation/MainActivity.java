package com.example.dod.treknevigation;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends Activity implements SensorEventListener {

    TextView t;
    private ImageView img;
    private float curdeg = 0f;
    private SensorManager sensorManager;
    private Sensor magneticFieldSensor;
    private Sensor accelerometerSensor;
    private float[] gravity = null;
    private float[] geomagnetic = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = findViewById(R.id.tvd);
        img = findViewById(R.id.imgv);
        // Get the SensorManager system service
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get the magnetic field and accelerometer sensors
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
          AdView mAdView = findViewById(R.id.adView);
          AdRequest.Builder adRequest = new AdRequest.Builder();
          mAdView.loadAd(adRequest.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for sensor updates
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listeners
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }

        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];

            // Calculate rotation matrix
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
            if (success) {
                // Get orientation (azimuth, pitch, roll)
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);

                // Azimuth is the first value, it gives us the direction the device is pointing
                float azimuth = orientation[0];

                // Convert azimuth to degrees
                float degree = (float) Math.toDegrees(azimuth);
                degree = (degree + 360) % 360;

                // Rotate the compass image
                RotateAnimation rotateAnimation = new RotateAnimation(
                        curdeg, -degree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                rotateAnimation.setDuration(210);
                rotateAnimation.setFillAfter(true);
                img.startAnimation(rotateAnimation);

                // Update the current degree
                curdeg = Math.round(-degree);
                t.setText(String.valueOf(-curdeg));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
