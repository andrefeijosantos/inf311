package com.example.pratica4b;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private String CURR_CLASS_LUM;
    private String CURR_CLASS_PROX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent it = getIntent();

        float lumValue = it.getFloatExtra("lum", -1);
        float proxValue = it.getFloatExtra("prox", -1);

        if(lumValue == -1)  CURR_CLASS_LUM = "erro";
        else if(lumValue < 20) CURR_CLASS_LUM = "baixa";
        else CURR_CLASS_LUM = "alta";

        if(proxValue == -1)  CURR_CLASS_PROX = "erro";
        else if(proxValue > 3) CURR_CLASS_PROX = "distante";
        else CURR_CLASS_PROX = "perto";
    }

    public void devolver(View v) {
        Intent it = new Intent();

        it.putExtra("classLum", CURR_CLASS_LUM);
        it.putExtra("classProx", CURR_CLASS_PROX);

        setResult(2, it);

        finish();
    }
}