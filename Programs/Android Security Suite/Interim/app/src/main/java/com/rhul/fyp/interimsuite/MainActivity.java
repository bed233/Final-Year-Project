package com.rhul.fyp.interimsuite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.rhul.fyp.interimsuite.permissionpoc.PermissionsScanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        final Button fileScan = findViewById(R.id.fileScan);
        final Button appScan = findViewById(R.id.appScan);
        final Button permissionManager = findViewById(R.id.permissionManager);
        fileScan.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(com.rhul.fyp.interimsuite.MainActivity.this,
                    com.rhul.fyp.interimsuite.hypatia.MainActivity.class));
        });
        appScan.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(com.rhul.fyp.interimsuite.MainActivity.this,
                    com.rhul.fyp.interimsuite.libreav.activities.MainActivity.class));
        });
        PermissionsScanner scan = new PermissionsScanner(this, MainActivity.this);
        permissionManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan.execute();
            }
        });
    }
}