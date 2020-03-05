package com.example.mappe2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mappe2.Service.MinService;

public class Preferanser extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefListener();
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }



    //PREFERANSER
    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferanser);
        }
    }


    protected void notifikasjon() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean valgt = sharedPreferences.getBoolean("notifikasjon",true);
        getSharedPreferences("PREFERENCE",MODE_PRIVATE)
                .edit().putBoolean("ValgtNotifikasjon",valgt).apply();


    }


    protected void smsNotifikasjon(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean valgt = sharedPreferences.getBoolean("sms",true);
        getSharedPreferences("PREFERENCE",MODE_PRIVATE)
                .edit().putBoolean("ValgtSMS",valgt).apply();

    }

    protected void valgtTid(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String valgt = sharedPreferences.getString("valgtTid","5");
        getSharedPreferences("PREFERENCE",MODE_PRIVATE)
                .edit().putString("ValgtTid",valgt).apply();

    }



    //LISTENER
    private void prefListener(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals("notifikasjon")){
                    notifikasjon();
                    serviceManager();
                    boolean test = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("ValgtNotifikasjon", true);
                    Log.d("Pref: ", "Byttet notifikasjon: " + test);
                }
                if (s.equals("sms")){
                    smsNotifikasjon();
                    serviceManager();
                    boolean test = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("ValgtSMS", true);
                    Log.d("Pref: ", "Byttet SMS notifikasjon: " + test);

                }
                if (s.equals("valgtTid")){
                    valgtTid();
                    serviceManager();
                    String valgtTid = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("ValgtTid", "5");
                    Log.d("Pref: ", "Byttet valgt tid: " + valgtTid);


                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener);
    }


    public void serviceManager(){
        boolean smsBool = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("ValgtSMS", true);
        boolean noftifikasjonBool = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("ValgtNotifikasjon", true);


        if(smsBool || noftifikasjonBool){
            Intent i = new Intent();
            i.setAction("com.example.mappe2.Service.MinBroadCast");
            sendBroadcast(i);
        }else {
            Intent i = new Intent(this, MinService.class);
            PendingIntent pIntent = PendingIntent.getService(this,0,i,0);

            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if(alarm != null){
                alarm.cancel(pIntent);
                stopService(i);
                Log.d("StopService: ", "Stoppet service - SMS: " + smsBool + " og Notifikasjon: " + noftifikasjonBool);
            }
        }

    }






}
