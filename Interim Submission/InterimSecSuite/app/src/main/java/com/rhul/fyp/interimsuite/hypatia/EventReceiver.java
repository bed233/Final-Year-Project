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
package com.rhul.fyp.interimsuite.hypatia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.io.File;
import java.util.HashSet;

public class EventReceiver extends BroadcastReceiver {

    @Override
    public final void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Utils.considerStartService(context);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            if (intent.getDataString().contains(context.getPackageName())) {
                Utils.considerStartService(context); //We've been updated, restart service
            } else {
                //scanApp(context, intent.getDataString());//An app was updated, scan it
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            //scanApp(context, intent.getDataString());//An app was installed, scan it
        }
    }

    /**
     * Adds files that are due to be scanned into an hashset that is then passed to the Malware Scanner class to be scanned.
     * @param context
     * @param appID
     */
    private static void scanApp(Context context, String appID) {
        if (Utils.isServiceRunning(MalwareScannerService.class, context)) {
            HashSet<File> filesToScan = new HashSet<>();
            try {
                filesToScan.add(new File(context.getPackageManager().getApplicationInfo(appID, PackageManager.GET_META_DATA).sourceDir));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (filesToScan.size() > 0) {
                new MalwareScanner(null, context, false).executeOnExecutor(Utils.getThreadPoolExecutor(), filesToScan);
            }
        }
    }

}