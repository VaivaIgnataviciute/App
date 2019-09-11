package com.example.app;


import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.widget.ImageView;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.io.UnsupportedEncodingException;
import java.util.Timer;

public class PrinterNew {
    String printerName;
    String printerModel;
    NsdServiceInfo printerService;
    String printInformation;
    String menuInformation;
    int state = 0;
    int currentMenu = 0;


    //printerState_t printerState;

//    public enum printerState_t {
//        Idle(-1),
//        Printing(2),
//        Transfering (0),
//        Heating (1),
//        Pausing (3),
//        Paused (4),
//        Cancelling (5),
//        Finished (6);
//
//        public int numVal;
//
//        printerState_t(int numVal) {
//            this.numVal = numVal;
//        }
//
//        public int getNumVal() {
//            return numVal;
//        }
//    }
//
//    public enum menuState {
//        mainMenu(0);
//
//        public int numVal;
//
//        menuState(int numVal) {
//            this.numVal = numVal;
//        }
//
//        public int getNumVal() {
//            return  numVal;
//        }
//    }


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

    public String getPrinterInformation() {
        return this.printInformation;
    }

    public void setPrinterInformation(String url, Context context, final VolleyCallback callback) {
        Log.d("API", "Currently calling URL " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("API", "Print information" + response);
                String temp = response.substring(2, response.length() - 2);
                byte msgArray[];
                try {
                    msgArray = temp.getBytes("ISO-8859-1");
                    state = msgArray[0];
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                printInformation = response;
                callback.onSuccess(printInformation);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "Print information nope ");
                callback.onFailure(error);
            }
        }));
    }

    public String getMenuInformation() {
        return menuInformation;
    }

    public void setMenuInformation(String url, Context context, final VolleyCallback callback) {
        Log.d("API", "Currently calling URL " + url);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                String temp = response.substring(2, response.length() - 2);
                Log.d("API", "Current menu" + response);
                byte msgArray[];
                try {
                    msgArray = temp.getBytes("ISO-8859-1");
                    currentMenu = msgArray[0];
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                menuInformation = response;
                callback.onSuccess(menuInformation);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "Current menu Nope ");
                callback.onFailure(error);
            }
        }));
    }


    public boolean isIdle() {
        if (state == -1 && currentMenu == 0) {
            return true;
        } else {
            return false;
        }
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
        Log.d("API", "Currently calling URL " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("API", "Printer model" + response);
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
                Log.d("API", "printer model nope ");
                callback.onFailure(error);
            }
        }));
    }
}


