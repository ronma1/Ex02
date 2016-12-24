package com.example.newpc.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.zxing.Result;

import java.util.StringTokenizer;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReaderActivity extends Activity implements ZXingScannerView.ResultHandler {


    private final static int    CAMERA_PERMISSION_CODE  = 1;
    private final static String dbLastQR                = "LastQR";
    private ZXingScannerView    mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        // Ask for permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();

    }

    /**
     * handelResult
     * Handles the result from scanning QR activity.
     * Changes the activity to MapsActivity {@link MapsActivity}
     * @param result - string represent the result
     */
    @Override
    public void handleResult(Result result) {
        Log.w("handleResult", "========== location saved ==========");
        double lat,lng;
        try {
            StringTokenizer tokenizer = new StringTokenizer(result.toString(), " ");
            lat = Double.parseDouble(tokenizer.nextToken());
            lng = Double.parseDouble(tokenizer.nextToken());
        }
        catch (Exception e){
            Log.w("onMapReady", " Failed parse location from qr - Set location to Ariel");
            // If parseDouble didn't succeed set default location to Ariel University
            lat = GenericMapsActivity.Ariel_University_lat;
            lng = GenericMapsActivity.Ariel_University_lng;
        }

        String send_via_bundle = Double.toString(lat) + " " + Double.toString(lng);
        Intent gIntent = new Intent(ReaderActivity.this, GenericMapsActivity.class);
        gIntent.putExtra(GenericMapsActivity.LocationHandler, send_via_bundle);
        gIntent.putExtra(GenericMapsActivity.fromActivity, dbLastQR);
        startActivity(gIntent);
    }
}
