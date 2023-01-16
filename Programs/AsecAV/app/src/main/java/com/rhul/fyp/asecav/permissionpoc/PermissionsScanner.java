package com.rhul.fyp.asecav.permissionpoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PermissionsScanner extends AsyncTask<Void, String, Void> {
    private final WeakReference<Context> contextRef;
    private final WeakReference<Activity> activityRef;

    private final ArrayList<AppParcelInfo> scannedApps = new ArrayList<>();

    public PermissionsScanner(Context context, Activity activity) {
        contextRef = new WeakReference<Context>(context);
        this.activityRef = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final PackageManager packageManager = contextRef.get().getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(0);
        for (ApplicationInfo info : packages) {
            String currentAppName = info.loadLabel(packageManager).toString();
            publishProgress(currentAppName);
            AppParcelInfo app = new AppParcelInfo(info.loadLabel(packageManager).toString(),
                    info.packageName, info.publicSourceDir,
                    info.flags & ApplicationInfo.FLAG_SYSTEM);
            app.icon = info.loadIcon(packageManager);
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            } else {
                try{
                    ArrayList<String> appPermissionsList =
                            getListOfPermissions(contextRef.get().createPackageContext(info.packageName, 0));
                    app.listOfPermissions = appPermissionsList;
                    scannedApps.add(app);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(isCancelled()){
                break;
            }
        }


        return null;
    }

    /**
     * Get the list of permissions used by the application
     * <p>
     * Borrowed from: https://stackoverflow.com/questions/18236801 (Yousha Aleayoub)
     */
    private static ArrayList<String> getListOfPermissions(final Context context) {
        ArrayList<String> arr = new ArrayList<>();

        try {
            final AssetManager am = context.createPackageContext(context.getPackageName(), 0).getAssets();
            final Method addAssetPath = am.getClass().getMethod("addAssetPath", String.class);
            final int cookie = (Integer) addAssetPath.invoke(am, context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir);
            final XmlResourceParser xmlParser = am.openXmlResourceParser(cookie, "AndroidManifest.xml");
            int eventType = xmlParser.next();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG) && "uses-permission".equals(xmlParser.getName())) {
                    for (byte i = 0; i < xmlParser.getAttributeCount(); i++) {
                        if (xmlParser.getAttributeName(i).equals("name")) {
                            arr.add(xmlParser.getAttributeValue(i));
                        }
                    }
                }
                eventType = xmlParser.next();
            }
            xmlParser.close();
        } catch (final XmlPullParserException exception) {
            exception.printStackTrace();
        } catch (final PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        } catch (final IOException exception) {
            exception.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return arr;

    }

    @Override
    protected void onPostExecute(Void unused) {
        putDateInSharedPreference();
        ResultActivity.apps = scannedApps;
        Intent resultScreen = new Intent(contextRef.get(), ResultActivity.class);

//        this.activityRef.get().finish();
        contextRef.get().startActivity(resultScreen);
    }


    private void putDateInSharedPreference() {
        String curDateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contextRef.get());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastScan", curDateTime);
        editor.apply();
    }
}
