package com.rhul.fyp.permissionpoc;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * AppParcelInfo implements Parcelable which will allow the app to wrap information about each
 * individual app into a 'parcel' that can be shared between activities.
 */
public class AppParcelInfo implements Parcelable {
    public String name; //App Name
    public String pName; //App Package Name
    public String path; //Storage Location
    public Drawable icon = null; // App Icon
    int isSystem; //Whether the app is a system app or not
    public ArrayList<String> listOfPermissions; //List of Permissions required for app as stated by AndroidManifest.xml
    Context context;

    /**
     * Constructor to create instance of AppParcelInfo class that will store information about each
     * individual app.
     *
     * @param in - Parcel that contains the information about the app
     */
    protected AppParcelInfo(Parcel in) {
        name = in.readString();
        pName = in.readString();
        path = in.readString();
        isSystem = in.readInt();
        listOfPermissions = in.readArrayList(String.class.getClassLoader());
    }

    public AppParcelInfo(String name, String pName, String path, int isSystem) {
        this.name = name;
        this.pName = pName;
        this.path = path;
        this.isSystem = isSystem;
    }

    /**
     * Writes Information to a new Parcel that can be passed between activities in the app.
     * @param dest - Parcel that the information is being written too
     * @param flags - Any flags the parcel may have.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(pName);
        dest.writeString(path);
        dest.writeInt(isSystem);
        dest.writeList(listOfPermissions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Loads the App Icon of the particular app using the package manager
     * @param context - Allows the method to access context which is a interface to global
     *                information about the environment the app is working in. In this case what
     *                apps are installed.
     * @return - Return The Icon of a particular app or return null if there is an error.
     */
    public Drawable loadIcon(Context context) {
        try {
            return context.getPackageManager().getPackageArchiveInfo(path, 0).applicationInfo.loadIcon(context.getPackageManager());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static Comparator<AppParcelInfo> appParcelInfoComparator = new Comparator<AppParcelInfo>() {
//        @Override
//        public int compare(AppParcelInfo appParcelInfo, AppParcelInfo t1) {
//            return appParcelInfo.name.toLowerCase().compareTo(t1.name.toLowerCase());
//        }
//    };

    /**
     * Required Interface for when using Parcelable and Parcels that allows instances of my
     * Parcelable class (AppParcelInfo) to be created from Parcel.
     */
    public static final Creator<AppParcelInfo> CREATOR = new Creator<AppParcelInfo>() {
        public AppParcelInfo getInfo(Context context, android.content.pm.ApplicationInfo app){
            return new AppParcelInfo(
                    app.loadLabel(context.getPackageManager()).toString(),
                    app.packageName,
                    app.publicSourceDir,
                    app.flags & ApplicationInfo.FLAG_SYSTEM
            );
        }
        @Override
        public AppParcelInfo createFromParcel(Parcel in) {
            return new AppParcelInfo(in);
        }

        @Override
        public AppParcelInfo[] newArray(int size) {
            return new AppParcelInfo[size];
        }
    };
}
