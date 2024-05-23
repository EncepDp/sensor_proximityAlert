package com.example.sensor_proximityalert;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private TextView proximityStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        proximityStatusView = findViewById(R.id.proximityStatusView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            if (proximitySensor != null) {
                sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "Proximity sensor not available", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];

            if (distance < proximitySensor.getMaximumRange()) {
                proximityStatusView.setText("Object is close: " + distance + " cm");
                triggerProximityAction();
            } else {
                proximityStatusView.setText("Object is far: " + distance + " cm");
            }
        }
    }

    private void triggerProximityAction() {
        Toast.makeText(this, "Proximity action triggered!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Tidak digunakan dalam aplikasi ini
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
