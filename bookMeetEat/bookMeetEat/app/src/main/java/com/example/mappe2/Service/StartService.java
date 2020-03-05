package com.example.mappe2.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class StartService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("I broadcast","StartService");

        String valgtTid = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("ValgtTid", "5");
        int valgtTidTime = Integer.valueOf(valgtTid);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, valgtTidTime);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Intent i = new Intent(this, MinService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this,0,i,0);
        AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }


}
