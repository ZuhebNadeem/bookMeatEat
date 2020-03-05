package com.example.mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mappe2.DBHandler.DB;
import com.example.mappe2.Klasser.Bestilling;
import com.example.mappe2.Klasser.Person;

import java.util.ArrayList;
import java.util.List;

public class SeBestilling extends AppCompatActivity {

    TextView txtBestillingDato;
    TextView txtBestillingRestaurant;
    TextView lblBestillingVenner;
    TextView txtBestillingVenner;

    public int valgtID;
    List<Bestilling> alleBestillinger;
    DB db;
    Bestilling bestilling;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_se_bestilling);

        db = new DB(this);

        txtBestillingDato = (TextView) findViewById(R.id.txtBestillingDato);
        txtBestillingRestaurant = (TextView) findViewById(R.id.txtBestillingRestaurant);
        lblBestillingVenner = (TextView) findViewById(R.id.lblBestillingVenner);
        txtBestillingVenner = (TextView) findViewById(R.id.txtBestillingVenner);


        alleBestillinger = db.visAlleBestillinger();


        Intent hentIntentVerdier = getIntent();
        valgtID = hentIntentVerdier.getIntExtra("ID",0);


         bestilling = alleBestillinger.get(valgtID-1);

        txtBestillingDato.setText(bestilling.getDato());

        txtBestillingRestaurant.setText(bestilling.getRestaurant().toString());

        ArrayList<Person> listVenner = bestilling.getDeltakelse();

        if(listVenner.size() > 0){
           lblBestillingVenner.setVisibility(View.VISIBLE);
           txtBestillingVenner.setVisibility(View.VISIBLE);

           String VennerTxt = "";

            for (Person p :listVenner) {
                VennerTxt += p.getNavn() + "   -  " + p.getTelefon() + "\n";
            }

            txtBestillingVenner.setText(VennerTxt);
        }



    }



    public void slettBestilling(View view) {
        db.SlettBestilling(valgtID);
        finish();
    }
}
