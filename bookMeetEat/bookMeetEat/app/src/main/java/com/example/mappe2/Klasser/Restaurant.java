package com.example.mappe2.Klasser;

import androidx.annotation.NonNull;

public class Restaurant {
    public int Id;
    public String Navn;
    public String Addresse;
    public String Telefon;
    public String Type;


    //KONSTRUKTØR
    public Restaurant(){

    }

    public Restaurant(String navn, String addresse, String telefon, String type){
        Navn = navn;
        Addresse = addresse;
        Telefon = telefon;
        Type = type;
    }

    public Restaurant(int id, String navn, String addresse, String telefon, String type){
        Id = id;
        Navn = navn;
        Addresse = addresse;
        Telefon = telefon;
        Type = type;
    }


    //METODER

    @NonNull
    @Override
    public String toString() {
        return this.Navn + " - Type: " + this.Type;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNavn() {
        return Navn;
    }

    public void setNavn(String navn) {
        Navn = navn;
    }

    public String getAddresse() {
        return Addresse;
    }

    public void setAddresse(String addresse) {
        Addresse = addresse;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
