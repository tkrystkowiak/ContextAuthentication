package com.tomaszkrystkowiak.contextauthentication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightMeter implements SensorEventListener {

    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mLight;
    private float lx = 0;

    LightMeter(Context mContext){
        this.mContext = mContext;
    }
    public void start() {
        mSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lx = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int getLightLevel(){
        if(lx <=10){
            return 0;
        }
        else{
            if(lx<=10000){
                return 1;
            }
            else{
                return 2;
            }
        }
    }

}
