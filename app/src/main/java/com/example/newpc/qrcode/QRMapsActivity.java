
package com.example.newpc.qrcode;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.StringTokenizer;


public class QRMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap           mMap;
    private static StringBuffer qrLocation;
    private final static String myLocationFromQR     = "Location from QR";
    private final static int    minBundlerSize       = 1;
    private final static double Ariel_University_lat = 32.104082;
    private final static double Ariel_University_lng = 35.207815;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrmaps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        qrLocation = new StringBuffer(); // Extra data from qr activity
        Bundle bundle = getIntent().getExtras();
        qrLocation.append(bundle.getString(ReaderActivity.LocationHandler));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            StringTokenizer tokenizer = new StringTokenizer(qrLocation.toString(), " ");
            lat = Double.parseDouble(tokenizer.nextToken());
            lng = Double.parseDouble(tokenizer.nextToken());
        }
        catch (Exception e){
            Log.w("onMapReady", " Failed parse location from qr - Set location to Ariel");
            // If parseDouble didn't succeed set default location to Ariel
            lat = Ariel_University_lat;
            lng = Ariel_University_lng;
        }

        LatLng location = new LatLng(lat, lng);

        mMap.addMarker(new MarkerOptions().position(location).title(myLocationFromQR));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

    }

}