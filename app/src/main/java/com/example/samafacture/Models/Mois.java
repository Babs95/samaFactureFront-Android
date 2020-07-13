package com.example.samafacture.Models;

public class Mois {
    private int id;
    private String libelle;
    private Integer mois_id;

    public Mois() {
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

    public Integer getMois_id() {
        return mois_id;
    }

    public void setMois_id(Integer mois_id) {
        this.mois_id = mois_id;
    }
}
