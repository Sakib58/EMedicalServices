package com.example.ems;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    CardView driverBtn,needAmb,DonateBloodBtn,btnNeedBlood;
    DatabaseReference databaseReference;
    public static ArrayList<Double> myArray;
    public static String[] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllLocations();
        driverBtn= findViewById(R.id.btnDriver);
        driverBtn.setVisibility(View.INVISIBLE);
        DonateBloodBtn=findViewById(R.id.btnDonateBlood);
        needAmb=findViewById(R.id.btnNeedAmb);
        btnNeedBlood=findViewById(R.id.btnNeedBlood);
        btnNeedBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SearchForBlood.class);
                startActivity(intent);
            }
        });
        arr=new String[500];
        DonateBloodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),BloodDoner.class);
                startActivity(intent);
            }
        });
        driverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginForDriver.class);
                startActivity(intent);
            }
        });
        needAmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    goToMaps();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Internal Error",Toast.LENGTH_SHORT).show();
                }

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for(@NonNull DataSnapshot ds:dataSnapshot.getChildren()){
                    arr[i]=ds.getKey();
                    i++;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getAllLocations(){
        databaseReference = FirebaseDatabase.getInstance().getReference("ActiveLocation");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doNext(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void doNext(@NonNull DataSnapshot dataSnapshot) {
        myArray = new ArrayList<>(100);
        for (@NonNull DataSnapshot ds : dataSnapshot.getChildren()) {
            LiveLocations liveLocations = new LiveLocations();
            liveLocations.setLatitude(ds.getValue(LiveLocations.class).getLatitude());
            liveLocations.setLongitude(ds.getValue(LiveLocations.class).getLongitude());
            myArray.add(liveLocations.getLatitude());
            myArray.add(liveLocations.getLongitude());
        }
    }
    public void goToMaps(){
        try {
            Intent intent=new Intent(MainActivity.this,ShowAllAvailableLocations.class);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Internal Error",Toast.LENGTH_SHORT).show();
        }

    }
}
