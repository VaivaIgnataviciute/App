package com.example.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
        ImageView statusCircle = listItem.findViewById(R.id.status_circle);
        TextView serviceNickName = listItem.findViewById(R.id.TextView_serviceNickName);
        final TextView serviceModel = listItem.findViewById(R.id.TextView_serviceModel);
        TextView printingState = listItem.findViewById(R.id.TextView_PrintingState);
        TextView printPercentage = listItem.findViewById(R.id.TextView_PrintPercentage);
        TextView printFileName = listItem.findViewById(R.id.TextView_PrintFileName);
        TextView printTimeRemaining = listItem.findViewById(R.id.TextView_PrintTimeRemaining);
        TextView errorMessage = listItem.findViewById(R.id.TextView_ErrorMessage);
        ImageView printCircle = listItem.findViewById(R.id.print_circle);
        ImageView clock = listItem.findViewById(R.id.time_clock);
        serviceModel.setText(currentService.getPrinterModel());
        serviceNickName.setText(currentService.getPrinterNickname());


        if (currentService.isIdle()) {

            statusCircle.setColorFilter(Color.rgb(42, 187, 155));
            statusCircle.setVisibility(View.VISIBLE);
            printingState.setText("");
            printPercentage.setText("");
            printFileName.setText("");
            printTimeRemaining.setText("");
            clock.setVisibility(View.GONE);
            clock.setVisibility(View.INVISIBLE);

        } else if (currentService.isHeating()) {

            printingState.setText("Heating up..");
            printingState.setTextColor(Color.rgb(43, 18, 71));
            clock.setVisibility(View.GONE);
            printPercentage.setText("");
            printFileName.setText("");
            printTimeRemaining.setText("");


        } else if (currentService.isPrinting()) {

            printingState.setText("Printing..");
            printingState.setTextColor(Color.rgb(43, 18, 71));
            printPercentage.setText((String.valueOf(currentService.percentageToPrint)));
            printTimeRemaining.setText(currentService.printTime);
            printFileName.setText(currentService.printFileNameToDisplay);
            clock.setVisibility(View.VISIBLE);

        } else if (currentService.isPausing()) {

            printingState.setText("Pausing..");
            printingState.setTextColor(Color.rgb(43, 18, 71));
            printPercentage.setText((String.valueOf(currentService.percentageToPrint)));
            printTimeRemaining.setText(currentService.printTime);
            printFileName.setText(currentService.printFileNameToDisplay);
            clock.setVisibility(View.VISIBLE);


        } else if (currentService.isPaused()) {

            printingState.setText("Paused..");
            printingState.setTextColor(Color.rgb(43, 18, 71));
            printPercentage.setText((String.valueOf(currentService.percentageToPrint)));
            printTimeRemaining.setText(currentService.printTime);
            printFileName.setText(currentService.printFileNameToDisplay);
            clock.setVisibility(View.VISIBLE);

        } else if (currentService.isCancelling()) {

            printingState.setText("Cancelling..");
            printingState.setTextColor(Color.rgb(43, 18, 71));
            printPercentage.setText("");
            printFileName.setText("");
            printTimeRemaining.setText("");

        } else if (currentService.isFinished()) {

            printingState.setText("Finished..");
            printingState.setTextColor(Color.rgb(43, 18, 71));
            printPercentage.setText((String.valueOf(currentService.percentageToPrint)));
            printTimeRemaining.setText(currentService.printTime);
            printFileName.setText(currentService.printFileName);
            clock.setVisibility(View.VISIBLE);

        } else {

            Log.d("nesuveike", "nesuveike haha");
        }


        if (currentService.isIdle()) {

            Log.d("Color", "green-idle");
            errorMessage.setText("");
            printingState.setText("Idle");
            printingState.setTextColor(Color.rgb(42, 187, 155));
            statusCircle.setColorFilter(Color.rgb(42, 187, 155));
            printCircle.setVisibility(View.GONE);
            clock.setVisibility(View.GONE);

        } else if (currentService.isOccupied()) {

            statusCircle.setColorFilter(Color.rgb(244, 208, 63));
            printingState.setTextColor(Color.rgb(244, 208, 63));
            printingState.setText("Occupied");
            printCircle.setVisibility(View.GONE);
            clock.setVisibility(View.GONE);
            Log.d("Color", "yellow-occupied");

        } else if (currentService.isBusyRed()) {

            statusCircle.setColorFilter(Color.rgb(214, 69, 65));
            printingState.setTextColor(Color.rgb(214, 69, 65));
            printingState.setText("Busy");
            printCircle.setVisibility(View.GONE);
            clock.setVisibility(View.GONE);
            Log.d("Color", "red-busy");

        } else if (currentService.isBusyPurple()) {
            printingState.setText("Busy");
            statusCircle.setColorFilter(Color.rgb(213, 184, 255));
            printingState.setTextColor(Color.rgb(213, 184, 255));
            printCircle.setVisibility(View.GONE);
            clock.setVisibility(View.GONE);
            Log.d("Color", "purple-busy(not overtakable)");

        } else if (currentService.isErrorMessage()) {

            errorMessage.setText("Error occured");
            printingState.setText("");
            printCircle.setVisibility(View.GONE);
            clock.setVisibility(View.GONE);
            errorMessage.setTextColor(Color.rgb(214, 69, 65));

        } else if (currentService.isPrintingMenuState()) {
            Log.d("Color", "white-printing");
            printCircle.getDrawable();
            printCircle.setVisibility(View.VISIBLE);
            statusCircle.setVisibility(View.GONE);
            //clock.setVisibility(View.VISIBLE);
        }


        return listItem;
    }

}
