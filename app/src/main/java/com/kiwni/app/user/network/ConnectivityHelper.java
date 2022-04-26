package com.kiwni.app.user.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.kiwni.app.user.MainActivity;

public class ConnectivityHelper extends BroadcastReceiver
{

    public static boolean isConnected = false;
    public static String status = "";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        status = NetworkUtil.getNetworkState(context);
        Log.d("TAG", "status = " + status);

        if(status == null || status.equals("No Internet"))
        {
            Toast.makeText(context, "No internet. Connect to wifi or cellular network.", Toast.LENGTH_LONG).show();
            isConnected = false;
        }
        else
        {
           Toast.makeText(context, "Internet connection is back now.", Toast.LENGTH_SHORT).show();
            isConnected = true;


        }
    }
}
