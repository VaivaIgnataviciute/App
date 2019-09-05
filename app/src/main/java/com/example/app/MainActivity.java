package com.example.app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;


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
    public String stringResponse;


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

    public String getPrinterName(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);


        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Model", "Printer mode" + response);
//                String temp = response.substring(2, response.length() - 3);
//                byte array[] = temp.getBytes();
//                Log.d("Model", "Printer mode" + temp);

                //Assigning printer model to the PrinterNew class , printer model attribute.



//                Log.d("Model", "zdrv77 " + myPrinterDetails.printerModel);
                stringResponse = response.substring(2, response.length() - 3);
                PrinterNew myPrinterDetails = new PrinterNew();
                 myPrinterDetails.setPrinterModel(stringResponse);





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Model", "Nope ");
            }
        }));
        return stringResponse;

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
            public void onServiceResolved( NsdServiceInfo nsdServiceInfo) {
                final PrinterNew myPrinterDetails = new PrinterNew();
                myPrinterDetails.setPrinterName(nsdServiceInfo.getServiceName());


               // myPrinterDetails.setPrinterModel(nsdServiceInfo.());
                getPrinterName("http://10.0.0.115/SettingGetPrinterName");
                myPrinterDetails.getPrinterModel();
                Log.d("TAG", "Printer mode on resolved " + getPrinterName("http://10.0.0.115/SettingGetPrinterName"));
                Log.d("TAG", "labas " + myPrinterDetails.getPrinterModel());




                Log.d("TAG", "Resolve Succeeded " + nsdServiceInfo);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        services.add(myPrinterDetails);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }


        };
    }

    NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {
        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.e("TAG", "DiscoveryFailed: Error code: " + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.e("TAG", "Discovery failed : Error code: " + errorCode);
        }

        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d("TAG", "Service discovery started");

        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.i("TAG", "Discovery stopped: " + serviceType);

        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            boolean found = false;
            Log.d("TAG", "Service discovery success : " + serviceInfo);
            Log.d("TAG", "Host = " + serviceInfo.getServiceName());
            Log.d("TAG", "Port = " + serviceInfo.getPort());
//            if (!services.contains(serviceInfo)) {
//                if (serviceInfo.getServiceType().equals(SERVICE_TYPE)) {
//                    mNsdManager.resolveService(serviceInfo, initializeResolveListener());
//                }
//            }
            for(PrinterNew printer : services) {
                if(printer.getPrinterService() == serviceInfo) {
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
            Log.d("TAG", "Service lost " + nsdServiceInfo);
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
            Log.d("TAG", "Xd" + services);
        }

    };



    /*

    NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {


        @Override
        public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int errorCode) {
            Log.e("TAG", "Resolved failed " + errorCode);
            Log.e("TAG", "Service = " + nsdServiceInfo);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {


            Log.d("TAG", "bbz" + nsdServiceInfo);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                    startActivity(intent);
               }
            });



            Log.d("TAG", "Resolve Succeeded " + nsdServiceInfo);

            if (nsdServiceInfo.getServiceType().equals(SERVICE_TYPE)) {
                Log.d("TAG", "Same IP");
                return;
            }



            hostPort = nsdServiceInfo.getPort();
            hostAddress = nsdServiceInfo.getHost();


        }
    };



    // NsdHelper's tearDown method
    public void tearDown() {

        mNsdManager.stopServiceDiscovery(mDiscoveryListener);

    }
    */


}


