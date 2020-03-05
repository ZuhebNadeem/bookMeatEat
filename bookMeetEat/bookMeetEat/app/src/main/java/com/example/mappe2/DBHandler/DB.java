package com.example.mappe2.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mappe2.Klasser.Bestilling;
import com.example.mappe2.Klasser.Person;
import com.example.mappe2.Klasser.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    //DB
    static String DATABASE_NAME = "Bestillinger";
    static int DATABASE_VERSION = 1;

    //VARIABLER
    static String KEY_ID = "ID";
    static String KEY_NAVN = "Navn";
    static String KEY_TYPE = "Type";
    static String KEY_ADRESSE = "Adresse";
    static String KEY_TLF = "Telefon";
    static String KEY_DATO = "Dato";
    static String KEY_RID = "RID";
    static String KEY_PID = "PID";
    static String KEY_BID = "BID";

    //TABELLER
    static String TABLE_PERSON = "PersonTabell";
    static String TABLE_RESTAURANT = "RestaurantTabell";
    static String TABLE_BESTILLING = "BestillingTabell";

    static String TABLE_PERSON_DELTAKELSE = "PersonDeltakelseTabell";

    @Override
    public void onConfigure(SQLiteDatabase db) {
        //MÅTTE ENABALE FREMMEDNØKLER FOR FOR BRUK AV API VERSJON 23
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
    }


