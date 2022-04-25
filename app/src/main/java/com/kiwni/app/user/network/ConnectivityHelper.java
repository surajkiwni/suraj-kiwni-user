package com.kiwni.app.user.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ConnectivityHelper extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String status = NetworkUtil.getNetworkState(context);
        Log.d("TAG", "status = " + status);

        if(status == null || status.equals("No Internet"))
        {
            Toast.makeText(context, "No internet. Connect to wifi or cellular network.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context, "Internet connection is back now.", Toast.LENGTH_SHORT).show();
        }
    }
}