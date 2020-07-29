package com.example.android.fueltracker;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

public class App extends Application
{
    public static NetworkBroadcastReceiver networkBroadcastReceiver = new NetworkBroadcastReceiver();
    public static boolean isConnected;

    @Override
    public void onCreate()
    {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(networkBroadcastReceiver, intentFilter);
    }
}
