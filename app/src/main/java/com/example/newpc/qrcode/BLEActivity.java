package com.example.newpc.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.HashMap;

public class BLEActivity extends Activity implements BeaconConsumer {
    private final static String      dbLastBLE = "LastBLE";
    protected static final String    TAG = "BLEActivity";
    protected static final String    noBeaconMsg = "No beacons avalibale yet";
    private static final int         distRow = 2;
    private BeaconManager            beaconManager;
    private TableLayout              table;
    private Button                   ble_loc_btn;
    private Beacon                   b;
    private HashMap<String, Integer> tableContent;
    private HashMap<String, Beacon>  beaconsMap;
    private int beaconCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        tableContent = new HashMap<>();
        beaconsMap   = new HashMap<>();
        table        = (TableLayout) findViewById(R.id.table_lay);
        ble_loc_btn  = (Button) findViewById(R.id.bleLocBTN);

        // sets ble_loc_btn on click action
        BLEListnerSetter();

        updateTable("Address", "Dist (Meter)");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    b = beacons.iterator().next();
                    Log.i(TAG, "The first beacon I see is about "+b.getDistance()+" meters away.");
                    Log.i(TAG, b.getBluetoothAddress());
                    beaconsMap.put(b.getBluetoothAddress(), b);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTable(b.getBluetoothAddress(), Double.toString(b.getDistance()));
                        }
                    });
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    /**
     * Updates table UI single row
     * @param address - to be attached to the first col
     * @param dist - to be attached to the second col
     */
    public void updateTable(String address, String dist){

        if(tableContent.containsKey(address)){
            int index = tableContent.get(address);
            TableRow row = (TableRow) table.getChildAt(index);
            TextView c3  = (TextView) row.getChildAt(distRow);
            c3.setText(dist);
        }
        else{
            TableRow row = new TableRow(table.getContext());
            TextView c1 = new TextView(row.getContext());
            TextView c3 = new TextView(row.getContext());
            TextView c2 = new TextView(row.getContext());
            c1.setText(address);
            c2.setText("    "); // Separator between cols
            c3.setText(dist);
            row.addView(c1);
            row.addView(c2);
            row.addView(c3);
            table.addView(row);
            if (!address.equals("Address")){
                beaconCount++;
                tableContent.put(address, beaconCount);
            }

        }
    }

    /**
     * Sets ble_loc_btn onClick method
     */
    public void BLEListnerSetter(){
        ble_loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Beacon nearest = null;
                double dist = Double.MAX_VALUE;
                for(String address : beaconsMap.keySet()){
                    if(beaconsMap.get(address).getDistance() < dist){
                        nearest = beaconsMap.get(address);
                        dist = nearest.getDistance();
                    }
                }

                if(nearest == null){
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), noBeaconMsg, duration);
                    toast.show();
                }
                else{

                    DatabaseReference bleAdresses = FirebaseDatabase.getInstance().getReference().child("BLE");
                    bleAdresses.child(nearest.getBluetoothAddress())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String location = (String) dataSnapshot.getValue();
                            if (location == null){
                                location = GenericMapsActivity.Ariel_University_lat + " " + GenericMapsActivity.Ariel_University_lng;
                            }
                            Intent gIntent = new Intent(BLEActivity.this, GenericMapsActivity.class);
                            gIntent.putExtra(GenericMapsActivity.LocationHandler, location);
                            gIntent.putExtra(GenericMapsActivity.fromActivity, dbLastBLE);
                            startActivity(gIntent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {  }
                    });

                }
            }
        });
    }

}