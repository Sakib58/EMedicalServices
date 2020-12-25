package com.example.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SearchForBlood extends AppCompatActivity {
    Spinner bloodGroup;
    TextView tv;
    EditText location;
    String slocation,sbg,lastDate;
    String result="";
    Button search;
    int i;
    DatabaseReference databaseReference,databaseReference2;
    ArrayList<String> validList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_blood);
        bloodGroup=findViewById(R.id.bloodGroup);
        location=findViewById(R.id.location);
        search=findViewById(R.id.searchFB);
        tv=findViewById(R.id.searchResult);
        validList=new ArrayList<String>();
        tv.setMovementMethod(new ScrollingMovementMethod());
        databaseReference= FirebaseDatabase.getInstance().getReference("BloodDonors");
        databaseReference2= FirebaseDatabase.getInstance().getReference("DonorsDonateDates");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result="";

                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(@NonNull DataSnapshot ds:dataSnapshot.getChildren()){
                                DateAndPhone dateAndPhone2=new DateAndPhone();
                                dateAndPhone2.setDonDate(ds.getValue(DateAndPhone.class).getDonDate());
                                lastDate=dateAndPhone2.getDonDate();
                                //Toast.makeText(getApplicationContext(),"Last Date:"+lastDate,Toast.LENGTH_SHORT).show();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();
                            String currDate=formatter.format(date);
                            try {
                                if(isValid(lastDate,currDate)){
                                    validList.add(ds.getKey());
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                        //start

                        slocation=location.getText().toString();
                        sbg=bloodGroup.getSelectedItem().toString();
                        if(slocation.equals("")){
                            Toast.makeText(getApplicationContext(),"Enter location",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(sbg.equals("Select Blood Group")){
                            Toast.makeText(getApplicationContext(),"Select required blood group",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Toast.makeText(getApplicationContext(),validList.get(0),Toast.LENGTH_SHORT).show();
                                i=0;
                                for(@NonNull DataSnapshot ds:dataSnapshot.getChildren()){
                                    BloodDoners bloodDoners=new BloodDoners();
                                    bloodDoners.setBdLocation(ds.getValue(BloodDoners.class).getBdLocation());
                                    bloodDoners.setBdBg(ds.getValue(BloodDoners.class).getBdBg());


                                    if (bloodDoners.getBdLocation().equals(location.getText().toString()) && bloodDoners.getBdBg().equals(bloodGroup.getSelectedItem().toString())){
                                        bloodDoners.setBdPhone(ds.getValue(BloodDoners.class).getBdPhone());

                                        if(validList.indexOf(bloodDoners.getBdPhone())>=0){

                                            i++;
                                            bloodDoners.setBdName(ds.getValue(BloodDoners.class).getBdName());
                                            result=result+i+".\n"+"Name: "+bloodDoners.getBdName()+"\n Phone: "+bloodDoners.getBdPhone()+"\n";
                                        }

                                    }
                                }
                                if(result.length()<2)tv.setText("Not Found");
                                else{
                                    tv.setText(result);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        //end
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //original start

                //original end

            }
        });
    }
    public boolean isValid(String date1,String date2) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date firstDate = sdf.parse(date1);
        Date secondDate = sdf.parse(date2);

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        //Toast.makeText(getApplicationContext(),"Today:"+date2+"Last Date:"+date1+" Diff:"+diff,Toast.LENGTH_SHORT).show();
        if(diff>=120)return true;
        else return false;


    }
}
