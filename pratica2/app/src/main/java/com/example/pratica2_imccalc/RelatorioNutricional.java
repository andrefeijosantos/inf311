package com.example.pratica2_imccalc;

import static java.lang.Math.round;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RelatorioNutricional extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("CICLO_VIDA", "Entrei na onCreate da RelatorioNutricional");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_nutricional);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent it = getIntent();
        initOutputs(it.getExtras());
        initButtons();
    }

    @Override
    protected void onStart() {
        Log.v("CICLO_VIDA", "Entrei na onStart da RelatorioNutricional");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.v("CICLO_VIDA", "Entrei na onResume da RelatorioNutricional");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.v("CICLO_VIDA", "Entrei na onRestart da RelatorioNutricional");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.v("CICLO_VIDA", "Entrei na onPause da RelatorioNutricional");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v("CICLO_VIDA", "Entrei na onStop da RelatorioNutricional");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v("CICLO_VIDA", "Entrei na onDestroy da RelatorioNutricional");
        super.onDestroy();
    }

    protected void initOutputs(Bundle savedInstanceState) {
        savedInstanceState.getString("nome");
        savedInstanceState.getInt("idade");
        savedInstanceState.getDouble("peso");
        savedInstanceState.getDouble("altura");

        final TextView nome = (TextView) findViewById(R.id.outputNome);
        nome.setText(savedInstanceState.getString("nome"));

        final TextView idade = (TextView) findViewById(R.id.outputIdade);
        idade.setText(String.valueOf(savedInstanceState.getInt("idade")) + " anos");

        final TextView peso = (TextView) findViewById(R.id.outputPeso);
        peso.setText(String.valueOf(savedInstanceState.getDouble("peso"))  + " Kg");

        final TextView altura = (TextView) findViewById(R.id.outputAltura);
        altura.setText(String.valueOf(savedInstanceState.getDouble("altura")) + " m");

        // Cálculo do IMC.
        final TextView imc = (TextView) findViewById(R.id.outputIMC);
        double imcValue = Math.round(100.0 * savedInstanceState.getDouble("peso") / (savedInstanceState.getDouble("altura")*savedInstanceState.getDouble("altura"))) / 100.0;
        imc.setText(imcValue + " Kg/m²");

        String imcClassificacao = "";
        if(imcValue < 18.5) imcClassificacao = "Abaixo do Peso";
        else if(imcValue <= 24.9) imcClassificacao = "Saudável";
        else if(imcValue <= 29.9) imcClassificacao = "Sobrepeso";
        else if(imcValue <= 34.9) imcClassificacao = "Obesidade Grau I";
        else if(imcValue <= 39.9) imcClassificacao = "Obesidade Grau II (severa)";
        else imcClassificacao = "Obesidade Grau III (mórbida)";

        // Classificação.
        final TextView classificacao = (TextView) findViewById(R.id.outputClassificacao);
        classificacao.setText(imcClassificacao);
    }

    protected void initButtons() {
        final Button bttn = (Button) findViewById(R.id.bttnVoltar);
        bttn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}