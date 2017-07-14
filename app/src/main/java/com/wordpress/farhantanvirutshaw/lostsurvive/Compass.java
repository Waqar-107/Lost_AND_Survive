package com.wordpress.farhantanvirutshaw.lostsurvive;

/**
 * Created by utshaw on 7/6/17.
 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Compass extends AppCompatActivity implements  SensorEventListener
{
    ImageView compass;
    TextView heading;
    SensorManager mSensorManager;
    float currentDegree=0f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        compass= (ImageView) findViewById(R.id.compass);
        heading= (TextView) findViewById(R.id.text);

        mSensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }



    public void onSensorChanged(SensorEvent event)
    {
        float degree=Math.round(event.values[0]);
        heading.setText("Heading: "+Float.toString(degree)+" degrees");

        //animation
        RotateAnimation ra=new RotateAnimation(currentDegree,-degree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(200);
        ra.setFillAfter(true);

        compass.startAnimation(ra);
        currentDegree=-degree;
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}