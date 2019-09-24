package com.example.app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.net.nsd.NsdServiceInfo;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import java.util.TimerTask;
import java.util.Timer;


import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String SERVICE_TYPE = "_cir3dprinter._tcp."; // change to normal
//    private String SERVICE_TYPE = "_googlecast._tcp.";


    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;
    ArrayList<PrinterNew> services;
    private NsdServiceInfoAdapter mAdapter;
    private Timer myTimer = new Timer();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting toolbar by id
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        services = new ArrayList<>();
        mAdapter = new NsdServiceInfoAdapter(this, R.id.TextView_serviceName, services);
        ListView listView = findViewById(R.id.ListViewServices);
        listView.setAdapter(mAdapter); // we add custom adapter to the listview to display data from adapter.


        //disabling default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //NSD stuff

        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object serviceObj = adapterView.getItemAtPosition(i);
                //NsdServiceInfo selectedService = (NsdServiceInfo) serviceObj;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        startActivity(intent);
                    }
                });

                //mNsdManager.stopServiceDiscovery(mDiscoveryListener);
                //mNsdManager.resolveService(selectedService, mResolveListener);

            }
        });
    }


    public NsdManager.ResolveListener initializeResolveListener() {
        return new NsdManager.ResolveListener() {

            @Override


            public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int errorCode) {
                Log.e("TAG", "Resolved failed " + errorCode);
                Log.e("TAG", "Service = " + nsdServiceInfo);
                mNsdManager.resolveService(nsdServiceInfo, initializeResolveListener());
            }


            @Override
            public void onServiceResolved(final NsdServiceInfo nsdServiceInfo) {
                final PrinterNew myPrinterDetails = new PrinterNew();
                myPrinterDetails.setPrinterName(nsdServiceInfo.getServiceName());
                Log.d("NSD", "Hostas" + nsdServiceInfo.getHost());
                myPrinterDetails.setPrinterModel("http://" + nsdServiceInfo.getHost() + "/SettingGetPrinterName", MainActivity.this, new PrinterNew.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("NSD", "Print model Resolve Succeeded" + nsdServiceInfo);
                                services.add(myPrinterDetails);

                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.d("NSD", "Print model response failed");
                        return;
                    }
                });

                myPrinterDetails.setPrinterInformation("http://" + nsdServiceInfo.getHost() + "/PrintGetInformation", MainActivity.this, new PrinterNew.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("NSD", "Print information response succeded and added to array " + nsdServiceInfo);
                                //services.add(myPrinterDetails);
                                mAdapter.notifyDataSetChanged();

                            }
                        });


                        myTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myPrinterDetails.setMenuInformation("http://" + nsdServiceInfo.getHost() + "/PrintGetInformation", MainActivity.this, new PrinterNew.VolleyCallback() {
                                            @Override
                                            public void onSuccess(String result) {

                                                Log.d("NSD", "Current print   response added to timer " + nsdServiceInfo);
                                                mAdapter.notifyDataSetChanged();
                                            }


                                            @Override
                                            public void onFailure(Object response) {
                                                Log.d("NSD", "Current print timer  failed");
                                            }
                                        });
                                    }
                                });
                            }
                        }, 0, 2000);
//

                        Log.d("API", "Print information " + myPrinterDetails.getPrinterInformation());
                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.d("NSD", "Print status response failed");
                        return;

                    }
                });

                myPrinterDetails.setMenuInformation("http://" + nsdServiceInfo.getHost() + "/GetCurrentMenu", MainActivity.this, new PrinterNew.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("NSD", "Current menu response succeded and added to array " + nsdServiceInfo);
                                //services.add(myPrinterDetails);
                               mAdapter.notifyDataSetChanged();

                            }
                        });
                        Log.d("NSD", "CurrentMenu " + myPrinterDetails.getMenuInformation());

                        myTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myPrinterDetails.setMenuInformation("http://" + nsdServiceInfo.getHost() + "/GetCurrentMenu", MainActivity.this, new PrinterNew.VolleyCallback() {
                                            @Override
                                            public void onSuccess(String result) {

                                                Log.d("NSD", "Current menu  response added to timer  " + nsdServiceInfo);
                                                mAdapter.notifyDataSetChanged();
                                            }


                                            @Override
                                            public void onFailure(Object response) {
                                                Log.d("NSD", "Current menu timer  failed");
                                            }
                                        });
                                    }
                                });
                            }
                        }, 0, 2000);

                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.d("NSD", "Current menu response failed");
                    }
                });

            }
        };
    }

    NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {
        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.e("NSD", "DiscoveryFailed: Error code: " + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.e("NSD", "Discovery failed : Error code: " + errorCode);
        }

        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d("NSD", "Service discovery started");

        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.i("NSD", "Discovery stopped: " + serviceType);

        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            boolean found = false;
            Log.d("NSD", "Service discovery success : " + serviceInfo);
            Log.d("NSD", "Host = " + serviceInfo.getServiceName());
            Log.d("NSD", "Port = " + serviceInfo.getPort());
//            if (!services.contains(serviceInfo)) {
//                if (serviceInfo.getServiceType().equals(SERVICE_TYPE)) {
//                    mNsdManager.resolveService(serviceInfo, initializeResolveListener());
//                }
//            }


            for (PrinterNew printer : services) {
                if (printer.getPrinterService() == serviceInfo) {
                    found = true;
                }
            }

            if (!found) {
                if (serviceInfo.getServiceType().equals(SERVICE_TYPE)) {
                    mNsdManager.resolveService(serviceInfo, initializeResolveListener());




                }
            }




        }

        @Override
        public void onServiceLost(NsdServiceInfo nsdServiceInfo) {

            PrinterNew serviceToRemove = new PrinterNew();
            for (PrinterNew currentService : services) {
                if (currentService.getPrinterName() == currentService.getPrinterName() && currentService.getPrinterModel() == currentService.getPrinterModel()) {
                    serviceToRemove = currentService;
                }
            }
            final PrinterNew finalServiceToRemove = serviceToRemove;
            if (serviceToRemove != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        services.remove(finalServiceToRemove);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }



            Log.d("NSD", "Service lost " + nsdServiceInfo);
//            Log.d("NSD", "Xd" + services);
        }

    };


}


