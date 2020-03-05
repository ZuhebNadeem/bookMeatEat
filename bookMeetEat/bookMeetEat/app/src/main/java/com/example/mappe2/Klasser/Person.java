package com.example.mappe2.Klasser;

import androidx.annotation.NonNull;

public class Person {
    //Variabler
    public int Id;
    public String Navn;
    public String Telefon;

    //3 forskjellige konstruktør, avhengig av hva vi trenger bruker vi disse
    public Person(int id, String navn, String telefon) {
        Id = id;
        Navn = navn;
        Telefon = telefon;
    }

    public Person(String navn, String telefon) {
        Navn = navn;
        Telefon = telefon;
    }

    public Person() {
    }

    //Getter og Setter til variablene

    @NonNull
    @Override
    public String toString() {
        return "Navn: " + this.Navn;
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
        this.Navn = navn;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }
}
