package com.example.newpc.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReaderActivity extends Activity implements ZXingScannerView.ResultHandler {

    final static String       LocationHandler         = "LocationHandler";
    private final static int  CAMERA_PERMISSION_CODE  = 1;
    private ZXingScannerView  mScannerView;
    private DatabaseReference dbqr      = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference dbdata    = dbqr.child("location");

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
        dbdata.setValue(result.getText());

        Intent gIntent = new Intent(ReaderActivity.this, MapsActivity.class);
        gIntent.putExtra(LocationHandler, result.toString());
        startActivity(gIntent);
    }
}
