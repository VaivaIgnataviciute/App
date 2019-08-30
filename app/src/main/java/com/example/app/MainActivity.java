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

import java.net.InetAddress;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String SERVICE_TYPE = "_cir3dprinter._tcp."; // change to normal
//    private String SERVICE_TYPE = "_googlecast._tcp.";


    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;
    ArrayList<NsdServiceInfo> services;
    private NsdServiceInfoAdapter mAdapter;


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
                NsdServiceInfo selectedService = (NsdServiceInfo) serviceObj;
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
                Log.d("TAG", "Resolve Succeeded " + nsdServiceInfo);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        services.add(nsdServiceInfo);
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
        public void onServiceFound(final NsdServiceInfo serviceInfo) {
            Log.d("TAG", "Service discovery success : " + serviceInfo);
            Log.d("TAG", "Host = " + serviceInfo.getServiceName());
            Log.d("TAG", "Port = " + serviceInfo.getPort());
            if (!services.contains(serviceInfo)){
                if (serviceInfo.getServiceType().equals(SERVICE_TYPE)) {
                        mNsdManager.resolveService(serviceInfo, initializeResolveListener());
                }
            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
            Log.d("TAG", "Service lost " + nsdServiceInfo);
            NsdServiceInfo serviceToRemove = new NsdServiceInfo();
            for (NsdServiceInfo currentService : services) {
                if (currentService.getHost() == nsdServiceInfo.getHost() && currentService.getPort() == currentService.getPort() && currentService.getServiceName() == currentService.getServiceName()) {
                    serviceToRemove = currentService;
                }
            }
            final NsdServiceInfo finalServiceToRemove = serviceToRemove;
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


