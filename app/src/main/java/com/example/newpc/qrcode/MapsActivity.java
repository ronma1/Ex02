package com.example.newpc.qrcode;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.StringTokenizer;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static StringBuffer qrLocation;
    private final static String myLocationFromQR = "Location from QR";
    private final static int    minBundlerSize   = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        qrLocation = new StringBuffer();
        Bundle bundle = getIntent().getExtras();
        qrLocation.append(bundle.getString(ReaderActivity.LocationHandler));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        if(qrLocation.length() > minBundlerSize){
            String loc_string = qrLocation.toString();
            StringTokenizer tokenizer = new StringTokenizer(loc_string, " ");
            LatLng location = new LatLng(Double.parseDouble(tokenizer.nextToken()),
                    Double.parseDouble(tokenizer.nextToken()));
            mMap.addMarker(new MarkerOptions().position(location).title(myLocationFromQR));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
        else {
            //TODO:  to implement via GPS
        }

    }

}