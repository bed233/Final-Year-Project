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

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.rhul.fyp.asecav.R;
import com.rhul.fyp.asecav.libreav.scanners.ScannerTask;

public class ScanActivity extends AppCompatActivity {
    /**
     * Handles screen when scan is currently in progress by updating the progress bar.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libreav_activity_scan);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(this.getString(R.string.scanning));
        }
        boolean withSysApps = getIntent().getBooleanExtra("withSysApps", false);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView percentText = findViewById(R.id.percentText);
        TextView statusText = findViewById(R.id.statusText);
        TextView secondarystatusText = findViewById(R.id.secondaryStatusText);
        Button stopButton = findViewById(R.id.stopButton);

        progressBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        progressBar.setProgress(0);


        final ScannerTask scanner = new ScannerTask(this, ScanActivity.this);
        scanner.setProgressBar(progressBar);
        scanner.setPercentText(percentText);
        scanner.setStatusText(statusText);
        scanner.setSecondaryStatusText(secondarystatusText);
        scanner.setWithSysApps(withSysApps);
        scanner.execute();

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner.cancel(true);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Back button is disabled while scanning
    }
}