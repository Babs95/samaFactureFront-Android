package com.example.samafacture.Models;

public class Fournisseur {
    private int id;
    private String libelle;
    private Integer fournisseur_id;

    public Fournisseur() {
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

    public Integer getFournisseur_id() {
        return fournisseur_id;
    }

    public void setFournisseur_id(Integer fournisseur_id) {
        this.fournisseur_id = fournisseur_id;
    }
}
