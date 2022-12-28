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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;

import com.rhul.fyp.asecav.BuildConfig;
import com.rhul.fyp.asecav.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.GZIPInputStream;

class Database {

    private static TextView log = null;
    private static SharedPreferences prefs = null;
    private static File databasePath = null;
    private static boolean databaseFullyLoaded = false;
    private static boolean databaseCurrentlyLoading = false;

    public final static ConcurrentLinkedQueue<SignatureDatabase> signatureDatabases = new ConcurrentLinkedQueue<>();
    public final static HashMap<String, String> signaturesMD5 = new HashMap<>();
    public final static HashMap<String, String> signaturesSHA1 = new HashMap<>();
    public final static HashMap<String, String> signaturesSHA256 = new HashMap<>();

    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    public Database(TextView log) {
        Database.log = log;
    }

    /**
     * Checks whether the databases are available on the device
     * @return
     */
    public static boolean areDatabasesAvailable() {
        return databasePath != null && databasePath.listFiles().length > 0 && signatureDatabases.size() > 0;
    }

    /**
     * Checks whether the databases are loaded into the app
     * @return
     */
    public static boolean isDatabaseLoaded() {
        return areDatabasesAvailable() && databaseFullyLoaded && (signaturesMD5.size() > 0 || signaturesSHA1.size() > 0 || signaturesSHA256.size() > 0);
    }

    /**
     * Retrieves number of Signatures are stored in the databases.
     * @return
     */
    public static int getSignatureCount() {
        return signaturesMD5.size() + signaturesSHA1.size() + signaturesSHA256.size();
    }

    /**
     * Checks whether any database needs an update then updates when user says so.
     * @param context
     * @param signatureDatabases
     */
    public static void updateDatabase(Context context, ConcurrentLinkedQueue<SignatureDatabase> signatureDatabases) {
        initDatabase(context);
        log.append(context.getString(R.string.main_database_updating, signatureDatabases.size() + "") + "\n");
        if(!Utils.getDatabaseURL(context).equals(Utils.DATABASE_URL_DEFAULT)) {
            log.append(context.getString(R.string.main_database_override, Utils.getDatabaseURL(context)) + "\n");
        }
        for (SignatureDatabase signatureDatabase : signatureDatabases) {
            boolean onionRouting = prefs.getBoolean("ONION_ROUTING", false);
            new Downloader().executeOnExecutor(Utils.getThreadPoolExecutor(), onionRouting, signatureDatabase.getUrl(), databasePath + "/" + signatureDatabase.getName(), signatureDatabase.getBaseUrl());
        }
    }

    private static void initDatabase(Context context) {
        databasePath = new File(context.getFilesDir() + "/signatures/");
        databasePath.mkdir();

        signatureDatabases.clear();

        prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String baseURL = Utils.getDatabaseURL(context);
        if (prefs.getBoolean("SIGNATURES_TARGETEDTHREATS", true)) {
            signatureDatabases.add(new SignatureDatabase(baseURL, "targetedthreats.hdb.gz"));
            signatureDatabases.add(new SignatureDatabase(baseURL, "targetedthreats.hsb.gz"));
        }
        if (prefs.getBoolean("SIGNATURES_ESET", true)) {
            signatureDatabases.add(new SignatureDatabase(baseURL, "eset.hdb.gz"));
            signatureDatabases.add(new SignatureDatabase(baseURL, "eset.hsb.gz"));
        }
        if (prefs.getBoolean("SIGNATURES_CLAMAV-MAIN", false)) {
            signatureDatabases.add(new SignatureDatabase(baseURL, "main.hdb.gz"));
            signatureDatabases.add(new SignatureDatabase(baseURL, "main.hsb.gz"));
        }
        if (prefs.getBoolean("SIGNATURES_CLAMAV-DAILY", false)) {
            signatureDatabases.add(new SignatureDatabase(baseURL, "daily.hdb.gz"));
            signatureDatabases.add(new SignatureDatabase(baseURL, "daily.hsb.gz"));
        }
        if (prefs.getBoolean("SIGNATURES_CLAMAV-ANDROID", true)) {
            signatureDatabases.add(new SignatureDatabase(baseURL, "Android.hdb.gz"));
            signatureDatabases.add(new SignatureDatabase(baseURL, "Android.hsb.gz"));
        }
    }

