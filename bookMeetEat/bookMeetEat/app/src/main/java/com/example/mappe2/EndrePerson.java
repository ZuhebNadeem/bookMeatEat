package com.example.mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mappe2.DBHandler.DB;
import com.example.mappe2.Klasser.Person;

import java.util.regex.Pattern;

public class EndrePerson extends AppCompatActivity {
    //Variabler
    EditText navnFeltTxt;
    EditText telefonFeltTxt;
    DB DB;

    public int valgtID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endre_person);

        //henter verdier fra xml og gir tilgang til databasen vår
        navnFeltTxt = (EditText) findViewById(R.id.txtEndrePersonNavn);
        telefonFeltTxt =  (EditText) findViewById(R.id.txtEndrePersonTelefon);
        DB = new DB(this);

        //henter verdiene vi valgte å sende med i intent fra forrige java-klasse(Mainactivity)
        Intent hentIntentVerdier = getIntent();
        valgtID = hentIntentVerdier.getIntExtra("ID",0);//henter ut ID som ble valgt i ListViewet

        navnFeltTxt.setText(hentIntentVerdier.getStringExtra("Navn"));
        telefonFeltTxt.setText(hentIntentVerdier.getStringExtra("Telefon"));
    }

    //ONCLICK METODER
    public void endrePerson(View view) {

        String innNavnet = navnFeltTxt.getText().toString().trim();
        String innTlfnr = telefonFeltTxt.getText().toString().trim();

        //Regex håndtere input-felter
        Pattern navnSjekk = Pattern.compile("[a-zæøå A-Zæøå]{3,30}");
        Pattern tlfSjekk = Pattern.compile("[0-9]{8}");

        if(navnSjekk.matcher(innNavnet).matches() && tlfSjekk.matcher(innTlfnr).matches() ) {
            Person person = new Person(Integer.valueOf(valgtID),navnFeltTxt.getText().toString(),telefonFeltTxt.getText().toString());
            DB.oppdaterPerson(person);//oppdaterer databasen med den verdien som blir skrevet inn til den ID som ble valgt
            finish();
        }
        else {
            Toast.makeText(EndrePerson.this,"Du må fylle ut feltene riktig",Toast.LENGTH_SHORT).show();
        }
    }

    public void slettPerson(View view) {
        DB.SlettPerson(Integer.valueOf(valgtID));
        finish();
    }
}
