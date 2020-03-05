package com.example.mappe2;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mappe2.DBHandler.DB;
import com.example.mappe2.Klasser.Bestilling;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //DATAFELT
    ListView ListAlleBestillinger;
    DB db;
    private int MY_PERMISSIONS_REQUEST_SEND_SMS;
    private int MY_PHONE_STATE_PERMISSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        db = new DB(this);
        ListAlleBestillinger = findViewById(R.id.visAlleBestillinger);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("MeldingSMS", "Hei, du er invitert til et arrangement i morgen. Dette er en automatisk påminnelse");
        editor.commit();

        String testPref = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("MeldingSMS", "");

        VisAlle();
        startService();
        NavigerBaringsbar();
        ListViewListener();

        super.onCreate(savedInstanceState);

    }


    public void VisAlle() {
        //For å liste ut navnene våre til ListViewet
        ListAlleBestillinger.setAdapter(null);
        List<Bestilling> personList = db.visAlleBestillinger();
        ListAdapter visNavnene = new ArrayAdapter<Bestilling>(this, android.R.layout.simple_list_item_1, personList);
        ListAlleBestillinger.setAdapter(visNavnene);
    }

    @Override
    protected void onResume() {
        VisAlle();
        super.onResume();
    }

    public void startService() {
        boolean smsBool = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("ValgtSMS", true);
        boolean noftifikasjonBool = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("ValgtNotifikasjon", true);

        if(smsBool || noftifikasjonBool){
            Intent i = new Intent();
            i.setAction("com.example.mappe2.Service.MinBroadCast");
            sendBroadcast(i);
        }

       tillatSMS();
    }

    public void ListViewListener() {
        ListAlleBestillinger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bestilling bestilling = (Bestilling) adapterView.getAdapter().getItem(i);
                int verdi = bestilling.getId();


                //Starter ny intent, når en av bestillingene trykkes i ListViewet
                Intent seBestilling = new Intent(MainActivity.this, SeBestilling.class);
                //velger å sende med verdier til den nye intent
                seBestilling.putExtra("ID",verdi);
                startActivity(seBestilling);

            }
        });
    }

    public void tillatSMS(){
        MY_PERMISSIONS_REQUEST_SEND_SMS = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS);
        MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if(MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED &&
                MY_PHONE_STATE_PERMISSION == PackageManager.PERMISSION_GRANTED) {
        }
        else
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }


    //NAVIGERINGSBAR
    public void NavigerBaringsbar() {
        BottomNavigationView navigationView = findViewById(R.id.HjemNavnID);


        BottomNavigationView.OnNavigationItemSelectedListener navListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.hjemResturantNavBar:
                                Intent resturantNavbar = new Intent(MainActivity.this, MainRestaurant.class);
                                startActivity(resturantNavbar);
                                break;
                            case R.id.hjemPersonerNavBar:
                                Intent personNavBar = new Intent(MainActivity.this, MainPerson.class);
                                startActivity(personNavBar);
                                break;
                            case R.id.hjemInstillingNavbar:
                                Intent InstillingNavBar = new Intent(MainActivity.this, Preferanser.class);
                                startActivity(InstillingNavBar);
                                break;
                            case R.id.hjemLeggTilBestillingNavBar:
                                Intent LeggTilNavBar = new Intent(MainActivity.this, RegBestilling.class);
                                startActivity(LeggTilNavBar);
                                break;
                        }
                        return false;
                    }
                };
        navigationView.setOnNavigationItemSelectedListener(navListener);
    }
}
