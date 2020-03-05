package com.example.mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mappe2.DBHandler.DB;
import com.example.mappe2.Klasser.Restaurant;

import java.util.regex.Pattern;

public class EndreRestaurant extends AppCompatActivity {

    //Variabler
    EditText navnFeltTxt;
    EditText adresseFeltTxt;
    EditText telefonFeltTxt;
    EditText typeFeltTxt;

    DB DB;

    public int valgtID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endre_restaurant);

        navnFeltTxt = (EditText) findViewById(R.id.txtEndreRestaurantNavn);
        adresseFeltTxt = (EditText) findViewById(R.id.txtEndreRestaurantAdresse);
        telefonFeltTxt = (EditText) findViewById(R.id.txtEndreRestaurantTelefon);
        typeFeltTxt = (EditText) findViewById(R.id.txtEndreRestaurantType);
        DB = new DB(this);

        //henter verdiene vi valgte å sende med i intent fra forrige java-klasse(Mainactivity)
        Intent hentIntentVerdier = getIntent();
        valgtID = hentIntentVerdier.getIntExtra("ID",0);//henter ut ID som ble valgt i ListViewet
        navnFeltTxt.setText(hentIntentVerdier.getStringExtra("Navn"));
        adresseFeltTxt.setText(hentIntentVerdier.getStringExtra("Adresse"));
        telefonFeltTxt.setText(hentIntentVerdier.getStringExtra("Telefon"));
        typeFeltTxt.setText(hentIntentVerdier.getStringExtra("Type"));
    }


    //ONCLICK METODER
    public void endreRestaurant(View view) {

        String innNavnet = navnFeltTxt.getText().toString().trim();
        String innAdresse = adresseFeltTxt.getText().toString().trim();
        String innTlfnr = telefonFeltTxt.getText().toString().trim();
        String innType = typeFeltTxt.getText().toString().trim();


        //Regex håndtere input-felter
        Pattern navnSjekk = Pattern.compile("[a-zæøå A-Zæøå]{3,30}");
        Pattern tlfSjekk = Pattern.compile("[0-9]{8}");
        Pattern adresseSjekk = Pattern.compile("[a-zæøå A-Zæøå 0-9]{3,50}");
        Pattern typeSjekk = Pattern.compile("[a-zæøå A-Zæøå]{3,30}");


        if(navnSjekk.matcher(innNavnet).matches() && tlfSjekk.matcher(innTlfnr).matches()
                && adresseSjekk.matcher(innAdresse).matches() && typeSjekk.matcher(innType).matches() ) {
            Restaurant restaurant = new Restaurant(
                    Integer.valueOf(valgtID),
                    navnFeltTxt.getText().toString(),
                    adresseFeltTxt.getText().toString(),
                    telefonFeltTxt.getText().toString(),
                    typeFeltTxt.getText().toString());

            DB.oppdaterRestaurant(restaurant);//oppdaterer databasen med den verdien som blir skrevet inn til den ID som ble valgt

            finish();

        }
        else {
            Toast.makeText(EndreRestaurant.this,"Du må fylle ut feltene riktig",Toast.LENGTH_SHORT).show();
        }
    }


    public void slettRestaurant(View view) {
        DB.SlettRestaurant(Integer.valueOf(valgtID));
        finish();
    }
}
