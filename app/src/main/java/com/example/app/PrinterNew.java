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
    String printerNickname;
    String printerModel;
    String printFileName;
    NsdServiceInfo printerService;
    String printInformation;
    String menuInformation;
    int state = 0;
    int currentMenu = 0;
    int printFileState = 0;




    public PrinterNew() {

    }

    public void setPrinterState(int state) {
        this.state = state;
    }

    public void setPrinterMenuInformation(int state) {
        this.currentMenu = state;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public  String getPrintFileName(){return printFileName;}

    public void  setPrintFileName (String url, Context context, final VolleyCallback callback) {
        Log.d("API", "Currently calling URL " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("API", "Printer nickname" + response);


                String temp = response.substring(2, response.length() -2);
                byte msgArray[];

                    msgArray = temp.getBytes();
                    printFileState = msgArray[0];

                    if (printFileState == 1) {
                        printFileName = response.substring(3,response.length() - 2);
                        printFileName = response;
                    } else
                    {
                        Log.d("API", "Printer file is not available");
                    }

                callback.onSuccess(printFileName);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "printer nickname  nope ");
                callback.onFailure(error);
            }
        }));
    }



    public String getPrinterNickname() {return printerNickname;}

    public void  setPrinterNickname (String url, Context context, final VolleyCallback callback) {
        Log.d("API", "Currently calling URL " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("API", "Printer nickname" + response);

                printerNickname = response.substring(2, response.length() - 3);
                callback.onSuccess(printerNickname);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "printer nickname  nope ");
                callback.onFailure(error);
            }
        }));
    }


    public String getPrinterModel() {
        return printerModel;
    }

    public void setPrinterModel(String url, Context context, final VolleyCallback callback) {
        Log.d("API", "Currently calling URL " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("API", "Printer model" + response);


                printerModel = response.substring(2, response.length() - 3);
                callback.onSuccess(printerModel);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "printer model nope ");
                callback.onFailure(error);
            }
        }));
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

               /* if(currentMenu == 10) {
                    Log.d("API", "Someone is doing bed leveling  ");
                }*/




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

    public void setPrinterService(NsdServiceInfo service) {
        this.printerService = service;
    }

    public NsdServiceInfo getPrinterService() {
        return this.printerService;
    }


    public boolean isIdle() {
        if (state == -1 && currentMenu == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHeating() {
        if (state == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPrinting() {
        if (state == 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTransfering() {
        if (state == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPausing() {
        if (state == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPaused() {
        if (state == 4) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCancelling() {
        if (state == 5) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isFinished() {
        if (state == 6) {
            return true;
        } else {
            return false;
        }
    }


    //Menu methods






    public interface VolleyCallback {
        void onSuccess(String result);

        void onFailure(Object response);
    }



}


