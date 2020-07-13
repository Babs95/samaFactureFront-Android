package com.example.samafacture.SqLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.samafacture.Models.Facture;
import com.example.samafacture.Models.Fournisseur;
import com.example.samafacture.Models.Mois;
import com.example.samafacture.Models.Typepaiment;
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
        db.execSQL("CREATE TABLE IF NOT EXISTS facture (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,datePaiement TEXT,montant TEXT,etat TEXT,user_id INTEGER,fournisseur TEXT,typepaiement TEXT,annee TEXT,mois TEXT,LocalState TEXT,SyncOnLine TEXT,idFacture INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS annee (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,etat TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS factureTemp (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,datePaiement TEXT,montant TEXT,etat TEXT,user_id INTEGER,fournisseur TEXT,typepaiement TEXT,annee TEXT,mois TEXT,LocalState TEXT,SyncOnLine TEXT,idFacture INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS fournisseur (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,fournisseur_id INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS mois (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,mois_id INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS typepaiement (id INTEGER PRIMARY KEY AUTOINCREMENT,libelle TEXT,typepaiement_id INTEGER);");
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
     * End Table User
     */

    /**
     * Table Fournisseur
     */
    public boolean createFournisseur(String libelle,Integer fournisseur_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("libelle", libelle);
            cv.put("fournisseur_id", fournisseur_id);
            db.insert("fournisseur", null,cv);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateFournisseur(Integer fournisseur_id,String libelle) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("libelle", libelle);
            db.update("fournisseur", cv, "fournisseur_id='" + fournisseur_id + "'", null);
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Fournisseur> ListFournisseur(){
        List<Fournisseur> listFournisseurs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query("fournisseur",null,null,null, null,null, null);
            if (c!=null && c.getCount()>0){
                //Ici on déplace le cursor au premier élément car on ne sait pas là ou il pointe
                c.moveToFirst();
                do {
                    Fournisseur fournisseur = new Fournisseur();
                    fournisseur.setId(c.getInt(c.getColumnIndex("id")));
                    fournisseur.setLibelle(c.getString(c.getColumnIndex("libelle")));
                    fournisseur.setFournisseur_id(c.getInt(c.getColumnIndex("fournisseur_id")));
                    listFournisseurs.add(fournisseur);
                    c.moveToNext();
                }while (!c.isAfterLast() );

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listFournisseurs;
    }

    public boolean deleteFournisseur(Integer fournisseur_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("fournisseur", "fournisseur_id='"+fournisseur_id+"'",null);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllFournisseur(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("fournisseur", null,null);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * End Table Fournisseur
     */


    /**
     * Table Mois
     */
    public boolean createMois(String libelle,Integer mois_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("libelle", libelle);
            cv.put("mois_id", mois_id);
            db.insert("mois", null,cv);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateMois(Integer mois_id,String libelle) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("libelle", libelle);
            db.update("mois", cv, "mois_id='" + mois_id + "'", null);
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Mois> ListMois(){
        List<Mois> listMois = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query("mois",null,null,null, null,null, null);
            if (c!=null && c.getCount()>0){
                //Ici on déplace le cursor au premier élément car on ne sait pas là ou il pointe
                c.moveToFirst();
                do {
                    Mois mois = new Mois();
                    mois.setId(c.getInt(c.getColumnIndex("id")));
                    mois.setLibelle(c.getString(c.getColumnIndex("libelle")));
                    mois.setMois_id(c.getInt(c.getColumnIndex("mois_id")));
                    listMois.add(mois);
                    c.moveToNext();
                }while (!c.isAfterLast() );

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listMois;
    }

    public boolean deleteMois(Integer mois_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("mois", "mois_id='"+mois_id+"'",null);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllMois(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("mois", null,null);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * End Table Mois
     */

    /**
     * Table Type Paiement
     */
    public boolean createTypePaiement(String libelle,Integer typepaiement_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("libelle", libelle);
            cv.put("typepaiement_id", typepaiement_id);
            db.insert("typepaiement", null,cv);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateTypePaiement(Integer typepaiement_id,String libelle) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("libelle", libelle);
            db.update("typepaiement", cv, "typepaiement_id='" + typepaiement_id + "'", null);
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Typepaiment> ListTypePaiement(){
        List<Typepaiment> listTypepaiement = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query("typepaiement",null,null,null, null,null, null);
            if (c!=null && c.getCount()>0){
                //Ici on déplace le cursor au premier élément car on ne sait pas là ou il pointe
                c.moveToFirst();
                do {
                    Typepaiment typepaiment = new Typepaiment();
                    typepaiment.setId(c.getInt(c.getColumnIndex("id")));
                    typepaiment.setLibelle(c.getString(c.getColumnIndex("libelle")));
                    typepaiment.setTypepaiement_id(c.getInt(c.getColumnIndex("typepaiement_id")));
                    listTypepaiement.add(typepaiment);
                    c.moveToNext();
                }while (!c.isAfterLast() );

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listTypepaiement;
    }

    public boolean deleteTypePaiement(Integer typepaiement_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("typepaiement", "typepaiement_id='"+typepaiement_id+"'",null);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllTypePaiement(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("typepaiement", null,null);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * End Table Type Paiement
     */

    /**
     *
     * Table Facture
     */
    public boolean createFacture(String libelle, String datePaiement, String montant, String etat, int user_id, String fournisseur, String typepaiement, String annee, String mois, String LocalState, String SyncOnLine, Integer idFacture){
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
    public boolean updateFacture(int id,String etat,String typepaiement,String LocalState,String SyncOnLine){
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("etat", etat);
                cv.put("typepaiement", typepaiement);
                cv.put("LocalState", LocalState);
                cv.put("SyncOnLine", SyncOnLine);
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
     * End Table Facture
     */


    /**
     * Table Facture Tampon
     */
    public boolean createFactureTemp(String libelle,String datePaiement,String montant,String etat,int user_id,String fournisseur,String typepaiement,String annee,String mois,String LocalState,String SyncOnLine,Integer idFacture){
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
