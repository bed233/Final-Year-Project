package com.rhul.fyp.asecav.libreav.activities;

import static com.rhul.fyp.asecav.libreav.helper.ThemeToggleHelper.toggleDarkMode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhul.fyp.asecav.R;
import com.rhul.fyp.asecav.libreav.adapters.ScanResultAdapter;
import com.rhul.fyp.asecav.libreav.utils.AppConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Sets up and provides the UI with the data that it needs to show when User selects an app in particular to scan/after a scan. This includes showing a list of permissions
 * as well as the app's prediction score. Also enables the user to trigger a uninstall from within the app.
 */
public class AppDetails extends AppCompatActivity {
    String packageName,result,scan_mode;
    ImageView appIcon;
    TextView appName,resultType,permissionListText,pName,prediction;
    RecyclerView recyclerView;
    Button uninstall;
    ArrayList<String> arrayList;
    ScanResultAdapter adapter;
    SharedPreferences sharedPreferences;

    /**
     * Handles the app specific details page by handles all data required to be viewed for every app
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libreav_activity_app_details);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        toggleDarkMode(sharedPreferences.getBoolean("darkMode", false));

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setTitle(this.getString(R.string.app_details));
//        }

        getSupportActionBar().setTitle(getIntent().getExtras().getString("appName"));
        getSupportActionBar().setSubtitle("App Scanner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setSubtitleTextColor(getResources().getColor(android.R.color.white));
        toolBar.setTitleTextColor(getResources().getColor(android.R.color.white));

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.primary_red));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        permissionListText=findViewById(R.id.permissions_list_text);
        pName=findViewById(R.id.app_package);
        prediction=findViewById(R.id.app_prediction_score);
        appIcon=findViewById(R.id.app_icon);
        appName=findViewById(R.id.app_name);
        resultType=findViewById(R.id.app_prediction_label);
        recyclerView=findViewById(R.id.permission_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uninstall=findViewById(R.id.uninstallButton);
        Intent intent =getIntent();
        arrayList=intent.getStringArrayListExtra("permissionList");
        if(arrayList == null || arrayList.isEmpty()){
            permissionListText.setText(this.getString(R.string.no_permissions_required));
        }
        else {
            adapter= new ScanResultAdapter(this,arrayList);
            recyclerView.setAdapter(adapter);
        }
        appName.setText(intent.getExtras().getString("appName"));
        result=intent.getExtras().getString("result");
        resultType.setText(result);
        scan_mode = intent.getExtras().getString("scan_mode");
        if (result.equals(this.getString(R.string.malware))) {
            resultType.setTextColor(Color.parseColor("#FF0000"));
        } else if (result.equals(this.getString(R.string.safe))) {
            resultType.setTextColor(Color.parseColor("#008000"));
        } else if (result.equals(this.getString(R.string.risky))) {
            resultType.setTextColor(Color.parseColor("#FFA500"));
        } else {
            resultType.setTextColor(Color.parseColor("#0080FF"));
        }

        if(scan_mode.equalsIgnoreCase("normal_scan")) {
            TextView sha256Label = findViewById(R.id.sha256_label);
            HorizontalScrollView sha256Container = findViewById(R.id.sha256_container);
            sha256Label.setVisibility(View.GONE);
            sha256Container.setVisibility(View.GONE);
        } else {
            String sha256Hash = intent.getExtras().getString(AppConstants.SHA_256_HASH);
            TextView sha256HashTv = findViewById(R.id.sha256_text);
            sha256HashTv.setText(sha256Hash);
        }

        if(scan_mode.equalsIgnoreCase("realtime_scan") || scan_mode.equalsIgnoreCase("normal_scan") || scan_mode.equalsIgnoreCase("custom_scan")) {
            uninstall.setVisibility(View.VISIBLE);
            uninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivityForResult(intent, 1);
                }
            });
        }else{
            uninstall.setVisibility(View.INVISIBLE);
        }

        packageName=intent.getExtras().getString("packageName");
        pName.setText(packageName);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        prediction.setText(this.getString(R.string.prediction_score) + " " + df.format(intent.getExtras().getFloat("prediction")));
        PackageManager pm = AppDetails.this.getPackageManager();
        try {
            appIcon.setImageDrawable(pm.getPackageInfo(packageName, 0).applicationInfo.loadIcon(pm));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether a particular app is installed which allows the app to check when an uninstall has been successful.
     * @param packageName - Name of the app
     * @param packageManager - Built in Android class for retrieving various information about currently installed APK's (Apps).
     * @return - Returns whether the app is installed or not after checking
     */
    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Once App is checked whether is it installed or not, This method will check then output a message to the user to let them know.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PackageManager pm = this.getPackageManager();
        if(requestCode==1 && !isPackageInstalled(packageName,pm)) {
            Toast.makeText(AppDetails.this,this.getString(R.string.uninstall_successful),Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(AppDetails.this, MainActivity.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id== android.R.id.home){
            startActivity(new Intent(this,
                    com.rhul.fyp.asecav.libreav.activities.ResultActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        super.onBackPressed();
//        startActivity(new Intent(this,MainActivity.class));
        return true;
    }

    @Override
    public void onBackPressed(){
        finish();
        super.onBackPressed();
        //startActivity(new Intent(this,MainActivity.class));
    }
}