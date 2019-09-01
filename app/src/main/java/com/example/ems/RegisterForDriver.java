package com.example.ems;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ems.DriverAllActivity.Drivers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterForDriver extends AppCompatActivity {
    CheckBox agreeTerms;
    TextView reg;
    TextView terms,loginTv;
    EditText email,pass,name,phone,licence,cPass;
    public static String mail,passWord;

    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_driver);

        databaseReference= FirebaseDatabase.getInstance().getReference("Driver");
        agreeTerms=findViewById(R.id.cvReg);
        reg=findViewById(R.id.tvRegRA);
        reg.setVisibility(View.INVISIBLE);
        terms=findViewById(R.id.tvTerms);
        email=findViewById(R.id.etmailD);
        mail=email.getText().toString();
        pass=findViewById(R.id.etPassD);
        passWord=pass.getText().toString();
        firebaseAuth=FirebaseAuth.getInstance();
        loginTv=findViewById(R.id.tvLoginD);
        name=findViewById(R.id.etNameD);
        phone=findViewById(R.id.etMobileD);
        licence=findViewById(R.id.etLicenceD);
        cPass=findViewById(R.id.etCpassD);


        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterForDriver.this,LoginForDriver.class);
                startActivity(intent);
            }
        });

        terms.setMovementMethod(new ScrollingMovementMethod());

        agreeTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    reg.setVisibility(View.VISIBLE);
                }
                else {
                    reg.setVisibility(View.INVISIBLE);
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Intent intent=new Intent(RegisterForDriver.this,LoginForDriver.class);
                                                startActivity(intent);
                                                Toast.makeText(getApplicationContext(),"Registration successful.Please check your email for verification",Toast.LENGTH_LONG).show();
                                                saveDriverInfo();


                                            }else {
                                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });

                                }else {
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });

    }
    public void saveDriverInfo(){
        String naMe=name.getText().toString();
        String mobile=phone.getText().toString();
        String eMail=email.getText().toString();
        String licenseNo=licence.getText().toString();
        String id=databaseReference.push().getKey();
        Drivers drivers=new Drivers(id,naMe,eMail,mobile,licenseNo);
        databaseReference.child(id).setValue(drivers);

    }


}
