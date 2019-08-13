package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private BroadcastReceiver wifiReciever;
    private ArrayAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id .listView);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiReciever = new WiFiScanReceiver();

        

    }


    public void onToggleClicked(View view) {

        adapter.clear();
        ToggleButton button =  findViewById(R.id.toggleButton);

        if (wifiManager == null) {
            //device does not support wifi
            Toast.makeText(getApplicationContext(), "Oop! Your device does not support Wi-Fi",
                    Toast.LENGTH_SHORT).show();
            button.setChecked(false);
        } else {
            if (button.isChecked()){ // to turn on wifi
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

                } else {
                Toast.makeText(getApplicationContext(), "Wi-Fi is turned off.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    class WiFiScanReceiver extends BroadcastReceiver {
        public void  onReceive (Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                List<ScanResult> wifiScanResultList = wifiManager.getScanResults();
                for ( int i = 0; i < wifiScanResultList.size(); i++) {
                    ScanResult accessPoint = wifiScanResultList.get(i);
                    String listItem = accessPoint.SSID +", "+accessPoint.BSSID;
                    adapter.add(listItem);
                }
            }
        }
    }


    protected void onResume () {
        super.onResume();
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReciever,filter);
    }


    protected void OnPause() {
        super.onPause();
        unregisterReceiver(wifiReciever);
    }
}
