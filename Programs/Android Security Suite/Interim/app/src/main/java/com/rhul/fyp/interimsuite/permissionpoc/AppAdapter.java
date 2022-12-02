package com.rhul.fyp.interimsuite.permissionpoc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhul.fyp.interimsuite.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter {
    Context context;
    private ArrayList<AppParcelInfo> apps;
    private WeakReference<Context> contextWeakReference;

    public AppAdapter(Context context, ArrayList<AppParcelInfo> appsScanned){
        this.context = context;
        apps = appsScanned;
        contextWeakReference = new WeakReference<>(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appIcon;
        TextView appLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appLabel = itemView.findViewById(R.id.appLabel);
        }

        void setAppInfo(final AppParcelInfo appParcelInfo){
            appIcon.setImageDrawable(appParcelInfo.icon);
            appLabel.setText(appParcelInfo.name);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contextWeakReference.get(), AppDetails.class);
                    intent.putExtra("appName", appParcelInfo.name);
                    intent.putExtra("packageName", appParcelInfo.pName);
                    intent.putStringArrayListExtra("permissionList",
                            appParcelInfo.listOfPermissions);
                    contextWeakReference.get().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.permission_app_list_card,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setAppInfo(apps.get(position));

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }
}
