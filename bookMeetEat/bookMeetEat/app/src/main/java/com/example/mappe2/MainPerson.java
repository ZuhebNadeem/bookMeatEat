package com.example.mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mappe2.DBHandler.DB;
import com.example.mappe2.Klasser.Person;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainPerson extends AppCompatActivity {
    //Definerer variabler
    ListView ListAllePersoner;
    DB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_person);

        //Henter fra xml og gi tilgang til vår database
        ListAllePersoner = findViewById(R.id.visAllePersoner);
        DB = new DB(this);


        //Kjører disse metodene ved onCreate
        VisAlle();
        ListViewListener();
        NavigerBaringsbarPerson();
    }


    //METODER



    //Lister ut alle navnene fra databasen og viser det i et ListView
    public void VisAlle() {
        //For å liste ut navnene våre til ListViewet
        ListAllePersoner.setAdapter(null);
        List<Person> personList = DB.visAllePersoner();
        ListAdapter visNavnene = new ArrayAdapter<Person>(this, android.R.layout.simple_list_item_1, personList);
            ListAllePersoner.setAdapter(visNavnene);
    }



        //Setter onClickListener på ListViewet vi nettopp opprettet. På hver felt har onClick på det navnet(ListViewet) som velges
    public void ListViewListener() {
        ListAllePersoner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Person person = (Person)adapterView.getAdapter().getItem(i);
                int verdi = person.getId();
                String navn = person.getNavn(); //vil gi oss den verdien vi trykker på
                String tlf = person.getTelefon();


                //Starter ny intent, når en av navnene trykkes i ListViewet
                Intent endrePersonen = new Intent(MainPerson.this, EndrePerson.class);
                //velger å sende med verdier til den nye intent
                endrePersonen.putExtra("ID",verdi);
                endrePersonen.putExtra("Navn",navn);
                endrePersonen.putExtra("Telefon",tlf);
                startActivity(endrePersonen);

            }
        });
    }

    @Override
    protected void onResume() {
        VisAlle();
        super.onResume();
    }



    //NAVIGERINGSBAR
    public void NavigerBaringsbarPerson() {
        BottomNavigationView navigationView = findViewById(R.id.PersonNavBar);


        BottomNavigationView.OnNavigationItemSelectedListener navListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.PhjemNavBar:
                                finish();
                                break;
                            case R.id.PResturantNavBar:
                                Intent personNavBar = new Intent(MainPerson.this, MainRestaurant.class);
                                startActivity(personNavBar);
                                finish();
                                break;
                            case R.id.LeggTilPerson:
                                Intent InstillingNavBar = new Intent(MainPerson.this, RegPerson.class);
                                startActivity(InstillingNavBar);
                                break;
                        }
                        return false;
                    }
                };
        navigationView.setOnNavigationItemSelectedListener(navListener);
    }


}
