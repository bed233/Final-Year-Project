package com.rhul.fyp.interimsuite.permissionpoc;

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

import com.rhul.fyp.interimsuite.R;

import java.util.ArrayList;

public class PermissionListAdapter extends RecyclerView.Adapter<PermissionListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> arrayList;

    public PermissionListAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.permission_list_result, parent,
                false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.name.setText(arrayList.get(position));
    }


    @Override
    public int getItemCount() {
        if (arrayList.isEmpty()) {
            return 0;
        } else {
            return arrayList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionDialog permissionDialog = new PermissionDialog();
                    permissionDialog.showDialog(context, arrayList.get(getAdapterPosition()));

                }
            });
        }
    }
}

/**
 * Allows each permission to be clicked in the list where a pop up will appear giving user a bit
 * more information regarding the permission.
 * <p>
 * Borrowed from: https://github.com/projectmatris/antimalwareapp/blob/development/app/src/main/java/tech/projectmatris/antimalwareapp/adapters/ScanResultAdapter.java
 * </p>
 */
class PermissionDialog {
    public void showDialog(Context context, String permission) {
        PackageManager packageManager = context.getPackageManager();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        CharSequence title = null;
        CharSequence desc;
        try {
            PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA);
            if (permissionInfo.group != null) {
                title = packageManager.getPermissionGroupInfo(permissionInfo.group, 0).loadLabel(packageManager);
                builder.setIcon(packageManager.getPermissionGroupInfo(permissionInfo.group, 0).loadIcon(packageManager));
            }
            desc = permissionInfo.loadDescription(packageManager);
            if (desc == null) {
                desc = permissionInfo.nonLocalizedDescription;
            }
        } catch (PackageManager.NameNotFoundException e) {
            title = context.getString(R.string.permission_info);
            desc = context.getString(R.string.no_description_found);
        }
        if (title == null) {
            title = context.getString(R.string.permission_info);
        }
        if (desc == null) {
            desc = context.getString(R.string.no_description_found);
        }
        builder.setMessage(desc);
        builder.setTitle(title);
        builder.create().show();

    }
}
