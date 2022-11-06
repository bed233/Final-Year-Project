package com.rhul.fyp.permissionpoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppDetails extends AppCompatActivity {
    String packageName;
    ImageView icon;
    TextView name;
    TextView listOfPermissions;
    TextView pName;
    PermissionListAdapter permissionsList;
    RecyclerView recyclerView;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        listOfPermissions = findViewById(R.id.permissions_list_text);
        pName = findViewById(R.id.app_package);
        icon = findViewById(R.id.app_icon);
        name = findViewById(R.id.app_name);

        recyclerView = findViewById(R.id.permission_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        arrayList = intent.getStringArrayListExtra("permissionList");
        permissionsList = new PermissionListAdapter(this, arrayList);
        if (arrayList == null){
            listOfPermissions.setText("Is Empty");
        }
        recyclerView.setAdapter(permissionsList);

        name.setText(intent.getExtras().getString("appName"));

        packageName = intent.getExtras().getString("packageName");
        pName.setText(packageName);

        PackageManager packageManager = AppDetails.this.getPackageManager();
        try {
            icon.setImageDrawable(packageManager.getPackageInfo(packageName, 0).applicationInfo.loadIcon(packageManager));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager){
        try{
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
