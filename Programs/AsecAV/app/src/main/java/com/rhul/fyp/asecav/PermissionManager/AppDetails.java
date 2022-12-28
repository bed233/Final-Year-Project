package com.rhul.fyp.asecav.PermissionManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhul.fyp.asecav.R;

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

    Button settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_activity_app_details);

        listOfPermissions = findViewById(R.id.permissions_list_text);
        pName = findViewById(R.id.app_package);
        icon = findViewById(R.id.app_icon);
        name = findViewById(R.id.app_name);
        settings = findViewById(R.id.openInSettingsButton);

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

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", packageName, null);
                openSettings.setData(uri);
                startActivity(openSettings);
            }
        });

        PackageManager packageManager = AppDetails.this.getPackageManager();
        try {
            icon.setImageDrawable(packageManager.getPackageInfo(packageName, 0).applicationInfo.loadIcon(packageManager));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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
