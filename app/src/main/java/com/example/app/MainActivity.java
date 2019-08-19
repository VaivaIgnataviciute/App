package com.example.app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.nfc.Tag;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;

import java.net.InetAddress;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String SERVICE_NAME = "3D printer";
    private String SERVICE_TYPE = "_cir3dprinter._tcp";

    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;
    NsdServiceInfo mService;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Getting toolbar by id
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //disabling default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);




        //NSD stuff

        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD,  mDiscoveryListener);


    }
    /*

    @Override

    protected void  onPause() {
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
        super.onPause();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if( mNsdManager != null) {
            mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        }
    }


    @Override
    protected void onDestroy() {
        if ( mNsdManager != null) {
          mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
        super.onDestroy();
    }
    */
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

            Log.d("TAG", "Service discovery success : " + serviceInfo);
            Log.d("TAG", "Host = " + serviceInfo.getServiceName());
            Log.d("TAG", "Port = " + String.valueOf(serviceInfo.getPort()));

            if (!serviceInfo.getServiceType().equals(SERVICE_TYPE)) {
                Log.d("TAG", "Unknown Service Type: " + serviceInfo.getServiceType());
            }

            else if (serviceInfo.getServiceName().equals(SERVICE_NAME)) {
                Log.d("TAG", "Same machine " + SERVICE_NAME);
            } else {
                Log.d("TAG", "Diff Machine : " + serviceInfo.getServiceName());
                mNsdManager.resolveService(serviceInfo, new MyResolveListener());
            }

        }

        @Override
        public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
            Log.d("TAG", "Service lost " + nsdServiceInfo);

        }
    };

    //NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener()
    private class MyResolveListener implements NsdManager.ResolveListener {
        @Override
        public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int errorCode) {
            Log.e("TAG", "Resolved failed " + errorCode);
            Log.e("TAG", "Service = " + nsdServiceInfo);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {

            Log.d("TAG", "Resolve Succeeded " + nsdServiceInfo);

            if (nsdServiceInfo.getServiceType().equals(SERVICE_TYPE)) {
                Log.d("TAG", "Same IP");
                return;
            }

            hostPort = nsdServiceInfo.getPort();
            hostAddress = nsdServiceInfo.getHost();

        }
    }

/*
    public void discoverServices() {
        if (mDiscoveryListener != null) {
            try {
                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            } finally {

            }
            mDiscoveryListener = null;
        }
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }
    */


}


