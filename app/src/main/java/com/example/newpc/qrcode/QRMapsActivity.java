
package com.example.newpc.qrcode;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.util.Data;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.StringTokenizer;

import static com.example.newpc.qrcode.R.id.map;


public class QRMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final static String dbLastQR             = "LastQR";
    private final static String myLocationFromQR     = "Location from QR";
    private final static double Ariel_University_lat = 32.104082;
    private final static double Ariel_University_lng = 35.207815;
    private final static int    defaultMapZoom       = 15;
    private GoogleMap           mMap;
    private static StringBuffer qrLocation;
    private SupportMapFragment  mapFragment;
    private Bundle              bundle;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrmaps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        qrLocation = new StringBuffer(); // Extra data from qr activity
        bundle = getIntent().getExtras();
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
            // If parseDouble didn't succeed set default location to Ariel University
            lat = Ariel_University_lat;
            lng = Ariel_University_lng;
        }

        LatLng location = new LatLng(lat, lng);

        // Insert the location to DB under users/uid/lastQR
        DatabaseReference lastQR_ref = ProfileActivity.myUser.child(dbLastQR);
        lastQR_ref.setValue(Double.toString(lat) + " " + Double.toString(lng));

        mMap.addMarker(new MarkerOptions().position(location).title(myLocationFromQR));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(defaultMapZoom));

    }

}