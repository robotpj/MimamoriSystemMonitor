package com.example.nakajima.mimamorisystemmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Observer;
import java.util.UUID;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by nakajima on 2017/09/27.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    public ConnectionReceiver(Observer observer) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            // not connection
            onDisconnect(context);
        } else {
            // connection
            onConnect(context);
        }
    }

    void onConnect(Context context) {
        String ipAddress = getIpAddress(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MimamoriSystem", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID", UUID.randomUUID().toString());

        // TODO: send

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UUID", uuid);
        editor.apply();
    }

    void onDisconnect(Context context) {

    }

    public String getIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();

        String ipString = String.format(
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));

        return ipString;
    }
}
