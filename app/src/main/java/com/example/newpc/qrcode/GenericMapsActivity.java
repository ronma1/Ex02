
package com.example.newpc.qrcode;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.StringTokenizer;

import static com.example.newpc.qrcode.R.id.map;


public class GenericMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final static String         LocationHandler      = "LocationHandler";
    final static String         fromActivity         = "FromActivity";
    final static double         Ariel_University_lat = 32.104082;
    final static double         Ariel_University_lng = 35.207815;

    private final static String myLocationFromQR     = "Location from QR";
    private final static int    defaultMapZoom       = 15;
    private GoogleMap           mMap;
    private static LatLng       locationOutSource;
    private SupportMapFragment  mapFragment;
    private Bundle              bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrmaps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        bundle = getIntent().getExtras();
        locationOutSource = getLocationFromString(bundle.getString(LocationHandler));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Insert the location to DB under users/uid/someLastLocation
        DatabaseReference locDB = ProfileActivity.myUser.child(bundle.getString(fromActivity));
        locDB.setValue(Double.toString(locationOutSource.latitude) + " " +
                Double.toString(locationOutSource.longitude));

        mMap.addMarker(new MarkerOptions().position(locationOutSource).title(myLocationFromQR));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationOutSource));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(defaultMapZoom));

    }

    /**
     * Parse string into LatLng object
     * @param loc String representation of location object
     * @return LatLng object
     */
    public LatLng getLocationFromString(String loc){
        StringTokenizer tokenizer = new StringTokenizer(loc, " ");
        double lat = Double.parseDouble(tokenizer.nextToken());
        double lng = Double.parseDouble(tokenizer.nextToken());
        return new LatLng(lat, lng);
    }


}