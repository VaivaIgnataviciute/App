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
    int printPercentage1= 0;
    int printPercentage2= 0;
    int printPercentage3 = 0;
    int printRemainingTime1 = 0;
    int printRemainingTime2 = 0;
    int printRemainingTime3 = 0;
    String printTime;
    String printFileNameToDisplay;
    String percentageToPrint;
    float percentage =0;
    int currentMenu = 0;
    int printFileState = 0;




    public PrinterNew() {

    }

    public void setPrinterState(int state) {
        this.state = state;
    }


    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public  String getPrintFileName(){return printFileName;}
    private RequestQueue aRequestQueue;
    private  RequestQueue bRequestQueue;
    private  RequestQueue cRequestQueue;
    private  RequestQueue dRequestQueue;
    private  RequestQueue eRequestQueue;


    public void  setPrintFileName (String url, Context context, final VolleyCallback callback) {

        if (aRequestQueue == null) {
            Log.d("API", "Currently calling URL " + url);
            aRequestQueue = Volley.newRequestQueue(context);
            aRequestQueue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("API", "Printer nickname" + response);


                    String temp = response.substring(2, response.length() - 2);
                    byte msgArray[];

                    msgArray = temp.getBytes();
                    printFileState = msgArray[0];

                    if (printFileState == 1) {
                        printFileName = response.substring(3, response.length() - 2);
                        printFileName = response;
                        printFileNameToDisplay = "(" + printFileName + ")";
                    } else {
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
                        printPercentage1 = msgArray[1];
                        printPercentage2 = msgArray[2];
                        printPercentage3 = msgArray[3];

                        percentage = (printPercentage1 << 16) | (printPercentage2 << 8) | printPercentage3;
                        percentage = percentage / 256.0f;
                        percentageToPrint = String.valueOf(percentage) + "%";

                        printRemainingTime1 = msgArray[4];
                        printRemainingTime2 = msgArray[5];
                        printRemainingTime3 = msgArray[6];

                        printTime = "Remaining time: " + String.valueOf(printRemainingTime1) + ":" + String.valueOf((printRemainingTime2) + ":" + String.valueOf(printRemainingTime3));


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


    //checking menu and printing state if idle

    public boolean isIdle() {
        if (state == -1 && currentMenu == 0) {
            return true;
        } else {
            return false;
        }
    }

    //Menu states

    public boolean isOccupied() {
        if (currentMenu == 3 || currentMenu == 4 || currentMenu == 5 || currentMenu == 9 || currentMenu == 11 || currentMenu == 14 || currentMenu == 15 || currentMenu == 16 || currentMenu == 24 || currentMenu == 25) {
            return true;

        } else {
            return false;
        }
    }

    public boolean isBusyRed() {
        if (currentMenu == 6 || currentMenu == 7 || currentMenu == 8 || currentMenu == 10 || currentMenu == 12 ||currentMenu == 17 || currentMenu == 18 || currentMenu == 27) {
            return true;
        } else  {
            return false;
        }
    }

    public boolean isBusyPurple() {
        if (currentMenu == 13 || currentMenu == 20 || currentMenu == 21 || currentMenu == 22 || currentMenu == 23 || currentMenu == 26 || currentMenu == 29 ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isErrorMessage() {
        if (currentMenu == 19) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPrintingMenuState() {
        if (currentMenu == 1 || currentMenu == 2 ||  currentMenu == 28 || currentMenu == 30) {
            return true;
        } else {
            return false;
        }
    }



    //Printing states

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


    public interface VolleyCallback {
        void onSuccess(String result);

        void onFailure(Object response);
    }



}


