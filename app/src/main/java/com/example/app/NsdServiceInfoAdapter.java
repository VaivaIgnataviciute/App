package com.example.app;

import android.content.Context;
import android.graphics.Color;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.InetAddress;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;


//Creating new custom NsdServiceInfoAdapter which extends ArrayAdapter

public class NsdServiceInfoAdapter extends ArrayAdapter<PrinterNew> {
    private Context mContext;
    private List<PrinterNew> services;
    private InetAddress hostAddress;


    //Creating new constructor with parameters such as this class(context), layout id (list item layout Id) and data model.

    public NsdServiceInfoAdapter(@NonNull Context context, int layoutId, List<PrinterNew> list) {
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
         PrinterNew currentService = services.get(position);


        //We asign the the view to the item layout as a TextView
        ImageView i = listItem.findViewById(R.id.status_circle);
        TextView t = listItem.findViewById(R.id.TextView_serviceName);
        final TextView r = listItem.findViewById(R.id.TextView_serviceIP);
        //hostAddress = currentService.getHost();
        r.setText(currentService.getPrinterModel());
        t.setText(currentService.getPrinterName());


            if (currentService.isIdle()) {
                i.setColorFilter(Color.rgb(42, 187, 155));
            } else {
                i.setColorFilter(Color.rgb(240, 52, 52));
            }


//
//
//
//new Timer().schedule(new TimerTask() {
//    @Override
//    public void run() {
//        if (currentService.isIdle()) {
//            i.setColorFilter(Color.rgb(42, 187, 155));
//        } else {
//            i.setColorFilter(Color.rgb(240, 52, 52));
//        }
//
//
//    }
//},0,2000);


        Log.d("bybis", "model" + String.valueOf(currentService.getPrinterModel()));
        Log.d("hello2", String.valueOf(currentService.getPrinterName()));


        return listItem;
    }

}
