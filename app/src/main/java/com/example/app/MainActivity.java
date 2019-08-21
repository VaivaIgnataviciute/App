package com.example.app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.BroadcastReceiver;
import android.net.nsd.NsdServiceInfo;

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
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;

import java.net.InetAddress;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String SERVICE_NAME = "3d print";
    private String SERVICE_TYPE = "_cir3dprinter._tcp.";


    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;
    NsdServiceInfo mService;
    private WebView mWebView;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Getting toolbar by id
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //disabling default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mWebView = findViewById(R.id.activity_main_webview);

        //Enable JavasScript for webView

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        if (Build.VERSION.SDK_INT >=19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }



        //NSD stuff

        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD,  mDiscoveryListener);


    }

    @Override
    public void onBackPressed(){
        if (mWebView.canGoBack() && mWebView.isFocused()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
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
            Log.d("TAG", "Port = " + serviceInfo.getPort());

            if (serviceInfo.getServiceType().equals(SERVICE_TYPE)) {
                Log.d("TAG", "Unknown Service Type: " + serviceInfo.getServiceType());
                mNsdManager.resolveService(serviceInfo, mResolveListener);

            }

            else if (serviceInfo.getServiceName().equals(SERVICE_NAME)) {
                Log.d("TAG", "Same machine " + SERVICE_NAME);

            } else {
                Log.d("TAG", "Diff Machine : " + serviceInfo.getServiceName());


            }

        }

        @Override
        public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
            Log.d("TAG", "Service lost " + nsdServiceInfo);

        }
    };

    NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {
        @Override
        public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int errorCode) {
            Log.e("TAG", "Resolved failed " + errorCode);
            Log.e("TAG", "Service = " + nsdServiceInfo);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {

            final String nsdServiceInfoName = nsdServiceInfo.getServiceName();


          final TextView myTextView =  findViewById(R.id.serviceName);


          //Set textView  in separate UI thread to run code outside the main UI thread.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myTextView.setText(nsdServiceInfoName);
                    myTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(MainActivity.this, "im printer",Toast.LENGTH_SHORT).show();
                            mWebView.loadUrl("http://10.0.0.115");

                        }
                    });

                }
            });


            
            Log.d("TAG", "Resolve Succeeded " + nsdServiceInfo);

            if (nsdServiceInfo.getServiceType().equals(SERVICE_TYPE)) {
                Log.d("TAG", "Same IP");
                return;
            }

            hostPort= nsdServiceInfo.getPort();
             String ipAddress = nsdServiceInfo.getHost().getHostAddress();

        }
    };

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


