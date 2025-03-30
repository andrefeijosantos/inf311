package com.example.pratica1a_simplecalc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    EditText inputValor1;
    EditText inputValor2;
    TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initTexts();
        initInputs();
        initButtons();
    }

    protected void initTexts() {
        result = (TextView) findViewById(R.id.txtResultado);
    }

    protected void initInputs() {
        inputValor1 = (EditText) findViewById(R.id.inputValor1);
        inputValor2 = (EditText) findViewById(R.id.inputValor2);
    }

    protected void initButtons() {
        final Button sum = (Button) findViewById(R.id.bttnSoma);
        sum.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                double val1 = Double.parseDouble(inputValor1.getText().toString());
                double val2 = Double.parseDouble(inputValor2.getText().toString());
                result.setText("O Resultado é: " + (val1+val2));
            }
        });

        final Button sub = (Button) findViewById(R.id.bttnSubtrai);
        sub.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                double val1 = Double.parseDouble(inputValor1.getText().toString());
                double val2 = Double.parseDouble(inputValor2.getText().toString());
                result.setText("O Resultado é: " + (val1-val2));
            }
        });

        final Button mul = (Button) findViewById(R.id.bttnMul);
        mul.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                double val1 = Double.parseDouble(inputValor1.getText().toString());
                double val2 = Double.parseDouble(inputValor2.getText().toString());
                result.setText("O Resultado é: " + (val1*val2));
            }
        });

        final Button div = (Button) findViewById(R.id.bttnDiv);
        div.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                double val1 = Double.parseDouble(inputValor1.getText().toString());
                double val2 = Double.parseDouble(inputValor2.getText().toString());

                if(val2 == 0) {
                    result.setText("Operação Impossível: divisão por 0");
                    return;
                }

                result.setText("O Resultado é: " + (val1/val2));
            }
        });
    }
}