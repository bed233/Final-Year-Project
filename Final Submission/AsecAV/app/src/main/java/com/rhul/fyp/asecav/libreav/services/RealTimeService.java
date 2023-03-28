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

package com.rhul.fyp.asecav.libreav.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.rhul.fyp.asecav.R;
import com.rhul.fyp.asecav.libreav.activities.MainActivity;
import com.rhul.fyp.asecav.libreav.scanners.AppScanner;

public class RealTimeService extends Service {
    private BroadcastReceiver receiver;

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        String CHANNEL_ID = "channel_100";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_NAME = "PROJECT MATRIS";
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder.setSmallIcon(R.drawable.ic_notification)
                    .setOngoing(true)
                    .setContentTitle("LibreAV")
                    .setContentText(getApplicationContext().getString(R.string.real_time_scan_on));
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT |
                    PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.setContentIntent(contentIntent);
            Notification notification = notificationBuilder.build();
            int NOTIFICATION_ID = 200;
            startForeground(NOTIFICATION_ID, notification);
        }
        receiver = new BroadcastReceiver() {
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
        };
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}