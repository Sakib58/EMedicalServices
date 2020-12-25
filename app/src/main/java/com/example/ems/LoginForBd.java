package com.example.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ems.DriverAllActivity.Drivers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginForBd extends AppCompatActivity {

    EditText phone,pass;
    TextView loginBtn,regBtn;
    DatabaseReference databaseReference;
    String sphone,spass,sOriginalPass;
    public static String psphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_for_bd);
        phone=findViewById(R.id.etPhoneBdL);
        pass=findViewById(R.id.etPassBdL);
        loginBtn=findViewById(R.id.tvLoginBdL);
        databaseReference= FirebaseDatabase.getInstance().getReference("BloodDonors");
        sphone=phone.getText().toString();
        spass=pass.getText().toString();
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Login Btn clicked",Toast.LENGTH_SHORT).show();
                checkLoginCedintial();

            }
        });
    }
    public void checkLoginCedintial(){
        //Toast.makeText(getApplicationContext(),"Checking!",Toast.LENGTH_SHORT).show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (@NonNull DataSnapshot ds:dataSnapshot.getChildren()){
                    //Toast.makeText(getApplicationContext(),"Searching with key:"+ds.getKey()+" where phone:"+sphone,Toast.LENGTH_SHORT).show();
                    if(ds.getKey().equals(phone.getText().toString())){
                        //Toast.makeText(getApplicationContext(),"Found!",Toast.LENGTH_SHORT).show();
                        BloodDoners bloodDoners=new BloodDoners();
                        bloodDoners.setBdPass(ds.getValue(BloodDoners.class).getBdPass());
                        sOriginalPass=bloodDoners.getBdPass();
                        if (pass.getText().toString().equals(sOriginalPass)){
                            psphone=phone.getText().toString();
                            Intent inten=new Intent(getApplicationContext(),BloodDonorInfo.class);
                            startActivity(inten);
                            Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
