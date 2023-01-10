/*
Hypatia: A realtime malware scanner for Android
Copyright (c) 2017-2018 Divested Computing Group

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.rhul.fyp.asecav.hypatia;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rhul.fyp.asecav.BuildConfig;
import com.rhul.fyp.asecav.R;

import java.io.File;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;
    private MalwareScanner malwareScanner = null;

    private TextView logView;

    private static final String buildVersionName = BuildConfig.VERSION_NAME;

    private boolean scanSystem = false;
    private boolean scanApps = true;
    private boolean scanInternal = true;
    private boolean scanExternal = false;

    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 0;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setContext(getApplicationContext());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.hypatia_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        logView = findViewById(R.id.txtLogOutput);
        logView.setMovementMethod(new ScrollingMovementMethod());
        logView.setTextIsSelectable(true);

        logView.append(getString(R.string.app_copyright) + "\n");
        logView.append(getString(R.string.app_license) + "\n");
        logView.append(R.string.app_version + buildVersionName + "\n");
        logView.append(getString(R.string.app_db_type_clamav) + "\n\n");

        malwareScanner = new MalwareScanner(this, this, true);

        prefs = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (!malwareScanner.running) {
                startScanner();
            } else {
                logView.append("\n" + getString(R.string.main_cancelling_scan) + "\n\n");
                malwareScanner.cancel(true);
                malwareScanner.running = false;
            }
        });

        requestPermissions();

        Utils.considerStartService(this);
    }

    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hypatia_menu_main, menu);
        menu.findItem(R.id.toggleRealtime).setChecked(Utils.isServiceRunning(MalwareScannerService.class, this));
        menu.findItem(R.id.toggleOnionRouting).setChecked(prefs.getBoolean("ONION_ROUTING", false));
        return true;
    }

    /**
     * Checks whether the app has the required permissions and then requests permission from user if not.
     */
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE);
        }
    }

    private void showCredits() {
        Dialog creditsDialog;
        AlertDialog.Builder creditsBuilder = new AlertDialog.Builder(this);
        creditsBuilder.setTitle(getString(R.string.lblFullCredits));
        creditsBuilder.setItems(R.array.fullCredits, (dialog, which) -> {
            //do nothing
        });
        creditsDialog = creditsBuilder.create();
        creditsDialog.show();
    }

    private String localizeDBDescription(String desc) {
        return desc
                .replaceAll("AUTHOR", getString(R.string.db_desc_author))
                .replaceAll("LICENSE", getString(R.string.db_desc_license))
                .replaceAll("SIZE_SMALL", getString(R.string.db_desc_size_small))
                .replaceAll("SIZE_MEDIUM", getString(R.string.db_desc_size_medium))
                .replaceAll("SIZE_LARGE", getString(R.string.db_desc_size_large))
                .replaceAll("SIZE", getString(R.string.db_desc_size))
                .replaceAll("SOURCE", getString(R.string.db_desc_source));
    }

    /**
     * Allows user to select which databases they would like to have installed on their device/which the app should use.
     */
    private void selectDatabases() {
        final String[] databases = {
                localizeDBDescription("ClamAV: Android Only\n • SIZE: SIZE_MEDIUM\n • LICENSE: GPL-2.0\n • AUTHOR: Cisco\n • SOURCE: https://clamav.net\n"),
                localizeDBDescription("ClamAV: Main\n • SIZE: SIZE_LARGE\n • LICENSE: GPL-2.0\n • AUTHOR: Cisco\n • SOURCE: https://clamav.net\n"),
                localizeDBDescription("ClamAV: Daily\n • SIZE: SIZE_LARGE\n • LICENSE: GPL-2.0\n • AUTHOR: Cisco\n • SOURCE: https://clamav.net\n"),
                localizeDBDescription("ESET\n • SIZE: SIZE_SMALL\n • LICENSE: BSD 2-Clause\n • AUTHOR: ESET\n • SOURCE: https://github.com/eset/malware-ioc\n"),
                localizeDBDescription("Targeted Threats\n • SIZE: SIZE_SMALL\n • LICENSE: CC BY-SA 4.0\n • AUTHOR: Nex\n • SOURCE: https://github.com/botherder/targetedthreats")};
        final boolean[] databaseDefaults = {
                prefs.getBoolean("SIGNATURES_CLAMAV-ANDROID", true),
                prefs.getBoolean("SIGNATURES_CLAMAV-MAIN", false),
                prefs.getBoolean("SIGNATURES_CLAMAV-DAILY", false),
                prefs.getBoolean("SIGNATURES_ESET", true),
                prefs.getBoolean("SIGNATURES_TARGETEDTHREATS", true)};

        Dialog databaseDialog;
        AlertDialog.Builder databaseBuilder = new AlertDialog.Builder(this);
        databaseBuilder.setTitle(R.string.lblSelectDatabasesTitle);

        databaseBuilder.setMultiChoiceItems(databases, databaseDefaults, (dialogInterface, i, selected) -> databaseDefaults[i] = selected);
        databaseBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
            prefs.edit().putBoolean("SIGNATURES_CLAMAV-ANDROID", databaseDefaults[0]).apply();
            prefs.edit().putBoolean("SIGNATURES_CLAMAV-MAIN", databaseDefaults[1]).apply();
            prefs.edit().putBoolean("SIGNATURES_CLAMAV-DAILY", databaseDefaults[2]).apply();
            prefs.edit().putBoolean("SIGNATURES_ESET", databaseDefaults[3]).apply();
            prefs.edit().putBoolean("SIGNATURES_TARGETEDTHREATS", databaseDefaults[4]).apply();
        });

        databaseDialog = databaseBuilder.create();
        databaseDialog.show();
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggleOnionRouting:
                if (!item.isChecked()) {
                    if (Utils.isOrbotInstalled(this)) {
                        prefs.edit().putBoolean("ONION_ROUTING", !item.isChecked()).apply();
                        item.setChecked(true);
                    } else {
                        prefs.edit().putBoolean("ONION_ROUTING", false).apply();
                        item.setChecked(false);
                        Toast.makeText(this, R.string.lblOnionRoutingNotInstalled, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    prefs.edit().putBoolean("ONION_ROUTING", false).apply();
                    item.setChecked(false);
                }
                break;
            case R.id.mnuUpdateDatabase:
                if (prefs.getBoolean("ONION_ROUTING", false)) {
                    Utils.requestStartOrbot(this);
                    logView.append(getString(R.string.lblOnionRoutingEnabledHint) + "\n");
                }
                updateDatabase();
                break;
            case R.id.mnuSelectDatabases:
                selectDatabases();
                break;
            case R.id.mnuDatabaseServer:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.lblDatabaseServer));
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(Utils.getDatabaseURL(this));
                builder.setView(input);
                builder.setPositiveButton(getString(R.string.lblOverride), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newServer = input.getText().toString();
                        if(!newServer.endsWith("/")) {
                            newServer += "/";
                        }
                        prefs.edit().putString("DATABASE_SERVER", newServer).apply();
                    }
                });
                builder.setNegativeButton(getString(R.string.lblReset), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.edit().putString("DATABASE_SERVER", Utils.DATABASE_URL_DEFAULT).apply();
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            case R.id.toggleRealtime:
                Intent realtimeScanner = new Intent(getApplicationContext(), MalwareScannerService.class);
                if (!item.isChecked()) {
                    prefs.edit().putBoolean("autostart", true).apply();
                    Utils.considerStartService(this);
                } else {
                    stopService(realtimeScanner);
                    prefs.edit().putBoolean("autostart", false).apply();
                }
                item.setChecked(!item.isChecked());
                break;
            case R.id.mnuScanSystem:
                scanSystem = !item.isChecked();
                item.setChecked(scanSystem);
                break;
            case R.id.mnuScanApps:
                scanApps = !item.isChecked();
                item.setChecked(scanApps);
                break;
            case R.id.mnuScanInternal:
                scanInternal = !item.isChecked();
                item.setChecked(scanInternal);
                break;
            case R.id.mnuScanExternal:
                scanExternal = !item.isChecked();
                item.setChecked(scanExternal);
                break;
            case R.id.mnuFullCredits:
                showCredits();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startScanner() {
        malwareScanner = new MalwareScanner(this, this, true);
        malwareScanner.running = true;
        HashSet<File> filesToScan = new HashSet<>();
        if (scanSystem) {
            filesToScan.add(Environment.getRootDirectory());
        }
        if (scanApps) {
            for (ApplicationInfo packageInfo : getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA)) {
                filesToScan.add(new File(packageInfo.sourceDir));
            }
        }
        if (scanInternal) {
            filesToScan.add(Environment.getExternalStorageDirectory());
        }
        if (scanExternal) {
            filesToScan.add(new File("/storage"));
        }
        malwareScanner.executeOnExecutor(Utils.getThreadPoolExecutor(), filesToScan);
    }

    private void updateDatabase() {
        new Database((TextView) findViewById(R.id.txtLogOutput));
        Database.updateDatabase(this, Database.signatureDatabases);
        if (Database.isDatabaseLoaded()) {
            Utils.getThreadPoolExecutor().execute(() -> Database.loadDatabase(getApplicationContext(), true, Database.signatureDatabases));
        }
    }

}
