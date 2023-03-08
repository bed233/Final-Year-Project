package com.rhul.fyp.asecav;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.ramijemli.percentagechartview.PercentageChartView;
import com.rhul.fyp.asecav.permissionpoc.PermissionsScanner;
import com.rhul.fyp.asecav.permissionpoc.ResultActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        final StatFs stats = new StatFs(Environment.getExternalStorageDirectory().getPath());
        final long gigabyteAvailable =
                (stats.getBlockSizeLong() * stats.getAvailableBlocksLong()) / (1024 * 1024 * 1024);
        final long totalSpace =
                (stats.getBlockSizeLong() * stats.getBlockCountLong()) / (1024 * 1024 * 1024) ;
        final long gigabyteUsed = totalSpace - gigabyteAvailable;
        final float percentUsed = (float)((float)gigabyteUsed / (float)totalSpace) * 100;
        final PercentageChartView storageStats = findViewById(R.id.storage_percent);
        storageStats.setProgress(percentUsed, true);
        final TextView freeSpace = findViewById(R.id.free_space);
        final TextView usedSpace = findViewById(R.id.used_space);
        final TextView totalSpaceText = findViewById(R.id.total_space);
        totalSpaceText.setText((int) totalSpace + "GB");
        freeSpace.setText((int) gigabyteAvailable + "GB");
        usedSpace.setText((int) gigabyteUsed + "GB");
        final Button fileScan = findViewById(R.id.fileScan);
        final Button appScan = findViewById(R.id.appScan);
        final Button permissionManager = findViewById(R.id.permissionManager);
        final Button maxLock = findViewById(R.id.appLocker);
        PermissionsScanner scan = new PermissionsScanner(this, MainActivity.this);
        scan.execute();
        maxLock.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(com.rhul.fyp.asecav.MainActivity.this,
                    com.rhul.fyp.asecav.maxlock.ui.SettingsActivity.class));
        });
        fileScan.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(com.rhul.fyp.asecav.MainActivity.this,
                    com.rhul.fyp.asecav.hypatia.MainActivity.class));
        });
        appScan.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(com.rhul.fyp.asecav.MainActivity.this,
                    com.rhul.fyp.asecav.libreav.activities.MainActivity.class));
        });
        permissionManager.setOnClickListener(view -> {
            if (!scan.finished){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                        Intent resultScreen = new Intent(scan.contextRef.get(), ResultActivity.class);
                        scan.contextRef.get().startActivity(resultScreen);                }
                }, 800);
            }else{
                finish();
                Intent resultScreen = new Intent(scan.contextRef.get(), ResultActivity.class);
                scan.contextRef.get().startActivity(resultScreen);
            }

        });
//        PermissionsScanner scan = new PermissionsScanner(this, MainActivity.this);
//        permissionManager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                scan.execute();
//            }
//        });
    }
}