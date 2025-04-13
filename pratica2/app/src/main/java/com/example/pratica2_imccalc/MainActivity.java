package com.example.pratica2_imccalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("CICLO_VIDA", "Entrei na onCreate da Main");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initButtons();
    }

    @Override
    protected void onStart() {
        Log.v("CICLO_VIDA", "Entrei na onStart da Main");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.v("CICLO_VIDA", "Entrei na onResume da Main");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.v("CICLO_VIDA", "Entrei na onRestart da Main");

        super.onRestart();

        ((EditText)findViewById(R.id.inputNome)).setText("");
        ((EditText)findViewById(R.id.inputIdade)).setText("");
        ((EditText)findViewById(R.id.inputPeso)).setText("");
        ((EditText)findViewById(R.id.inputAltura)).setText("");
    }

    @Override
    protected void onPause() {
        Log.v("CICLO_VIDA", "Entrei na onPause da Main");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v("CICLO_VIDA", "Entrei na onStop da Main");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v("CICLO_VIDA", "Entrei na onDestroy da Main");
        super.onDestroy();
    }

    protected void initButtons() {
        final Button bttn = (Button) findViewById(R.id.bttnRelatorioNutricional);
        bttn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(getBaseContext(), RelatorioNutricional.class);
                Bundle params = new Bundle();

                // Salvando informações do usuário.
                String nome = ((EditText)findViewById(R.id.inputNome)).getText().toString();
                String idade =((EditText)findViewById(R.id.inputIdade)).getText().toString();
                String peso = ((EditText)findViewById(R.id.inputPeso)).getText().toString();
                String altura = ((EditText)findViewById(R.id.inputAltura)).getText().toString();

                if(nome.isEmpty() || idade.isEmpty() || peso.isEmpty() || altura.isEmpty())
                    return;

                params.putString("nome", nome);
                params.putInt("idade", Integer.parseInt(idade));
                params.putDouble("peso", Double.parseDouble(peso));
                params.putDouble("altura", Double.parseDouble(altura));

                it.putExtras(params);
                startActivity(it);
            }
        });
    }
}