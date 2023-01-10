/*
 * LibreAV - Anti-malware for Android using machine learning
 * Copyright (C) 2020 Project Matris
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rhul.fyp.asecav.libreav.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.rhul.fyp.asecav.libreav.scanners.ApkScanner;
import com.rhul.fyp.asecav.libreav.services.RealTimeService;
import com.rhul.fyp.asecav.libreav.settings.SettingsActivity;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

import com.rhul.fyp.asecav.R;

import static com.rhul.fyp.asecav.libreav.helper.ThemeToggleHelper.toggleDarkMode;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    Button scanButton;
    TextView lastScanText;
    private final Context context = this;
    private boolean withSysApps = false;
    SharedPreferences sharedPreferences;
    FilePickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libreav_activity_main);
        setupSharedPreferences();

        toggleDarkMode(sharedPreferences.getBoolean("darkMode", false));

        Toolbar toolbar = findViewById(R.id.toolbar);
        lastScanText = findViewById(R.id.textView1);

        setSupportActionBar(toolbar);

        withSysApps = sharedPreferences.getBoolean("includeSystemApps", false);

        String lastScan = sharedPreferences.getString("lastScan", this.getString(R.string.never));
        lastScanText.setText(this.getString(R.string.last_scan) + " " + lastScan);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isServiceRunning(RealTimeService.class)) {
                if (sharedPreferences.getBoolean("realTime", true)) {
                    startService(new Intent(this, RealTimeService.class));
                }
            }
        }

        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v -> context.startActivity(new Intent(context, ScanActivity.class).putExtra("withSysApps", withSysApps)));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
            super.onBackPressed();
        }
    }

    /**
     * Check whether the app has Storage Permissions on the external(SD Card) as well as internal Storage device.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    if (dialog != null) {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * Check whether the app has all permissions needed to run.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (dialog != null) {   //Show dialog if the read permission has been granted.
                    dialog.show();
                }
            } else {
                //Permission has not been granted. Notify the user.
                Toast.makeText(MainActivity.this, this.getString(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("includeSystemApps") && sharedPreferences.getBoolean(key, false)) {
            withSysApps = sharedPreferences.getBoolean(key, false);
            Toast.makeText(getApplicationContext(), this.getString(R.string.scan_system_apps_toast), Toast.LENGTH_LONG)
                    .show();
        } else if (key.equals("darkMode") && sharedPreferences.getBoolean(key, true)) {
            toggleDarkMode(sharedPreferences.getBoolean("darkMode", true));
            Toast.makeText(getApplicationContext(), this.getString(R.string.dark_mode_enabled_toast), Toast.LENGTH_LONG)
                    .show();
        } else if (key.equals("darkMode") && !sharedPreferences.getBoolean(key, true)) {
            toggleDarkMode(sharedPreferences.getBoolean("darkMode", true));
            Toast.makeText(getApplicationContext(), this.getString(R.string.dark_mode_disabled_toast), Toast.LENGTH_LONG)
                    .show();
        } else if (key.equals("realTime") && sharedPreferences.getBoolean(key, true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startService(new Intent(this, RealTimeService.class));
            }
        } else if (key.equals("realTime") && !sharedPreferences.getBoolean(key, true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopService(new Intent(this, RealTimeService.class));
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scan_apk) {
            DialogProperties properties = new DialogProperties();
            properties.selection_mode = DialogConfigs.SINGLE_MODE;
            properties.selection_type = DialogConfigs.FILE_SELECT;
            properties.root = Environment.getExternalStorageDirectory();
            properties.error_dir = properties.root;
            properties.offset = properties.root;
            properties.extensions = new String[]{"apk"};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }

            dialog = new FilePickerDialog(MainActivity.this, properties);
            dialog.setTitle(this.getString(R.string.select_a_file));
            dialog.setDialogSelectionListener(files -> {
                //files is the array of the paths of files selected by the Application User.
                if (files != null) {
                    File selectedFile = new File(files[0]);
                    if (selectedFile.exists() && selectedFile.isFile()) {
                        final ApkScanner apkScanner = new ApkScanner(context, files[0]);
                        apkScanner.execute();
                        //Toast.makeText(context,files[0],Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.file_does_not_exist), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.error_loading_file), Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
        } else if (id == R.id.nav_custom_scan) {
            this.startActivity(new Intent(this, CustomScanActivity.class).putExtra("withSysApps", withSysApps));
        } else if (id == R.id.nav_settings) {
            this.startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_help) {
            this.startActivity(new Intent(this, HelpActivity.class));
        } else if (id == R.id.nav_about) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://projectmatris.tech")));
        } else if (id == R.id.nav_bug_report) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/projectmatris/antimalwareapp/issues/new")));
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareContent = "Check out LibreAV, a free and open-source anti-malware using machine learning: https://f-droid.org/en/packages/tech.projectmatris.antimalwareapp/";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Project Matris");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        } else if (id == R.id.nav_sponsor) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://opencollective.com/libreav")));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Check if a service is running or not
     *
     * @param serviceClass class of the service
     * @return Boolean value specifying service is running or not
     */
    private boolean isServiceRunning(Class serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}