package com.example.mappe2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mappe2.DBHandler.DB;
import com.example.mappe2.Klasser.Bestilling;
import com.example.mappe2.Klasser.Person;
import com.example.mappe2.Klasser.Restaurant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegBestilling extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //DATAFELT

    DB db;

    //For dato og tid
    Button velgTidspunktBtn;
    TextView valgtTidspunktTxt;
    DatePickerDialog datePickerDialog;
    int dag, måned, år, time, minutt;
    int dagFinal, månedFinal, årFinal, timeFinal, minuttFinal;

    //For resturanter
    TextView txtValgtRestaurant;
    Button btnVelgResturant ;
    List<Restaurant> listAlleRestauranter;
    String[] resturantArray;
    Restaurant valgtRestaurant;

    //For personer
    Button btnPersoner;
    TextView txtValgtePersoner;
    boolean[] valgtePersoner;
    List<Person> dropdownAllePersoner;
    String[] dropdownAllePersonerTekst;

    ArrayList<Person> valgtePersonerListe;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_bestilling);

        //DB
        db = new DB(this);

        //xml og funksjon oppsett
        oppsett();
    }



    //DATO OG TID

    //Metode for å velge dato og tid
    public void velgTidspunkt(View view) {
        //Calender definisjoner
         final Calendar clndr = Calendar.getInstance();
         dag = clndr.get(Calendar.DAY_OF_MONTH);
         måned = clndr.get(Calendar.MONTH);
         år = clndr.get(Calendar.YEAR);

        //date picker dialog
        datePickerDialog = new DatePickerDialog(RegBestilling.this,RegBestilling.this,år,måned,dag);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

        velgTidspunktBtn.setBackgroundResource(android.R.drawable.btn_default);
        velgTidspunktBtn.setText("Endre dato og tid");


    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        årFinal = i;
        månedFinal = i1+1;
        dagFinal = i2;

        Calendar c = Calendar.getInstance();
        time = c.get(Calendar.HOUR_OF_DAY);
        minutt = c.get(Calendar.MINUTE);

        TimePickerDialog datoDialog = new TimePickerDialog(this,this,time,minutt, true);
        datoDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        timeFinal = i;
        minuttFinal = i1;

        valgtTidspunktTxt.setText(" "+dagFinal+"."+månedFinal+"."+årFinal+" "+sjekkTid(timeFinal)+":"+sjekkTid(minuttFinal));

    }

    public String sjekkTid(int tid) {
        if (tid >= 10) {
            return String.valueOf(tid);
        } else {
            return "0" + String.valueOf(tid);
        }
    }




    //RESTAURANT

    //Metode for å velge resturant, kan kun velge en
    public void velgResturant(View view) {
        AlertDialog.Builder restaurantBuilder = new AlertDialog.Builder(RegBestilling.this);
        restaurantBuilder.setTitle("Velg Resturant");

        restaurantBuilder.setSingleChoiceItems(resturantArray, -1,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtValgtRestaurant.setText(resturantArray[i] + "\n");
                        valgtRestaurant.setId(listAlleRestauranter.get(i).getId());
                        valgtRestaurant.setNavn(listAlleRestauranter.get(i).getNavn());
                        valgtRestaurant.setType(listAlleRestauranter.get(i).getType());
                        valgtRestaurant.setTelefon(listAlleRestauranter.get(i).getTelefon());
                        valgtRestaurant.setAddresse(listAlleRestauranter.get(i).getAddresse());

                        dialogInterface.dismiss();

                    }
                });

        AlertDialog restaurantDialog = restaurantBuilder.create();
        restaurantDialog.show();

        btnVelgResturant.setBackgroundResource(android.R.drawable.btn_default);
        btnVelgResturant.setText("Endre Restaurant");




    }




    //VENNER

    //Lager dropdown list med checkbox for venner man vil invitere
    public void velgPersoner(View view) {
            AlertDialog.Builder personBuilder = new AlertDialog.Builder(RegBestilling.this);
            personBuilder.setTitle("Velg venner");


            personBuilder.setMultiChoiceItems(dropdownAllePersonerTekst, valgtePersoner, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int indeks, boolean valgt) {
                    valgtePersoner[indeks] = valgt;
                }
            });

            personBuilder.setPositiveButton("Bekreft", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txtValgtePersoner.setText("Du har valgt: \n");
                    for (int t = 0; t<valgtePersoner.length; t++){
                        boolean checked = valgtePersoner[t];
                        if (checked) {
                            txtValgtePersoner.append(dropdownAllePersonerTekst[t] + "\n");
                            valgtePersonerListe.add(dropdownAllePersoner.get(t));
                        }
                    }
                }
            });

            personBuilder.setCancelable(false);

            AlertDialog personDialog = personBuilder.create();
            personDialog.show();

            btnPersoner.setBackgroundResource(android.R.drawable.btn_default);
            btnPersoner.setText("Endre valgte venner");

    }


        //METODER
    public void personerOppsett(){
        txtValgtePersoner = (TextView) findViewById(R.id.visAllePersonerBestilling);

        dropdownAllePersoner = db.visAllePersoner();

        dropdownAllePersonerTekst = new String[dropdownAllePersoner.size()];

        for (int i = 0; i < dropdownAllePersoner.size() ; i++) {
            dropdownAllePersonerTekst[i] = dropdownAllePersoner.get(i).getNavn();
        }

        valgtePersoner = new boolean[dropdownAllePersoner.size()];

        valgtePersonerListe = new ArrayList<Person>();
    }

    public void datoOppsett(){
        valgtTidspunktTxt = (TextView) findViewById(R.id.valgtTidspunkt);
    }

    public void restaurantOppsett(){
            txtValgtRestaurant = (TextView) findViewById(R.id.visValgtResturant);

            listAlleRestauranter = db.visAlleRestauranter();

            resturantArray = new String[listAlleRestauranter.size()];

            for(int i=0; i<listAlleRestauranter.size(); i++) {
                resturantArray[i] = listAlleRestauranter.get(i).getNavn();
            }

            valgtRestaurant = new Restaurant();
        }

    public void oppsett(){
        btnVelgResturant = (Button) findViewById(R.id.btnRegBestillingVelgResturant);
        btnPersoner = (Button) findViewById(R.id.btnRegBestillingVelgPersoner);
        velgTidspunktBtn = (Button) findViewById(R.id.btnRegBestillingVelgTidspunkt);

        datoOppsett();
        restaurantOppsett();
        personerOppsett();
        }


    public void DBBestilling(View view) {
            String innTid = valgtTidspunktTxt.getText().toString();
            String innResturant = txtValgtRestaurant.getText().toString();
            String innPersoner = txtValgtePersoner.getText().toString();

            if(!innTid.matches("") && !innResturant.matches("") ) {
                Bestilling nyBestilling = new Bestilling(valgtTidspunktTxt.getText().toString(),valgtRestaurant,valgtePersonerListe);
                db.LeggTilBestilling(nyBestilling);
                finish();
            }
            else {
                Toast.makeText(RegBestilling.this,"Du må fylle ut feltene riktig",Toast.LENGTH_SHORT).show();
            }
    }
} //SLUTT PÅ KLASSEN

