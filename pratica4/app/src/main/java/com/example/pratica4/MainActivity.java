package com.example.pratica4;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lumSensor, proxSensor;
    private String CURR_PRECISION;
    private float CURR_LUM_VALUE = 0,
                  CURR_PROX_VALUE = 0;

    private Switch switchLum, switchVibr;

    private MotorHelper motor;
    private LanternaHelper lanterna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchLum  = (Switch) findViewById(R.id.switch1);
        switchVibr = (Switch) findViewById(R.id.switch2);

        motor = new MotorHelper(this);
        lanterna = new LanternaHelper(this);

        // Obtem o gerenciador de sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // === SENSOR DE LUMINOSIDADE ===
        lumSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lumSensor == null){
            Toast.makeText(this, "Sensor de luminosidade inexistente",
                    Toast.LENGTH_LONG).show();
        }

        // === SENSOR DE PROXIMIDADE ===
        proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proxSensor == null){
            Toast.makeText(this, "Sensor de proximidade inexistente",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lumSensor != null)
            sensorManager.registerListener(this, lumSensor, SensorManager.SENSOR_DELAY_GAME);
        if(proxSensor != null)
            sensorManager.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                CURR_LUM_VALUE = event.values[0];
                break;
            case Sensor.TYPE_PROXIMITY:
                CURR_PROX_VALUE = event.values[0];
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                CURR_PRECISION = "Baixa";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                CURR_PRECISION = "Média";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                CURR_PRECISION = "Alta";
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                CURR_PRECISION = "Sinal indisponível – não confiável";
                break;
            default:
        }
        Log.i("MUDANCA_PRECISAO", "SENSOR: " + sensor.getName() +
                " | PRECISÃO: " + CURR_PRECISION);
    }

    public void classificar(View v) {
        Intent it = new Intent("ACTION_APP_CLASSIFICACAO");
        it.putExtra("lum", CURR_LUM_VALUE);
        it.putExtra("prox", CURR_PROX_VALUE);
        startActivityForResult(it, 10);
    }

    @Override
    protected void onActivityResult(int codReq, int codRes, Intent it) {
        if(it == null)
            Toast.makeText(this, "Classificação cancelada", Toast.LENGTH_SHORT).show();
        else {
            String classLum = it.getStringExtra("classLum");
            switch(classLum) {
                case "alta":
                    switchLum.setChecked(false);
                    lanterna.desligar();
                    break;
                case "baixa":
                    switchLum.setChecked(true);
                    lanterna.ligar();
                    break;
                default:
                    Toast.makeText(this, "Retorno desconhecido", Toast.LENGTH_SHORT).show();
                    break;
            }

            String classProx = it.getStringExtra("classProx");
            switch(classProx) {
                case "distante":
                    switchVibr.setChecked(false);
                    motor.pararVibracao();
                    break;
                case "perto":
                    switchVibr.setChecked(true);
                    motor.iniciarVibracao();
                    break;
                default:
                    Toast.makeText(this, "Retorno desconhecido", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        lanterna.desligar();
        motor.pararVibracao();
    }
}