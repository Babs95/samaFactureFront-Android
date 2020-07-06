package com.example.samafacture.Models;

public class Typepaiment {
    private int id;
    private String libelle;

    public Typepaiment(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Typepaiment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
