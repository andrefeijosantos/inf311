package com.example.pratica3_mylocations;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.time.Instant;

public class MainActivity extends ListActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDB();

        String menu[] = new String[] {
                            "Minha casa em Cordeiro",
                            "Minha casa em Viçosa",
                            "Meu departamento na UFV",
                            "Relatorio",
                            "Fechar aplicação"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                                                    android.R.layout.simple_list_item_1,
                                                    menu);
        setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent it = new Intent(this, LocationActivity.class);
        Bundle bundle = new Bundle();

        ContentValues row = new ContentValues();

        // Android Studio pede que a versão do SDK seja verificada.
        Instant timestamp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timestamp = Instant.now();
            row.put("timestamp", "" + timestamp);
        } else {
            row.put("timestamp", "");
            Toast.makeText(this, "Não foi possível registrar o horário de acesso.", Toast.LENGTH_SHORT).show();
        }

        switch (position) {
            case 0:
                bundle.putString("Loc", "Cordeiro");
                bundle.putString("Caption", "Minha casa em Cordeiro");
                row.put("idLocation", 3);
                row.put("msg", "visita a Cordeiro");
                db.insert("Logs", null, row);
                it.putExtras(bundle);
                startActivity(it);
                break;
            case 1:
                bundle.putString("Loc", "Vicosa");
                bundle.putString("Caption", "Meu apt em Vicosa");
                row.put("idLocation", 2);
                row.put("msg", "visita a Vicosa");
                db.insert("Logs", null, row);
                it.putExtras(bundle);
                startActivity(it);
                break;
            case 2:
                bundle.putString("Loc", "DPI");
                bundle.putString("Caption", "Meu departamento na UFV");
                row.put("idLocation", 1);
                row.put("msg", "visita ao DPI");
                db.insert("Logs", null, row);
                it.putExtras(bundle);
                startActivity(it);
                break;
            case 3:
                it = new Intent(getBaseContext(), Relatorio.class);
                startActivity(it);
                break;
            case 4:
                finish();
                break;
        }
    }

    private void initDB() {
        db = this.openOrCreateDatabase("LOCATION_APP", Context.MODE_PRIVATE, null);

        // Cria as tabelas do Banco de Dados.
        db.execSQL("CREATE TABLE IF NOT EXISTS Location (id INTEGER PRIMARY KEY AUTOINCREMENT, descricao TEXT, latitude REAL, longitude REAL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Logs (id INTEGER PRIMARY KEY AUTOINCREMENT, idLocation INTEGER, msg TEXT, timestamp TEXT, FOREIGN KEY(idLocation) REFERENCES Location(id));");

        // Verifica se a tabela já foi criada.
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Location;", null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();

            if (count == 0) {
                ContentValues values = new ContentValues();

                values.put("descricao", "DPI");
                values.put("latitude", -20.765022);
                values.put("longitude", -42.868671);
                db.insert("Location", null, values);
                values.clear();

                values.put("descricao", "Vicosa");
                values.put("latitude", -20.755691);
                values.put("longitude", -42.880575);
                db.insert("Location", null, values);
                values.clear();

                values.put("descricao", "Cordeiro");
                values.put("latitude", -22.026346);
                values.put("longitude", -42.357992);
                db.insert("Location", null, values);
                values.clear();
            }
        }
    }
}