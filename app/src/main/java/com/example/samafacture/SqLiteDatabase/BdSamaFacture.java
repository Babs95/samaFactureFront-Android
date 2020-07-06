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
        db.execSQL("CREATE TABLE IF NOT EXISTS facture (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,montant TEXT,mois TEXT,annee TEXT,modepaiement TEXT,etat TEXT,LocalState TEXT,SyncOnLine TEXT,idFacture INTEGER UNIQUE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS annee (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,etat TEXT);");
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

    public boolean createFacture(String libelle,String montant,String mois,String annee,String modepaiement,String etat,String LocalState,String SyncOnLine,int idFacture){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("libelle", libelle);
            cv.put("montant", montant);
            cv.put("mois", mois);
            cv.put("annee", annee);
            cv.put("modepaiement", modepaiement);
            cv.put("etat", etat);
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
    public boolean updateFacture(int id,String libelle,String montant,String mois,String annee,String modepaiement,String etat,String LocalState,String SyncOnLine,int idFacture){
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("libelle", libelle);
                cv.put("montant", montant);
                cv.put("mois", mois);
                cv.put("annee", annee);
                cv.put("modepaiement", modepaiement);
                cv.put("etat", etat);
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
                do{
                    Facture facture = new Facture();
                    facture.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                    facture.setLibelle(c.getString(c.getColumnIndexOrThrow("libelle")));
                    facture.setMontant(c.getString(c.getColumnIndexOrThrow("montant")));
                    facture.setMois(c.getString(c.getColumnIndexOrThrow("mois")));
                    facture.setAnnee(c.getString(c.getColumnIndexOrThrow("annee")));
                    facture.setModepaiement(c.getString(c.getColumnIndexOrThrow("modepaiement")));
                    facture.setEtat(c.getString(c.getColumnIndexOrThrow("etat")));
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
                do{
                    Facture facture = new Facture();
                    facture.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                    facture.setLibelle(c.getString(c.getColumnIndexOrThrow("libelle")));
                    facture.setMontant(c.getString(c.getColumnIndexOrThrow("montant")));
                    facture.setMois(c.getString(c.getColumnIndexOrThrow("mois")));
                    facture.setAnnee(c.getString(c.getColumnIndexOrThrow("annee")));
                    facture.setModepaiement(c.getString(c.getColumnIndexOrThrow("modepaiement")));
                    facture.setEtat(c.getString(c.getColumnIndexOrThrow("etat")));
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
