package com.example.newpc.qrcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button MAP, scan, ble;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;

    private static String UID = null;
    private DatabaseReference dbUsers   = FirebaseDatabase.getInstance().getReference().child("users");
    static DatabaseReference myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance(); //initializing firebase authentication object

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //displaying logged in user name
        textViewUserEmail.setText(textViewUserEmail.getText() + " " +user.getEmail());

        UID = user.getUid();
        myUser = dbUsers.child(UID);

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        MAP = (Button) findViewById(R.id.MAP);
        scan = (Button) findViewById(R.id.scan);
        ble =(Button) findViewById(R.id.ble);
        MAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(ProfileActivity.this, MapsActivity.class);
                startActivity(gIntent);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(ProfileActivity.this, ReaderActivity.class);
                startActivity(rIntent);
            }
        });

        ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(ProfileActivity.this, BLEActivity.class);
                startActivity(rIntent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}