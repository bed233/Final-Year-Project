package com.rhul.fyp.interimsuite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        final Button fileScan = findViewById(R.id.fileScan);
        final Button appScan = findViewById(R.id.appScan);
        fileScan.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this,
                    com.rhul.fyp.interimsuite.hypatia.MainActivity.class));
        });
        appScan.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this,
                    com.rhul.fyp.interimsuite.libreav.activities.MainActivity.class));
        });
    }
}