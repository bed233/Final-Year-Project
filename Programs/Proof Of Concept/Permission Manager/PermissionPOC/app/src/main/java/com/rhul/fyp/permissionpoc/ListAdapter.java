package com.rhul.fyp.permissionpoc;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {
    Context context;
    private final List<ApplicationInfo> apps;
    private final PackageManager packageManager;

    public ListAdapter(Context context, List<ApplicationInfo> listOfApps) {
        packageManager = context.getPackageManager();
        apps = listOfApps;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView pName;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.appIcon);
            name = itemView.findViewById(R.id.appLabel);
            pName = itemView.findViewById(R.id.appSecondLabel);
        }

        void setAppInfo(final ApplicationInfo appInfo) {
            name.setText(appInfo.loadLabel(packageManager));
            icon.setImageDrawable(appInfo.loadIcon(packageManager));
            pName.setText(appInfo.packageName);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_card_list,
                parent, false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListAdapter.ViewHolder view = (ListAdapter.ViewHolder) holder;
        view.setAppInfo(apps.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
