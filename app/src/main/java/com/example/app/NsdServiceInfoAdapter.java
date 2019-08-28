package com.example.app;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NsdServiceInfoAdapter extends ArrayAdapter<NsdServiceInfo> {
    private Context mContext;
    private ArrayList<NsdServiceInfo> services;

    public NsdServiceInfoAdapter(@NonNull Context context, int layoutId, ArrayList<NsdServiceInfo> list) {
        super(context, layoutId, list);
        mContext = context;
        services = list;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

       /* View view = super.getView(position,convertView,parent);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 40;
        view.setLayoutParams(params);*/

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

            NsdServiceInfo currentService = services.get(position);

            TextView t = listItem.findViewById(R.id.TextView_serviceName);
            t.setText(currentService.getServiceName());




        }
        return listItem;
    }

}