//OVERRIDE METODER + NØDVENDIGE METODER

    public DB(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    //Her lager vi databasen
    @Override
    public void onCreate(SQLiteDatabase db) {
        String personTabell = "CREATE TABLE "+TABLE_PERSON+" ("+KEY_ID+" " +
                "INTEGER PRIMARY KEY, "+KEY_NAVN+" TEXT, "+KEY_TLF+" TEXT"+")";

        String restaurantTabell = "CREATE TABLE "+TABLE_RESTAURANT+" ("+KEY_ID+" " +
                "INTEGER PRIMARY KEY, "+KEY_NAVN+" TEXT, "+ KEY_ADRESSE +" TEXT, "+KEY_TLF+" TEXT, "+KEY_TYPE+" TEXT"+")";

        String bestillingTabell = "CREATE TABLE "+TABLE_BESTILLING+" ("
                +KEY_ID+" " + "INTEGER PRIMARY KEY, "
                +KEY_DATO+" TEXT, "
                +KEY_RID+ " INTEGER, FOREIGN KEY ("+KEY_RID+") REFERENCES "+TABLE_RESTAURANT+"("+KEY_ID+") ON DELETE CASCADE"+")";

        String personDeltakelseTabell = "CREATE TABLE "+TABLE_PERSON_DELTAKELSE+" ("
                + KEY_PID + " INTEGER, "
                + KEY_BID + " INTEGER, "
                + "PRIMARY KEY (" +KEY_PID+", "+KEY_BID+ "), "
                +"FOREIGN KEY ("+KEY_PID+") REFERENCES "+TABLE_PERSON+"("+KEY_ID+") ON DELETE CASCADE, "
                +"FOREIGN KEY ("+KEY_BID+") REFERENCES "+TABLE_BESTILLING+"("+KEY_ID+") ON DELETE CASCADE"
        +")";

        //dagensBestillinger();

        //Legger til Tabeller til DB
        db.execSQL(personTabell);
        db.execSQL(restaurantTabell);
        db.execSQL(bestillingTabell);
        db.execSQL(personDeltakelseTabell);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_PERSON);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_RESTAURANT);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_BESTILLING);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_PERSON_DELTAKELSE);
            onCreate(db);
    }





    //PERSON - METODE

    //Returner liste av alle personer i DB
    public List<Person> visAllePersoner() {
        List<Person> personList = new ArrayList<Person>();

        String select = "SELECT * FROM " + TABLE_PERSON;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select,null);//cursor peker til db.

        //rawQuyery retunerer rader og kolonner i cursor
        if(cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setId(cursor.getInt(0));
                person.setNavn(cursor.getString(1));
                person.setTelefon(cursor.getString(2));
                personList.add(person);
            } while (cursor.moveToNext()); //gjør dette så lenge markøren flyttes til neste rad
            cursor.close();
            db.close();
        }
        return personList;
    }

    //Vi lagrer Person-klassen til databasen.
    public void LeggTilPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); //brukes for å lagre et sett med verdier

        values.put(KEY_NAVN,person.getNavn());
        values.put(KEY_TLF,person.getTelefon());

        db.insert(TABLE_PERSON,null,values);
        db.close();
    }

    //Metode for å endre person DB
    public void oppdaterPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); //brukes for å lagre et sett med verdier

        values.put(KEY_NAVN,person.getNavn());
        values.put(KEY_TLF,person.getTelefon());

        db.update(TABLE_PERSON,values,KEY_ID + "= ?",
                new String[]{String.valueOf(person.getId())});
        db.close();
    }


    //Metode for å slette en person fra DB
    public void SlettPerson(int personID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PERSON,KEY_ID + "= ?",
                new String[]{String.valueOf(personID)});
        db.close();
    }



    //RESTAURANTER - METODE

    //Lister alle restauranter
    public List<Restaurant> visAlleRestauranter() {
        List<Restaurant> restaurantList = new ArrayList<Restaurant>();

        String select = "SELECT * FROM " + TABLE_RESTAURANT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select,null);//cursor peker til db.

        //rawQuyery retunerer rader og kolonner i cursor
        if(cursor.moveToFirst()) {
            do {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(cursor.getInt(0));
                restaurant.setNavn(cursor.getString(1));
                restaurant.setAddresse(cursor.getString(2));
                restaurant.setTelefon(cursor.getString(3));
                restaurant.setType(cursor.getString(4));
                restaurantList.add(restaurant);
            } while (cursor.moveToNext()); //gjør dette så lenge markøren flyttes til neste rad
            cursor.close();
            db.close();
        }
        return restaurantList;
    }

    //Vi lagrer Restaurant til databasen.
    public void LeggTilRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); //brukes for å lagre et sett med verdier

        values.put(KEY_NAVN,restaurant.getNavn());
        values.put(KEY_ADRESSE,restaurant.getAddresse());
        values.put(KEY_TLF,restaurant.getTelefon());
        values.put(KEY_TYPE,restaurant.getType());

        db.insert(TABLE_RESTAURANT,null,values);
        db.close();
    }


    //Metode for å endre person DB
    public void oppdaterRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); //brukes for å lagre et sett med verdier

        values.put(KEY_NAVN,restaurant.getNavn());
        values.put(KEY_ADRESSE,restaurant.getAddresse());
        values.put(KEY_TLF,restaurant.getTelefon());
        values.put(KEY_TYPE,restaurant.getType());

        db.update(TABLE_RESTAURANT,values,KEY_ID + "= ?",
                new String[]{String.valueOf(restaurant.getId())});
        db.close();
    }

    //Metode for å slette en person fra DB
    public void SlettRestaurant(int restaurantID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANT,KEY_ID + "= ?",
                new String[]{String.valueOf(restaurantID)});
        db.close();
    }


    //BESTILLING
    public void LeggTilBestilling(Bestilling bestilling){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesBestilling = new ContentValues(); //brukes for å lagre et sett med verdier

        Log.d("test", "LeggTilBestilling: " + bestilling.getRestaurant().getId());
        valuesBestilling.put(KEY_DATO,bestilling.getDato());
        valuesBestilling.put(KEY_RID,bestilling.getRestaurant().getId());

        db.insert(TABLE_BESTILLING,null,valuesBestilling);

        //FINNER SISTE BESTILLING
        String select = "SELECT * FROM " + TABLE_BESTILLING;
        Cursor cursor = db.rawQuery(select,null);//cursor peker til db.
        //Legger til venner
        if(cursor.moveToLast()){
            Log.d("test","legger til restaurant: " + cursor.getInt(0));
            int bestillingID =  cursor.getInt(0);
            for (Person p: bestilling.Deltakelse) {
                ContentValues valuesPersonDeltakelse = new ContentValues();

                valuesPersonDeltakelse.put(KEY_PID,p.Id);
                valuesPersonDeltakelse.put(KEY_BID,bestillingID);

                db.insert(TABLE_PERSON_DELTAKELSE,null,valuesPersonDeltakelse);
            }
        }

        db.close();
    }


    public List<Bestilling> visAlleBestillinger() {
        List<Bestilling> bestillingList = new ArrayList<Bestilling>();

        String select = "SELECT * FROM " + TABLE_BESTILLING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select,null);//cursor peker til db.

        //rawQuyery retunerer rader og kolonner i cursor
        if(cursor.moveToFirst()) {
            do {
                Bestilling bestilling = new Bestilling();
                bestilling.setId(cursor.getInt(0));
                bestilling.setDato(cursor.getString(1));
                int restaurantID = cursor.getInt(2);

                Cursor cursorRestaurant = db.rawQuery("SELECT * FROM "+ TABLE_RESTAURANT + " WHERE " + KEY_ID + " = " + restaurantID, null);
                cursorRestaurant.moveToFirst();

                Restaurant restaurant = new Restaurant();
                restaurant.setId(cursorRestaurant.getInt(0));
                restaurant.setNavn(cursorRestaurant.getString(1));
                restaurant.setAddresse(cursorRestaurant.getString(2));
                restaurant.setTelefon(cursorRestaurant.getString(3));
                restaurant.setType(cursorRestaurant.getString(4));

                bestilling.setRestaurant(restaurant);

                Log.d("TAG", "visAlleBestillinger: "
                        + bestilling.getId() + " "
                        + bestilling.getDato() + " "
                        + bestilling.getRestaurant().getNavn() + " ");

                Cursor cursorPersonDeltakelse = db.rawQuery("SELECT * FROM "+ TABLE_PERSON_DELTAKELSE + " WHERE " + KEY_BID + " = " + bestilling.getId(), null);
                ArrayList<Person> deltaklse = new ArrayList<>();

                if(cursorPersonDeltakelse.moveToFirst()){
                    do{
                        Cursor cursorPerson = db.rawQuery("SELECT * FROM "+ TABLE_PERSON + " WHERE " + KEY_ID + " = " + cursorPersonDeltakelse.getInt(0), null);
                        cursorPerson.moveToFirst();
                        Person person = new Person();
                        person.setId(cursorPerson.getInt(0));
                        person.setNavn(cursorPerson.getString(1));
                        person.setTelefon(cursorPerson.getString(2));
                        deltaklse.add(person);
                    }while (cursorPersonDeltakelse.moveToNext());
                }

                bestilling.setDeltakelse(deltaklse);
                bestillingList.add(bestilling);
            } while (cursor.moveToNext()); //gjør dette så lenge markøren flyttes til neste rad
            cursor.close();
            db.close();
        }
        return bestillingList;
    }

    //Metode for å slette en person fra DB
    public void SlettBestilling(int bestillingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BESTILLING,KEY_ID + "= ?",
                new String[]{String.valueOf(bestillingID)});
        db.close();
    }






}
