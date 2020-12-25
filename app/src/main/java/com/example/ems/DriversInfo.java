package com.example.ems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriversInfo extends AppCompatActivity {
    TextView name,phone;
    String key;
    DatabaseReference databaseReference;
    String dName,dPhone;
    Button findInf,bCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_info);
        name=findViewById(R.id.dName);
        phone=findViewById(R.id.dPhone);
        findInf=findViewById(R.id.dFindInfo);
        bCall=findViewById(R.id.dCall);
        databaseReference= FirebaseDatabase.getInstance().getReference("Driver");
        findInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText(ShowAllAvailableLocations.clickedName);
                phone.setText(ShowAllAvailableLocations.clickedPhone);
            }
        });
        bCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ShowAllAvailableLocations.clickedPhone));
                startActivity(callIntent);
            }
        });

    }
}
