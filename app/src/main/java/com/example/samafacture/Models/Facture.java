package com.example.samafacture.Models;

public class Facture {
    private int id;
    private String libelle;
    private String datePaiement;
    private String montant;
    private String etat;
    private int user_id;
    private String fournisseur;
    private String typepaiement;
    private String annee;
    private String mois;
    private String LocalState;
    private String SyncOnLine;
    private int idFacture;

    public Facture() {
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

    public String getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getTypepaiement() {
        return typepaiement;
    }

    public void setTypepaiement(String typepaiement) {
        this.typepaiement = typepaiement;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public String getLocalState() {
        return LocalState;
    }

    public void setLocalState(String localState) {
        LocalState = localState;
    }

    public String getSyncOnLine() {
        return SyncOnLine;
    }

    public void setSyncOnLine(String syncOnLine) {
        SyncOnLine = syncOnLine;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }


}
