package com.rhul.fyp.asecav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.ramijemli.percentagechartview.PercentageChartView;
import com.rhul.fyp.asecav.libreav.receiver.AppListener;
import com.rhul.fyp.asecav.libreav.services.RealTimeService;
import com.rhul.fyp.asecav.permissionpoc.PermissionsScanner;
import com.rhul.fyp.asecav.permissionpoc.ResultActivity;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        setupSharedPreferences();

        final StatFs stats = new StatFs(Environment.getExternalStorageDirectory().getPath());
        final long gigabyteAvailable =
                (stats.getBlockSizeLong() * stats.getAvailableBlocksLong()) / (1024 * 1024 * 1024);
        final long totalSpace =
                (stats.getBlockSizeLong() * stats.getBlockCountLong()) / (1024 * 1024 * 1024);
        final long gigabyteUsed = totalSpace - gigabyteAvailable;
        final float percentUsed = (float) ((float) gigabyteUsed / (float) totalSpace) * 100;
        final TextView freeSpace = findViewById(R.id.free_space);
        final TextView usedSpace = findViewById(R.id.used_space);
        final TextView totalSpaceText = findViewById(R.id.total_space);
        final PercentageChartView storageStats = findViewById(R.id.storage_percent);
        final LinearLayout fileScan = findViewById(R.id.fileScan);
        final LinearLayout appScan = findViewById(R.id.appScan);
        final LinearLayout permissionManager = findViewById(R.id.permissionViewer);
        final LinearLayout maxLock = findViewById(R.id.appLocker);
        final TextView lastAppScan = findViewById(R.id.lastAppScan);


        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AsecAV");
        getSupportActionBar().setSubtitle("Main Menu");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        storageStats.setProgress(percentUsed, true);

        totalSpaceText.setText((int) totalSpace + "GB");
        freeSpace.setText((int) gigabyteAvailable + "GB");
        usedSpace.setText((int) gigabyteUsed + "GB");

        String lastScan = sharedPreferences.getString("lastScan", this.getString(R.string.never));
        lastAppScan.setText(lastScan);
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
            if (!scan.finished) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                        Intent resultScreen = new Intent(scan.contextRef.get(), ResultActivity.class);
                        scan.contextRef.get().startActivity(resultScreen);
                    }
                }, 800);
            } else {
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

    private void setupSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.openSource:
                startActivity(new Intent(this, com.rhul.fyp.asecav.AboutLibrariesActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}