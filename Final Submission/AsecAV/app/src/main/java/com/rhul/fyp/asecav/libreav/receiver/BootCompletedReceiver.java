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

package com.rhul.fyp.asecav.libreav.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.rhul.fyp.asecav.libreav.services.RealTimeService;

public class BootCompletedReceiver extends BroadcastReceiver {
    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.LOCKED_BOOT_COMPLETED")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!isServiceRunning(RealTimeService.class, context)) {
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        if (sharedPreferences.getBoolean("realTime", true)) {
                            context.startForegroundService(new Intent(context, RealTimeService.class));
                        }
                    }
                }
            }
        }
    }

    private boolean isServiceRunning(Class serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
