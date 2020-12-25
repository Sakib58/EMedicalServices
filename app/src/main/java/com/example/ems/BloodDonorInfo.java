package com.example.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BloodDonorInfo extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{
    TextView setDate,lastDon;
    Button pickADate,submitDate,donationRec;
    DatabaseReference databaseReference;
    ArrayList<String> recList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donor_info);
        pickADate=findViewById(R.id.pickDate);
        setDate=findViewById(R.id.setDate);
        recList=new ArrayList<>();
        donationRec=findViewById(R.id.DonationRecDate);
        submitDate=findViewById(R.id.submitDate);
        submitDate.setVisibility(View.INVISIBLE);
        lastDon=findViewById(R.id.lastDonationDate);
        databaseReference= FirebaseDatabase.getInstance().getReference("DonorsDonateDates");
        pickADate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                submitDate.setVisibility(View.VISIBLE);
            }
        });
        submitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateAndPhone dateAndPhone=new DateAndPhone(setDate.getText().toString());
                databaseReference.child(LoginForBd.psphone).setValue(dateAndPhone);
                Toast.makeText(getApplicationContext(),"Phone:"+LoginForBd.psphone+" Date:"+setDate.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        donationRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (@NonNull DataSnapshot ds:dataSnapshot.getChildren()){
                            //Toast.makeText(getApplicationContext(),"Checking..."+LoginForBd.psphone+" getChild: "+ds.getKey(),Toast.LENGTH_SHORT).show();
                            if(ds.getKey().equals(LoginForBd.psphone)){
                                //Toast.makeText(getApplicationContext(),"Found:",Toast.LENGTH_SHORT).show();
                                DateAndPhone dateAndPhone=new DateAndPhone();
                                dateAndPhone.setDonDate(ds.getValue(DateAndPhone.class).getDonDate());
                                lastDon.setText(dateAndPhone.getDonDate());
                                //Toast.makeText(getApplicationContext(),ds.getRef().getKey(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);
        String day=String.valueOf(i2);
        String mon=String.valueOf(i1+1);
        if (day.length()<2)day="0"+i2;
        if (mon.length()<2)mon="0"+(i1+1);
        String currentDateString = day+"/"+mon+"/"+i;
        setDate.setText(currentDateString);
    }
}
