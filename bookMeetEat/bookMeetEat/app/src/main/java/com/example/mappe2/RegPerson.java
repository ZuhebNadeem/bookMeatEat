package com.example.mappe2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mappe2.DBHandler.DB;
import com.example.mappe2.Klasser.Person;

import java.util.regex.Pattern;

public class RegPerson extends AppCompatActivity {
    //Definerer variabler
    EditText navnTxt;
    EditText tlfTxt;
    DB DB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_person);

        //Henter variabler fra xml
        navnTxt = (EditText) findViewById(R.id.txtRegPersonNavn);
        tlfTxt = (EditText) findViewById(R.id.txtRegPersonTelefon);
        DB = new DB(this);
    }

    //Registrer en Person-klasse til databasen
    public void RegistrerPerson(View view) {

        String innNavnet = navnTxt.getText().toString().trim();
        String innTlfnr = tlfTxt.getText().toString().trim();

        //Regex håndtere input-felter
        Pattern navnSjekk = Pattern.compile("[a-zæøå A-Zæøå]{3,30}");
        Pattern tlfSjekk = Pattern.compile("[0-9]{8}");

        if(navnSjekk.matcher(innNavnet).matches() && tlfSjekk.matcher(innTlfnr).matches() ) {
            Person person = new Person(navnTxt.getText().toString(),tlfTxt.getText().toString());
            DB.LeggTilPerson(person);
            finish();
        }
        else {
            Log.d("FEIL","FEIL");
            Toast.makeText(RegPerson.this,"Du må fylle ut feltene riktig",Toast.LENGTH_SHORT).show();
        }

    }


}



