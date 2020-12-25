package com.example.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class RegisterForBD extends AppCompatActivity {
    Spinner selectBg;
    EditText fName,phone,location,pass,cPass,code;
    CheckBox cv;
    TextView regBtn,loginBtn,submitBtn;
    public static String codeSent;

    String sfname,sphone,slocation,spass,scpass,sbg,sCode;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    //String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_bd);
        selectBg=findViewById(R.id.etBgBd);
        fName=findViewById(R.id.etNameBd);
        phone=findViewById(R.id.etMobileBd);
        location=findViewById(R.id.etLocationBd);
        pass=findViewById(R.id.etPassBd);
        cPass=findViewById(R.id.etCpassBd);
        cv=findViewById(R.id.cvRegBd);
        regBtn=findViewById(R.id.tvRegBd);
        loginBtn=findViewById(R.id.tvLoginBd);
        mAuth = FirebaseAuth.getInstance();
        regBtn.setVisibility(View.INVISIBLE);
        code=findViewById(R.id.etCode);
        submitBtn=findViewById(R.id.tvSubCode);
        databaseReference= FirebaseDatabase.getInstance().getReference("BloodDonors");

        code.setVisibility(View.INVISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);

        cv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    regBtn.setVisibility(View.VISIBLE);
                }
                else
                    regBtn.setVisibility(View.INVISIBLE);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sfname=fName.getText().toString();
                sphone=phone.getText().toString();
                slocation=location.getText().toString();
                sbg= selectBg.getSelectedItem().toString();
                spass=pass.getText().toString();
                scpass=cPass.getText().toString();
                if(sfname.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter your name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sphone.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter your phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(slocation.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter your Location",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sbg.equals("Select Blood Group")){
                    Toast.makeText(getApplicationContext(),"Select your blood group",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(spass.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter your Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(spass.equals(scpass)){
                    //Actual implementation
                    //Toast.makeText(getApplicationContext(),"Everything is working fine",Toast.LENGTH_SHORT).show();
                    try{
                        sendVerificationCode();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Internal Error",Toast.LENGTH_SHORT).show();
                    }
                    //Intent intent=new Intent(getApplicationContext(),VerifyPhoneBd.class);
                    //startActivity(intent);
                    code.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Please enter confirm password correctly",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sCode=code.getText().toString();
                try{
                    verifySignInCode();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Internal Error",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void verifySignInCode(){
        String code = sCode;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(RegisterForBD.codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Registration Successful!",Toast.LENGTH_SHORT).show();
                            saveInfoToDatabase();
                            Intent intent=new Intent(getApplicationContext(),LoginForBd.class);
                            startActivity(intent);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
    public void saveInfoToDatabase(){
        BloodDoners bloodDoners=new BloodDoners(sfname,sphone,slocation,sbg,spass);
        databaseReference.child(sphone).setValue(bloodDoners);
    }


    private void sendVerificationCode(){

        String phone = sphone;

        if(phone.isEmpty()){
            //editTextPhone.setError("Phone number is required");
            //editTextPhone.requestFocus();
            return;
        }

        if(phone.length() < 10 ){
            //editTextPhone.setError("Please enter a valid phone");
            //editTextPhone.requestFocus();
            //return;
        }

        phone="+88"+phone;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };
}