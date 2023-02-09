package com.rhul.fyp.asecav.permissionpoc;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.rhul.fyp.asecav.MainActivity;
import com.rhul.fyp.asecav.R;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    RecyclerView appCardList;
    RecyclerView.LayoutManager layout;
    AppAdapter adapter;
    public static ArrayList<AppParcelInfo> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.permission_apps_list);
        appCardList = findViewById(R.id.resultList);
        if(Build.VERSION.SDK_INT >=21){
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AsecAV");
        getSupportActionBar().setSubtitle("Permission Viewer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));


        layout = new LinearLayoutManager(this);
        appCardList.setLayoutManager(layout);

        adapter = new AppAdapter(this, apps);
        appCardList.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, com.rhul.fyp.asecav.MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.permission_viewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, com.rhul.fyp.asecav.MainActivity.class));
            return true;
        } else if (id == R.id.refresh) {
            PermissionsScanner scan = new PermissionsScanner(this, ResultActivity.this);
            scan.execute();
            if (!scan.finished) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                        Intent resultScreen = new Intent(scan.contextRef.get(), ResultActivity.class);
                        scan.contextRef.get().startActivity(resultScreen);
                    }
                }, 500);

            } else {
                Intent resultScreen = new Intent(scan.contextRef.get(), ResultActivity.class);
                scan.contextRef.get().startActivity(resultScreen);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
