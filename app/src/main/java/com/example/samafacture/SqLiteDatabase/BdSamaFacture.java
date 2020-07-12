package com.example.samafacture.SqLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.samafacture.Models.Facture;
import com.example.samafacture.Models.User;

import java.util.ArrayList;
import java.util.List;

public class BdSamaFacture extends SQLiteOpenHelper {

    public BdSamaFacture(@Nullable Context context) {
        super(context, "samafacture.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT,nom TEXT,prenom TEXT,email TEXT,login VARCHAR(30),password VARCHAR(20),user_id INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS facture (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,datePaiement TEXT,montant TEXT,etat TEXT,user_id INTEGER,fournisseur TEXT,typepaiement TEXT,annee TEXT,mois TEXT,LocalState TEXT,SyncOnLine TEXT,idFacture INTEGER UNIQUE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS annee (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,etat TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS factureTemp (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,datePaiement TEXT,montant TEXT,etat TEXT,user_id INTEGER,fournisseur TEXT,typepaiement TEXT,annee TEXT,mois TEXT,LocalState TEXT,SyncOnLine TEXT,idFacture INTEGER UNIQUE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean createUser(String nom,String prenom,String email,String login, String password, int user_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("nom", nom);
            cv.put("prenom", prenom);
            cv.put("email", email);
            cv.put("login", login);
            cv.put("password", password);
            cv.put("user_id", user_id);
            db.insert("user", null,cv);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateUser(int id,String nom,String prenom,String email,String login, String password, int user_id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("nom", nom);
            cv.put("prenom", prenom);
            cv.put("email", email);
            cv.put("login", login);
            cv.put("password", password);
            cv.put("user_id", user_id);
            db.update("user", cv, "id='" + id + "'", null);
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkUserExist(){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean babs = false;
        try {
            Cursor c = db.query("user",null,null,null, null,null, null);
            if (c!=null && c.getCount() > 0){
                babs = true;
            }
            return babs;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public List<User> ListUser(){
        List<User> listUsers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query("user",null,null,null, null,null, null);
            if (c!=null && c.getCount()>0){
                //Ici on déplace le cursor au premier élément car on ne sait pas là ou il pointe
                c.moveToFirst();
                do {
                    User user = new User();
                    user.setId(c.getInt(c.getColumnIndex("id")));
                    user.setNom(c.getString(c.getColumnIndex("nom")));
                    user.setPrenom(c.getString(c.getColumnIndex("prenom")));
                    user.setEmail(c.getString(c.getColumnIndex("email")));
                    user.setLogin(c.getString(c.getColumnIndex("login")));
                    user.setPassword(c.getString(c.getColumnIndex("password")));
                    user.setUser_id(c.getInt(c.getColumnIndex("user_id")));
                    listUsers.add(user);
                    c.moveToNext();
                }while (!c.isAfterLast() );

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listUsers;
    }

    /**
     *
     * Table Facture
     */
    public boolean createFacture(String libelle,String datePaiement,String montant,String etat,int user_id,String fournisseur,String typepaiement,String annee,String mois,String LocalState,String SyncOnLine,int idFacture){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("libelle", libelle);
            cv.put("datePaiement", datePaiement);
            cv.put("montant", montant);
            cv.put("etat", etat);
            cv.put("user_id", user_id);
            cv.put("fournisseur", fournisseur);
            cv.put("typepaiement", typepaiement);
            cv.put("annee", annee);
            cv.put("mois", mois);
            cv.put("LocalState", LocalState);
            cv.put("SyncOnLine", SyncOnLine);
            cv.put("idFacture", idFacture);
            db.insert("facture", null,cv);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateFacture(int id,String libelle,String datePaiement,String montant,String etat,int user_id,String fournisseur,String typepaiement,String annee,String mois,String LocalState,String SyncOnLine,int idFacture){
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("libelle", libelle);
                cv.put("datePaiement", datePaiement);
                cv.put("montant", montant);
                cv.put("etat", etat);
                cv.put("user_id", user_id);
                cv.put("fournisseur", fournisseur);
                cv.put("typepaiement", typepaiement);
                cv.put("annee", annee);
                cv.put("mois", mois);
                cv.put("LocalState", LocalState);
                cv.put("SyncOnLine", SyncOnLine);
                cv.put("idFacture", idFacture);
                db.update("facture", cv, "id='"+id+"'",null);
                db.close();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
    }

    public boolean deleteFacture(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("facture", null,null);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Facture> getFactures(){
        List<Facture> listFactures = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query("facture",null,null,null, null,null, null);
            if (c!=null && c.getCount() > 0) {
                c.moveToFirst();
                do{
                    Facture facture = new Facture();
                    facture.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                    facture.setLibelle(c.getString(c.getColumnIndexOrThrow("libelle")));
                    facture.setDatePaiement(c.getString(c.getColumnIndexOrThrow("datePaiement")));
                    facture.setMontant(c.getString(c.getColumnIndexOrThrow("montant")));
                    facture.setEtat(c.getString(c.getColumnIndexOrThrow("etat")));
                    facture.setUser_id(c.getInt(c.getColumnIndexOrThrow("user_id")));
                    facture.setFournisseur(c.getString(c.getColumnIndexOrThrow("fournisseur")));
                    facture.setTypepaiement(c.getString(c.getColumnIndexOrThrow("typepaiement")));
                    facture.setAnnee(c.getString(c.getColumnIndexOrThrow("annee")));
                    facture.setMois(c.getString(c.getColumnIndexOrThrow("mois")));
                    facture.setLocalState(c.getString(c.getColumnIndexOrThrow("LocalState")));
                    facture.setSyncOnLine(c.getString(c.getColumnIndexOrThrow("SyncOnLine")));
                    facture.setIdFacture(c.getInt(c.getColumnIndexOrThrow("idFacture")));
                    listFactures.add(facture);
                    c.moveToNext();
                }while (!c.isAfterLast());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listFactures;
    }

    public List<Facture> getFacturesPayer(){
        List<Facture> listFacturePayer = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query("facture",null,"LocalState = 'payer' AND SyncOnLine = 'nonOk'",null, null,null, null);
            if (c!=null && c.getCount() > 0){
                c.moveToFirst();
                do{
                    Facture facture = new Facture();
                    facture.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                    facture.setLibelle(c.getString(c.getColumnIndexOrThrow("libelle")));
                    facture.setDatePaiement(c.getString(c.getColumnIndexOrThrow("datePaiement")));
                    facture.setMontant(c.getString(c.getColumnIndexOrThrow("montant")));
                    facture.setEtat(c.getString(c.getColumnIndexOrThrow("etat")));
                    facture.setUser_id(c.getInt(c.getColumnIndexOrThrow("user_id")));
                    facture.setFournisseur(c.getString(c.getColumnIndexOrThrow("fournisseur")));
                    facture.setTypepaiement(c.getString(c.getColumnIndexOrThrow("typepaiement")));
                    facture.setAnnee(c.getString(c.getColumnIndexOrThrow("annee")));
                    facture.setMois(c.getString(c.getColumnIndexOrThrow("mois")));
                    facture.setLocalState(c.getString(c.getColumnIndexOrThrow("LocalState")));
                    facture.setSyncOnLine(c.getString(c.getColumnIndexOrThrow("SyncOnLine")));
                    facture.setIdFacture(c.getInt(c.getColumnIndexOrThrow("idFacture")));
                    listFacturePayer.add(facture);
                    c.moveToNext();
                }while (!c.isAfterLast());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listFacturePayer;
    }

    public List<Facture> getFacturesNonPayer(){
        List<Facture> listFactureNonPayer = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query("facture",null,"LocalState = 'non-payer'",null, null,null, null);
            if (c!=null && c.getCount() > 0){
                c.moveToFirst();
                do{
                    Facture facture = new Facture();
                    facture.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                    facture.setLibelle(c.getString(c.getColumnIndexOrThrow("libelle")));
                    facture.setDatePaiement(c.getString(c.getColumnIndexOrThrow("datePaiement")));
                    facture.setMontant(c.getString(c.getColumnIndexOrThrow("montant")));
                    facture.setEtat(c.getString(c.getColumnIndexOrThrow("etat")));
                    facture.setUser_id(c.getInt(c.getColumnIndexOrThrow("user_id")));
                    facture.setFournisseur(c.getString(c.getColumnIndexOrThrow("fournisseur")));
                    facture.setTypepaiement(c.getString(c.getColumnIndexOrThrow("typepaiement")));
                    facture.setAnnee(c.getString(c.getColumnIndexOrThrow("annee")));
                    facture.setMois(c.getString(c.getColumnIndexOrThrow("mois")));
                    facture.setLocalState(c.getString(c.getColumnIndexOrThrow("LocalState")));
                    facture.setSyncOnLine(c.getString(c.getColumnIndexOrThrow("SyncOnLine")));
                    facture.setIdFacture(c.getInt(c.getColumnIndexOrThrow("idFacture")));
                    listFactureNonPayer.add(facture);
                    c.moveToNext();
                }while (!c.isAfterLast());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listFactureNonPayer;
    }
    /**
     * Facture Tampon
     */
    public boolean createFactureTemp(String libelle,String datePaiement,String montant,String etat,int user_id,String fournisseur,String typepaiement,String annee,String mois,String LocalState,String SyncOnLine,int idFacture){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("libelle", libelle);
            cv.put("datePaiement", datePaiement);
            cv.put("montant", montant);
            cv.put("etat", etat);
            cv.put("user_id", user_id);
            cv.put("fournisseur", fournisseur);
            cv.put("typepaiement", typepaiement);
            cv.put("annee", annee);
            cv.put("mois", mois);
            cv.put("LocalState", LocalState);
            cv.put("SyncOnLine", SyncOnLine);
            cv.put("idFacture", idFacture);
            db.insert("factureTemp", null,cv);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteFactureTemp(int user_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("factureTemp", "user_id='"+user_id+"'",null);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Facture> getFacturesTempUser(int user_id){
        List<Facture> listFacturePayer = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query("factureTemp",null,"user_id='"+user_id+"'",null, null,null, null);
            if (c!=null && c.getCount() > 0){
                c.moveToFirst();
                do{
                    Facture facture = new Facture();
                    facture.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                    facture.setLibelle(c.getString(c.getColumnIndexOrThrow("libelle")));
                    facture.setDatePaiement(c.getString(c.getColumnIndexOrThrow("datePaiement")));
                    facture.setMontant(c.getString(c.getColumnIndexOrThrow("montant")));
                    facture.setEtat(c.getString(c.getColumnIndexOrThrow("etat")));
                    facture.setUser_id(c.getInt(c.getColumnIndexOrThrow("user_id")));
                    facture.setFournisseur(c.getString(c.getColumnIndexOrThrow("fournisseur")));
                    facture.setTypepaiement(c.getString(c.getColumnIndexOrThrow("typepaiement")));
                    facture.setAnnee(c.getString(c.getColumnIndexOrThrow("annee")));
                    facture.setMois(c.getString(c.getColumnIndexOrThrow("mois")));
                    facture.setLocalState(c.getString(c.getColumnIndexOrThrow("LocalState")));
                    facture.setSyncOnLine(c.getString(c.getColumnIndexOrThrow("SyncOnLine")));
                    facture.setIdFacture(c.getInt(c.getColumnIndexOrThrow("idFacture")));
                    listFacturePayer.add(facture);
                    c.moveToNext();
                }while (!c.isAfterLast());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listFacturePayer;
    }


}
