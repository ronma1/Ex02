package com.example.newpc.qrcode;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.HashMap;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String    TAG = "RangingActivity";
    private static final int         distRow = 2;
    private BeaconManager            beaconManager;
    private TableLayout              table;
    private Beacon                   b;
    private HashMap<String, Integer> tableContent;
    private int beaconCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        tableContent = new HashMap<>();
        table = (TableLayout) findViewById(R.id.table_lay);
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

        // TODO: ADD FIREBASE CONNECTION ALSO HERE

    }
}