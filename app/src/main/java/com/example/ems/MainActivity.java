package com.example.ems;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView driverBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        driverBtn= findViewById(R.id.btnDriver);
        driverBtn.setPaintFlags(driverBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        driverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginForDriver.class);
                startActivity(intent);
            }
        });
    }
}
