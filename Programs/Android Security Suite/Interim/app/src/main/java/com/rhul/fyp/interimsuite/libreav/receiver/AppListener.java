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

package com.rhul.fyp.interimsuite.libreav.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rhul.fyp.interimsuite.libreav.scanners.AppScanner;

public class AppListener extends BroadcastReceiver {
    /**
     * Enables real time scanning of apps by running in background and waiting for android to notify that it has installed a new package then analyses the APK.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getBoolean("realTime", true)) {
            String packageName;
            if (intent.getAction() != null) {
                if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED") && intent.getDataString() != null) {
                    packageName = intent.getDataString().replace("package:", "");
                    final AppScanner scanner = new AppScanner(context, packageName, "realtime_scan");
                    scanner.execute();
                }
            }
        }
    }
}