package com.example.mappe2.Klasser;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Bestilling {

    //DATAFELT
    public int Id;
    public String Dato;
    public Restaurant restaurant;
    public ArrayList<Person> Deltakelse;


    //KONSTRUKTØR
    public Bestilling() {
    }

    public Bestilling(String dato, Restaurant restaurantID, ArrayList<Person> deltakelse) {
        Dato = dato;
        restaurant = restaurantID;
        Deltakelse = deltakelse;
    }

    public Bestilling(int id, String dato, Restaurant restaurantID, ArrayList<Person> deltakelse) {
        Id = id;
        Dato = dato;
        restaurant = restaurantID;
        Deltakelse = deltakelse;
    }


    //METODER
    @NonNull
    @Override
    public String toString() {
        String utTekst = "Dato: " + this.Dato
                + "\n"+ "Restaurant: " + this.restaurant.getNavn();

        if(!Deltakelse.isEmpty()){
            utTekst += "\n"  + "Antall venner: " + Deltakelse.size();
        }

        return utTekst;
    }


    //GET - SET
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDato() {
        return Dato;
    }

    public void setDato(String dato) {
        Dato = dato;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ArrayList<Person> getDeltakelse() {
        return Deltakelse;
    }

    public void setDeltakelse(ArrayList<Person> deltakelse) {
        Deltakelse = deltakelse;
    }
}
