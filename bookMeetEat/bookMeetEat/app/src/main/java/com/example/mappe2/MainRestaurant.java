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
import com.example.mappe2.Klasser.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainRestaurant extends AppCompatActivity {

    ListView ListAlleRestauranter;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_restaurant);

        ListAlleRestauranter = findViewById(R.id.visAlleRestauranter);
        db = new DB(this);

        VisAlle();
        ListViewListener();
        NavigerBaringsbarResturant();
    }



    public void VisAlle() {
        ListAlleRestauranter.setAdapter(null);
        List<Restaurant> restaurantList = db.visAlleRestauranter();
        ListAdapter visNavnene = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, restaurantList);
        ListAlleRestauranter.setAdapter(visNavnene);
    }

    public void ListViewListener() {
        ListAlleRestauranter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Restaurant restaurant = (Restaurant) adapterView.getAdapter().getItem(i);
                int verdi = restaurant.getId();
                String navn = restaurant.getNavn();
                String adr = restaurant.getAddresse();
                String tlf = restaurant.getTelefon();
                String type = restaurant.getType();


                //Starter ny intent, når en av navnene trykkes i ListViewet
                Intent endreRestauranten = new Intent(MainRestaurant.this, EndreRestaurant.class);
                //velger å sende med verdier til den nye intent
                endreRestauranten.putExtra("ID",verdi);
                endreRestauranten.putExtra("Navn",navn);
                endreRestauranten.putExtra("Adresse",adr);
                endreRestauranten.putExtra("Telefon",tlf);
                endreRestauranten.putExtra("Type",type);
                startActivity(endreRestauranten);

            }
        });
    }

    @Override
    protected void onResume() {
        VisAlle();
        super.onResume();
    }


    //NAVIGERINGSBAR
    public void NavigerBaringsbarResturant() {
        BottomNavigationView navigationView = findViewById(R.id.ResturantnavBar);


        BottomNavigationView.OnNavigationItemSelectedListener navListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.RhjemNavBar:
                                finish();
                                break;
                            case R.id.RPersonerNavBar:
                                Intent personNavBar = new Intent(MainRestaurant.this, MainPerson.class);
                                startActivity(personNavBar);
                                finish();
                                break;
                            case R.id.RLeggTilNavBar:
                                Intent InstillingNavBar = new Intent(MainRestaurant.this, RegRestaurant.class);
                                startActivity(InstillingNavBar);
                                break;
                        }
                        return false;
                    }
                };
        navigationView.setOnNavigationItemSelectedListener(navListener);
    }





}
