package com.example.opengllayerscode;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MotionSensorHelper implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private float[] gravity;
    private float[] motion;
//    private int inclination ;
//    private float acceleration = 0f;
//    private float currentAcceleration = 0f;
//    private float lastAcceleration = 0f;

    public MotionSensorHelper(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        boolean isGravityAvailable = isSensorAvailable(context, Sensor.TYPE_GRAVITY);
        if(isGravityAvailable){
            sensor= sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }
        else{
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        gravity = new float[3];
        motion = new float[3];
//        acceleration = 10f;
//        currentAcceleration = SensorManager.GRAVITY_EARTH;
//        lastAcceleration = SensorManager.GRAVITY_EARTH;
    }

    public void startListening() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST );
    }

    public void stopListening() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.8f;

        float[] values = event.values.clone();

//        currentAcceleration = (float) Math.sqrt(( values[0] *  values[0] + values[1] * values[1] + values[2] * values[2]));
//        float delta = currentAcceleration - lastAcceleration;
//        acceleration = acceleration * 0.9f + delta;
//        double norm_Of_g = Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]);
//        values[0] = (float) (values[0] / norm_Of_g);
//        values[1] = (float) (values[1] / norm_Of_g);
//        values[2] = (float) (values[2] / norm_Of_g);
//        inclination = (int) Math.round(Math.toDegrees(Math.acos(values[2])));


        // Remove cross-axis sensitivity
        gravity[0] = alpha * gravity[0] + (1 - alpha) * values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * values[2];

        // Accumulate values
        motion[0] += (values[0] - gravity[0]);
        motion[1] += (values[1] - gravity[1]);
        motion[2] += (values[2] - gravity[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    public float getY() {
        float yVal = motion[0];
//        Log.d("fdsfsdfsadfsadf", "acceleration "+acceleration);
//
//        if (inclination < 70 || inclination > 155 )
//        {
//            if (acceleration > 2) {
//                yVal = 0.0f;
//            }
//        }
//        else
//        {
//            yVal=motion[0];
//        }
        return yVal;
    }

    public float getX() {
        return motion[1];
    }

    public float getZ() {
        return motion[2];
    }

    private boolean isSensorAvailable(Context context, int sensorType) {
        // Get the SensorManager instance
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Check if the sensor is available
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        return sensor != null;
    }
}
