package com.example.pratica3_mylocations;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Relatorio extends ListActivity {

    List<String> logs = new ArrayList<>();
    List<Integer> ids = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = this.openOrCreateDatabase("LOCATION_APP", Context.MODE_PRIVATE, null);
        Cursor cursor = db.query("Logs", new String[]{"idLocation, msg, timestamp"}, null, null, null, null, null);

        if (cursor.getCount() > 0)
            while (cursor.moveToNext()) {
                int idLocation = cursor.getInt(cursor.getColumnIndexOrThrow("idLocation"));
                String msg = cursor.getString(cursor.getColumnIndexOrThrow("msg"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                ids.add(idLocation);
                logs.add(msg + " - " + timestamp);
            }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logs);
        setListAdapter(arrayAdapter);

        cursor.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String[] cols = {"Location.latitude", "Location.longitude"};
        String[] args = {String.valueOf(ids.get(position))};

        Cursor cursor = db.query("Logs INNER JOIN Location ON Logs.idLocation = Location.id",
                cols,
                "Logs.idLocation = ?",
                args,
                null, null, null);

        if (cursor.moveToFirst())
            Toast.makeText(this, cursor.getDouble(0) + ", " + cursor.getDouble(1),
                            Toast.LENGTH_LONG).show();

        cursor.close();
    }
}