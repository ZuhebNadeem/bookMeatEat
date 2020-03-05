package com.example.mappe2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mappe2.DBHandler.DB;
import com.example.mappe2.Klasser.Restaurant;

import java.util.regex.Pattern;

public class RegRestaurant extends AppCompatActivity {

    //Variabler
    EditText navnFeltTxt;
    EditText adresseFeltTxt;
    EditText telefonFeltTxt;
    EditText typeFeltTxt;
    DB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_restaurant);

        //Henter variabler fra xml
        navnFeltTxt = (EditText) findViewById(R.id.txtRegRestaurantNavn);
        adresseFeltTxt = (EditText) findViewById(R.id.txtRegRestaurantAdresse);
        telefonFeltTxt = (EditText) findViewById(R.id.txtRegRestaurantTelefon);
        typeFeltTxt = (EditText) findViewById(R.id.txtRegRestaurantType);
        DB = new DB(this);
    }

    //Registrer en Person-klasse til databasen
    public void RegistrerRestaurant(View view) {

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
                    navnFeltTxt.getText().toString(),
                    adresseFeltTxt.getText().toString(),
                    telefonFeltTxt.getText().toString(),
                    typeFeltTxt.getText().toString());
            DB.LeggTilRestaurant(restaurant);
            finish();
        }
        else {
            Log.d("FEIL","FEIL i registrering av resturant");
            Toast.makeText(RegRestaurant.this,"Du må fylle ut feltene riktig",Toast.LENGTH_SHORT).show();
        }
    }

}
