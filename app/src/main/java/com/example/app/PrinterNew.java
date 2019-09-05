package com.example.app;


import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PrinterNew {
    String printerName;
    String printerModel;
    NsdServiceInfo printerService;
    int printerState;


    public PrinterNew() {

    }


    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterModel() {
        return printerModel;
    }

//    public void setPrinterModel(String printerModel) {
//        this.printerModel = printerModel;
//    }

    public int getPrinterState() {
        return this.printerState;
    }

    public void setPrinterState(int printerState) {
        this.printerState = printerState;
    }

    public void setPrinterService(NsdServiceInfo service) {
        this.printerService = service;
    }

    public NsdServiceInfo getPrinterService() {
        return this.printerService;
    }

    public interface VolleyCallback {
        void onSuccess(String result);
        void onFailure(Object response);
    }

    public void setPrinterModel(String url, Context context, final VolleyCallback callback) {
        Log.d("Model", "Currently calling URL " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Model", "Printer mode" + response);
//                String temp = response.substring(2, response.length() - 3);
//                byte array[] = temp.getBytes();
//                Log.d("Model", "Printer mode" + temp);

                //Assigning printer model to the PrinterNew class , printer model attribute.

//                Log.d("Model", "zdrv77 " + myPrinterDetails.printerModel);
                  printerModel = response.substring(2, response.length() - 3);
                  callback.onSuccess(printerModel);
//                PrinterNew myPrinterDetails = new PrinterNew();
//                myPrinterDetails.setPrinterModel(stringResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Model", "Nope ");
                callback.onFailure(error);
            }
        }));
    }
}