    /**
     * Loads already downloaded databases into the app in order to be used.
     * @param context
     * @param forceReload
     * @param signatureDatabases
     */
    public static void loadDatabase(Context context, boolean forceReload, ConcurrentLinkedQueue<SignatureDatabase> signatureDatabases) {
        if ((!isDatabaseLoaded() || forceReload) && !databaseCurrentlyLoading) {
            databaseFullyLoaded = false;
            databaseCurrentlyLoading = true;
            initDatabase(context);
            signaturesMD5.clear();
            signaturesSHA1.clear();
            signaturesSHA256.clear();
            System.gc();
            for (SignatureDatabase database : signatureDatabases) {
                File databaseLocation = new File(databasePath + "/" + database.getName());
                if (databaseLocation.exists()) {
                    try {
                        BufferedReader reader;
                        if (databaseLocation.getName().endsWith(".gz")) {
                            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(databaseLocation))));
                        } else {
                            reader = new BufferedReader(new FileReader(databaseLocation));
                        }
                        String line;
                        if (database.getName().contains(".hdb")) {//.hdb format: md5, size, name
                            while ((line = reader.readLine()) != null) {
                                if (line.length() > 0) {
                                    String[] lineS = line.split(":");
                                    if (lineS[0].length() > 0) {
                                        signaturesMD5.put(lineS[0].substring(0, Utils.MAX_HASH_LENGTH), lineS[2]);
                                    }
                                }
                            }
                        } else if (database.getName().contains(".hsb")) {//.hsb format: sha256, size, name
                            while ((line = reader.readLine()) != null) {
                                if (line.length() > 0) {
                                    String[] lineS = line.split(":");
                                    if (lineS[0].length() == 32) {
                                        signaturesSHA1.put(lineS[0].substring(0, Utils.MAX_HASH_LENGTH), lineS[2]);
                                    } else if (lineS[0].length() > 0) {
                                        signaturesSHA256.put(lineS[0].substring(0, Utils.MAX_HASH_LENGTH), lineS[2]);
                                    }
                                }
                            }
                        }
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            signaturesMD5.put("44d88612fea8a8f36de82e1278abb02f".substring(0, Utils.MAX_HASH_LENGTH), "Eicar-Test-Signature");
            signaturesSHA256.put("6a0b4866f143c32e651662cebf7f380d27b0db809db3b6a34cf34c7436ab6bbf".substring(0, Utils.MAX_HASH_LENGTH), "Hypatia-Test-Signature");
            System.gc();
            databaseFullyLoaded = true;
            databaseCurrentlyLoading = false;
        }
    }

    /**
     * Downloads any missing Databases or databases that the user has requested to be downloaded.
     */
    public static class Downloader extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... objects) {
            boolean onionRouting = (boolean) objects[0];
            String url = (String) objects[1];
            File out = new File((String) objects[2]);
            String baseURL = (String) objects[3];
            try {
                HttpURLConnection connection;
                if (onionRouting) {
                    Utils.waitUntilOrbotIsAvailable();
                    Proxy orbot = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9050));
                    connection = (HttpURLConnection) new URL(url).openConnection(orbot);
                } else {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                }
                connection.setConnectTimeout(90000);
                connection.setReadTimeout(30000);
                connection.addRequestProperty("User-Agent", "Hypatia");
                String lastModifiedLocal = "";
                if (out.exists()) {
                    connection.setIfModifiedSince(out.lastModified());
                    lastModifiedLocal = Utils.getContext().getString(R.string.main_database_not_modified_since, dateFormat.format(new Date(out.lastModified())));
                }
                connection.connect();
                String lastModifiedServer = dateFormat.format(new Date(connection.getLastModified()));
                int res = connection.getResponseCode();
                if (res != 304) {
                    if (res == 200) {
                        if (out.exists()) {
                            out.delete();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(out);

                        final byte[] data = new byte[1024];
                        int count;
                        while ((count = connection.getInputStream().read(data, 0, 1024)) != -1) {
                            fileOutputStream.write(data, 0, count);
                        }

                        fileOutputStream.close();
                        publishProgress(url.replaceAll(baseURL, "")
                                + "\n\t" + Utils.getContext().getString(R.string.main_database_download_success)
                                + "\n\t" + Utils.getContext().getString(R.string.main_database_released_on, lastModifiedServer) + "\n");
                    } else {
                        publishProgress(url.replaceAll(baseURL, "")
                                + "\n\t" + Utils.getContext().getString(R.string.main_database_download_error, res + "") + "\n");
                    }
                } else {
                    publishProgress(url.replaceAll(baseURL, "")
                            + "\n\t" + Utils.getContext().getString(R.string.main_database_not_changed) + " " + lastModifiedLocal + "\n");
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                out.delete();
                publishProgress(url.replaceAll(baseURL, "")
                        + "\n" + Utils.getContext().getString(R.string.main_database_download_error_logcat) + "\n");
            }
            return null;
        }

        @Override
        protected final void onProgressUpdate(String... progress) {
            log.append(progress[0] + "\n");
        }
    }
}
