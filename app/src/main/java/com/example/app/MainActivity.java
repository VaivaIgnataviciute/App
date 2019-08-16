package com.example.app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;

import java.util.List;


public class MainActivity extends AppCompatActivity {








    private WifiManager wifiManager;
    private BroadcastReceiver wifiReciever;
    private ArrayAdapter adapter;
    SupplicantState supState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Getting toolbar by id
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //disabling default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // getting listview from layout
        ListView listView = findViewById(R.id.listView);

        // Creating adapter and array of local AP. Array displays AP's in listview adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        //Using wifi manager API I display all information about succesfull connetion to local network
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiReciever = new WiFiScanReceiver();


        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        supState = wifiInfo.getSupplicantState();




    }

     //Creating toogle button which turns on and turns of wifi
    public void onToggleClicked(View view) {

        adapter.clear();
        ToggleButton button = findViewById(R.id.toggleButton);

        //if the device can't connect to wifi, it displays toast with message informing user about that.
        if (wifiManager == null) {
            //device does not support wifi
            Toast.makeText(getApplicationContext(), "Oop! Your device does not support Wi-Fi",
                    Toast.LENGTH_SHORT).show();
            button.setChecked(false);
        } else {// otherwise if button is checked,connection was succesful , the wifi scans the local networks and checks for AP's
            if (button.isChecked()) { // to turn on wifi
                if (!wifiManager.isWifiEnabled()) {
                    Toast.makeText(getApplicationContext(), "Wi-Fi is turned on." +
                                    "\n" + "Scanning for access points...",
                            Toast.LENGTH_SHORT).show();
                    wifiManager.setWifiEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Wi-Fi is already turned on." +
                                    "\n" + "Scanning for access points...",
                            Toast.LENGTH_SHORT).show();
                }

                wifiManager.startScan();

            } else { //last case wifi is turn off and no services is available.
                Toast.makeText(getApplicationContext(), "Wi-Fi is turned off.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    //Using wifi scan receiver we can get the results and iterate them.
    class WiFiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action) ) {
                List<ScanResult> wifiScanResultList = wifiManager.getScanResults();
                String listItem ;
                for (int i = 0; i < wifiScanResultList.size(); i++ ) {


                    try {
                        if (i == Integer.parseInt("Thorsens, 70:8b:cd:89:19:e8")) {
                            break;
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Number format exception occurred");
                    }

                    // once the results are iterated we display their IP addresses and place them in listview.
                    ScanResult accessPoint = wifiScanResultList.get(i);
                    listItem = accessPoint.SSID + ", " + accessPoint.BSSID;
                    adapter.add(listItem);

                    }

            }
        }
    }


    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReciever, filter);
    }



    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiReciever);
    }

}

