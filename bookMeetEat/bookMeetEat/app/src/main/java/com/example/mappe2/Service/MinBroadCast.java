package com.example.mappe2.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MinBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context,StartService.class);
        context.startService(i);

        Log.d("I broadcast","BROADCAST");
    }

}
