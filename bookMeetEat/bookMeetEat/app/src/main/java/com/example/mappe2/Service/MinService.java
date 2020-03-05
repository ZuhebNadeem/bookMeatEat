package com.example.mappe2.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.mappe2.DBHandler.DB;
import com.example.mappe2.Klasser.Bestilling;
import com.example.mappe2.Klasser.Person;
import com.example.mappe2.MainActivity;
import com.example.mappe2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MinService extends Service {

    DB db = new DB(this);
    private int MY_PERMISSIONS_REQUEST_SEND_SMS;
    private int MY_PHONE_STATE_PERMISSION;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean smsBool = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("ValgtSMS", true);
        boolean noftifikasjonBool = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("ValgtNotifikasjon", true);
        String message = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("MeldingSMS", "Hei, du er invitert til et arrangement med din venn i morgen. Dette er en automatisk påminnelse");

        ArrayList<Bestilling> liste = varselListe();


        if(noftifikasjonBool){
            //Lager notifikasjon for når AlarmManager settes
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent i = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,0);

            if(liste != null && liste.size() != 0){
                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle("Reservasjon i morgen!").setContentText("Du har " + liste.size() + " bestilling(er) som næmrer seg!")
                        .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
                        .build();

                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0,notification);
            }else{
                Log.d("Notifikasjon","INGEN BESTILLINGER SOM SKAL VARSLES");
            }
        }

        if (smsBool){
            if(liste != null && liste.size() != 0){
                for (Bestilling b: liste) {
                    if(b.Deltakelse != null && b.Deltakelse.size() != 0){
                        for (Person p: b.Deltakelse) {
                            SendSMS(p.Telefon,message);
                        }
                    }
                }
            }




        }

        return super.onStartCommand(intent, flags, startId);
    }

    public ArrayList<Bestilling> varselListe(){

        //Brukervalgt dag, som sjekker hvor mange dager før personen skal få notifikasjon.
        int antallDager = 1;

        //Bestillinger som skal varsles
        ArrayList<Bestilling> varselListe = new ArrayList<Bestilling>();

        //Vår dato format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        //dagSjekk
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, antallDager); // <-- Bruker valgt dag
        Date dagSjekk = cal.getTime();

        List<Bestilling> bestillinger = db.visAlleBestillinger();


        for (Bestilling b: bestillinger) {
            try {
                boolean finnesBestilling = false;
                Date bestillingDato = simpleDateFormat.parse(b.getDato());
                Log.d("test", "TestVerdi: " + bestillingDato.toString() + "  Imorgen: " + dagSjekk.toString());

                if(dagSjekk.getMonth() == bestillingDato.getMonth() && dagSjekk.getDay() == bestillingDato.getDay() && dagSjekk.getYear() == bestillingDato.getYear()){
                    Log.d("test", "TRUE: SAMME DATO");
                    finnesBestilling = true;
                }else {
                    Log.d("test", "FALSE:");
                }

                if(finnesBestilling){
                    varselListe.add(b);
                }

            }catch (Exception e){
                return null;
            }
        }

        return varselListe;
    }



    public void SendSMS(String tlfNr, String message) {

        MY_PERMISSIONS_REQUEST_SEND_SMS = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        //message

        if(MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED &&
                MY_PHONE_STATE_PERMISSION == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsMan= SmsManager.getDefault();
            smsMan.sendTextMessage(tlfNr, null, message, null, null);
            Log.d("SMS: ", "Har sendt ut SMS til: " + tlfNr);
        }else {
            Log.d("SMS: ", "Har ikke PERMISSION til å sende SMS");

        }
    }

}
