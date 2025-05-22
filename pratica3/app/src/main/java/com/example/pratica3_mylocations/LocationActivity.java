package com.example.pratica3_mylocations;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Localizações
    private Map<LatLng, String> points;
    private LatLng CORDEIRO;
    private LatLng VICOSA;
    private LatLng DPI;
    private LatLng CURR = null;
    private Marker CURR_MARKER = null;
    String curr;


    // Legendas
    private final String TITLE_CORDEIRO = "Minha casa em Cordeiro";
    private final String TITLE_VICOSA = "Meu apt em Vicosa";
    private final String TITLE_DPI = "Meu departamento na UFV";

    // Mapa
    private GoogleMap map;

    // Banco de Dados
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Intent it = getIntent(); Bundle bundle = it.getExtras();
        curr = bundle.getString("Loc");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Inicializa banco de dados.
        points =  new HashMap<>();
        db = openOrCreateDatabase("LOCATION_APP", Context.MODE_PRIVATE, null);
        Cursor cursor = db.query("Location", new String[]{"descricao, latitude, longitude"}, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));

                if(descricao.equals("Cordeiro"))
                    CORDEIRO = new LatLng(latitude, longitude);
                else if(descricao.equals("Vicosa"))
                    VICOSA = new LatLng(latitude, longitude);
                else if(descricao.equals("DPI"))
                    DPI = new LatLng(latitude, longitude);
            }
        }
        cursor.close();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.addMarker(new MarkerOptions().position(CORDEIRO).title(TITLE_CORDEIRO));
        map.addMarker(new MarkerOptions().position(VICOSA).title(TITLE_VICOSA));
        map.addMarker(new MarkerOptions().position(DPI).title(TITLE_DPI));

        switch(curr) {
            case "Cordeiro":
                onClickCordeiro(null);
                break;
            case "Vicosa":
                onClickVicosa(null);
                break;
            case "DPI":
                onClickDPI(null);
                break;
        }
    }

    public void onClickCordeiro(View v) {
        if(CURR != null)
            CURR_MARKER.remove();

        curr = "Cordeiro";

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CORDEIRO, 16);
        map.animateCamera(update);

        Toast.makeText(this, "Minha casa em Cordeiro", Toast.LENGTH_SHORT).show();
    }

    public void onClickVicosa(View v) {
        if(CURR != null)
            CURR_MARKER.remove();

        curr = "Vicosa";

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(VICOSA, 16);
        map.animateCamera(update);

        Toast.makeText(this, "Meu apto em Vicosa", Toast.LENGTH_SHORT).show();
    }

    public void onClickDPI(View v) {
        if(CURR != null)
            CURR_MARKER.remove();

        curr = "DPI";

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(DPI, 16);
        map.animateCamera(update);

        Toast.makeText(this, "Meu departamento na UFV", Toast.LENGTH_SHORT).show();
    }

    public void onClickLoc(View v) {
        requestLocationPermissionAndGetCurrent();
    }

    public void requestLocationPermissionAndGetCurrent() {
        // Verifica a necessidade de pedir a permissão
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Verifica se precisa explicar para o usuário a necessidade da permissão (true se já negou antes).
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Permita o uso da localização para ler utilizar essa funcionalidade.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            }

            // Se não negou antes.
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);

        }

        // Caso o usuário já tenha dado a permissão.
        else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            // Testa se o aparelho tem GPS
            PackageManager packageManager = getPackageManager();
            boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

            // Estabelece critério de precisão.
            if (hasGPS) criteria.setAccuracy(Criteria.ACCURACY_FINE);
            else criteria.setAccuracy(Criteria.ACCURACY_COARSE);

            // Obtém o melhor provedor habilitado com o critério acima.
            String provider = lm.getBestProvider(criteria, true);

            // Caso não seja possível obter o provedor.
            if (provider == null) {
                Toast.makeText(this, "Nenhum provedor encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtém a localização do usuário.
            Location currLocation = lm.getLastKnownLocation(provider);

            if (currLocation == null) {
                Toast.makeText(this, "Localização não disponível", Toast.LENGTH_SHORT).show();
                return;
            }

            if(CURR != null)
                CURR_MARKER.remove();

            LatLng currCoords = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());

            CURR_MARKER = map.addMarker(new MarkerOptions().position(CURR).title("Minha localização atual")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currCoords, 12));
        }
    }
}