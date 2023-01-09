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
package com.rhul.fyp.asecav.Hypatia;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import com.rhul.fyp.asecav.BuildConfig;

import java.io.File;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Utils {

    private static Context context = null;
    public final static int MAX_SCAN_SIZE = (1000 * 1000) * 80; //80MB
    public final static int MAX_SCAN_SIZE_REALTIME = MAX_SCAN_SIZE / 2; //40MB
    public final static String DATABASE_URL_DEFAULT = "https://divested.dev/MalwareScannerSignatures/";

    public final static int MAX_HASH_LENGTH = 12;

    public static final AtomicInteger FILES_SCANNED = new AtomicInteger();
    private static ThreadPoolExecutor threadPoolExecutor = null;

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = getNewThreadPoolExecutor(getMaxThreads());
        }
        return threadPoolExecutor;
    }

    public static ThreadPoolExecutor getNewThreadPoolExecutor(int threads) {
        return new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(16), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static int getMaxThreads() {
        int maxThreads = Runtime.getRuntime().availableProcessors();
        if (maxThreads > 4) {
            maxThreads = 4;
        }
        if(maxThreads < 2) {
            maxThreads = 2;
        }
        return maxThreads;
    }

    public static HashSet<File> getFilesRecursive(File root) {
        HashSet<File> filesAll = new HashSet<>();
        if (root.isFile()) { //TODO: Skip this
            filesAll.add(root);
            return filesAll;
        } else {
            File[] files = root.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        filesAll.addAll(getFilesRecursive(f));
                    } else {
                        if (f.length() <= MAX_SCAN_SIZE && f.canRead()) {//Exclude files larger than limit for performance
                            filesAll.add(f);
                        }
                    }
                }
            }
        }
        return filesAll;
    }

    //Credit (CC BY-SA 3.0): https://stackoverflow.com/a/5921190
    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getDatabaseURL(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        return prefs.getString("DATABASE_SERVER", DATABASE_URL_DEFAULT);
    }

    public static void considerStartService(Context context) {
        if (!Utils.isServiceRunning(MalwareScannerService.class, context)) {
            SharedPreferences prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            boolean autostart = prefs.getBoolean("autostart", false);

            if (autostart) {
                Intent realtimeScanner = new Intent(context, MalwareScannerService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(realtimeScanner);
                } else {
                    context.startService(realtimeScanner);
                }
            }
        }
    }

    //Credit (CC BY-SA 3.0): https://stackoverflow.com/a/6758962
    public static boolean isPackageInstalled(Context context, String packageID) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageID, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public static boolean isOrbotInstalled(Context context) {
        return isPackageInstalled(context, "org.torproject.android");
    }

    //Credit: OrbotHelper/NetCipher
    public static void requestStartOrbot(Context context) {
        Intent intent = new Intent("org.torproject.android.intent.action.START");
        intent.setPackage("org.torproject.android");
        intent.putExtra("org.torproject.android.intent.extra.PACKAGE_NAME", context.getPackageName());
        context.sendBroadcast(intent);
    }

    //Credit: https://www.geekality.net/2013/04/30/java-simple-check-to-see-if-a-server-is-listening-on-a-port/
    public static boolean isPortListening(String host, int port) {
        try (Socket s = new Socket(host, port)) {
            s.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void waitUntilOrbotIsAvailable() {
        int tries = 0;
        while (!isPortListening("127.0.0.1", 9050) && tries <= 60) {
            tries++;
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Utils.context = context;
    }
}
