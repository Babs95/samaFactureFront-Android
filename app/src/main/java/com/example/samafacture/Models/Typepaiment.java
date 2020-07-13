package com.example.samafacture.Models;

public class Typepaiment {
    private int id;
    private String libelle;
    private Integer typepaiement_id;

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

    public Integer getTypepaiement_id() {
        return typepaiement_id;
    }

    public void setTypepaiement_id(Integer typepaiement_id) {
        this.typepaiement_id = typepaiement_id;
    }
}
