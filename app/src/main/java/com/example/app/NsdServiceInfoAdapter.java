package com.example.app;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.InetAddress;
import java.util.ArrayList;


//Creating new custom NsdServiceInfoAdapter which extends ArrayAdapter

public class NsdServiceInfoAdapter extends ArrayAdapter<NsdServiceInfo> {
    private Context mContext;
    private ArrayList<NsdServiceInfo> services;
    private InetAddress hostAddress;


    //Creating new constructor with parameters such as this class(context), layout id (list item layout Id) and data model.

    public NsdServiceInfoAdapter(@NonNull Context context, int layoutId, ArrayList<NsdServiceInfo> list) {
        super(context, layoutId, list);
        mContext = context;
        services = list;
    }

    @NonNull
    @Override

    // Creating method get(), which is called when listItem needs to be populated with data.
    //Get a View that displays the data at the specified position in the data set.
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        //Checking if view is empty then we inflate our list  layout.
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        //Getting data's position in the data set.
        NsdServiceInfo currentService = (NsdServiceInfo) services.get(position);

        //We asign the the view to the item layout as a TextView
        TextView t = listItem.findViewById(R.id.TextView_serviceName);
        TextView r = listItem.findViewById(R.id.TextView_serviceIP);
        //hostAddress = currentService.getHost();
        r.setText("IP: "+ String.valueOf(currentService.getHost()) );
        t.setText("name: " + currentService.getServiceName());


        Log.d("tag", "service name" + currentService.getServiceName());
        Log.d("hello2", String.valueOf(currentService.getHost()));


        return listItem;
    }

}
