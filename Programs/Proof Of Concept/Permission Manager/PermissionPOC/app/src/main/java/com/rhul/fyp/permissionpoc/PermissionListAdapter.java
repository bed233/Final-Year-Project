package com.rhul.fyp.permissionpoc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PermissionListAdapter extends RecyclerView.Adapter<PermissionListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> arrayList;

    public PermissionListAdapter(Context context, ArrayList<String> arrayList){
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_card_list, parent,
                false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.name.setText(arrayList.get(position));
    }


    @Override
    public int getItemCount() {
        if(arrayList.isEmpty()){
            return 0;
        } else{
            return arrayList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name=itemView.findViewById(R.id.appName);
        }
    }


}
