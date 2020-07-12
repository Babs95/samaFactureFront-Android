package com.example.samafacture;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.samafacture.Models.Annee;
import com.example.samafacture.Models.Facture;
import com.example.samafacture.Models.Fournisseur;
import com.example.samafacture.Models.Mois;
import com.example.samafacture.Models.Typepaiment;
import com.example.samafacture.Models.User;
import com.example.samafacture.SqLiteDatabase.BdSamaFacture;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText txtLogin, txtPassword;
    private Button btnConnect, btnSignIn;
    private  String login, password;
    ProgressBar progressBar;
    Wave wave2;
    private BdSamaFacture bdSamaFacture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creation de la base de données
        bdSamaFacture = new BdSamaFacture(this);
        //loadFactureOnSqliteDatabase();

        setContentView(R.layout.activity_main);
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        btnConnect = findViewById(R.id.btnConnect);
        btnSignIn = findViewById(R.id.btnSignIn);

        givenUsingTimer__whenSchedulingRepeatedTask__thenCorrect();
        //Progessbar
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setVisibility(View.GONE);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = txtLogin.getText().toString().trim();
                password = txtPassword.getText().toString();

                if(login.isEmpty() || password.isEmpty()) {
                    String error = getString(R.string.error_fields);
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                } else {
                    //progressBar.setVisibility(View.INVISIBLE);
                    authentification(login, password);
                    /*String logn = "Bbasb";
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("ExtraLogin", logn);
                    startActivity(intent);*/
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InscriptionActivity.class);
                startActivity(intent);
            }
        });
    }
    public void givenUsingTimer__whenSchedulingRepeatedTask__thenCorrect(){
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                System.out.println("Task performed on " + new Date());
            }
        };
        Timer timer = new Timer("Timer");

        long delay  = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    public void  authentification(String login, String password) {
        //new YourAsyncTask().execute();
        //if (login!=null) {

        //}

        progressBar.setVisibility(View.VISIBLE);
        //wave2.start();
        try {
            String url ="https://api-samafacture.herokuapp.com/api/user/GetLogin/"+login+"/"+password;
            OkHttpClient  client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //if (progressBar.getVisibility() == View.VISIBLE) {
                                progressBar.setVisibility(View.GONE);
                            //}
                            String error = getString(R.string.error_connection);
                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int idBabs = 0;
                    try {
                        JSONObject jo = new JSONObject(response.body().string());
                        String status = jo.getString("status");

                        if(status.equalsIgnoreCase("KO")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    //wave2.stop();
                                    String message =getString(R.string.error_parameters);
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            JSONArray jsonArray=jo.getJSONArray("user");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                Gson gson=new Gson();
                                User user = gson.fromJson(jsonObject1.toString(), User.class);
                                System.out.println("id");
                                System.out.println(user.getId());
                                idBabs = user.getId();
                                System.out.println("nom");
                                System.out.println(user.getNom());
                                System.out.println("Prenom");
                                System.out.println(user.getPrenom());
                                System.out.println("user_id");
                                System.out.println(user.getUser_id());
                                boolean check =bdSamaFacture.checkUserExist();
                                if(check == true){
                                    boolean currentUser = false;
                                    bdSamaFacture.updateUser(1,user.getNom(),user.getPrenom(),user.getEmail(),user.getLogin(),user.getPassword(),user.getId());
                                    List<Facture> LisFact =  bdSamaFacture.getFactures();
                                    for (int j=0;j<LisFact.size();j++){
                                        if(LisFact.get(j).getUser_id() == idBabs){
                                            currentUser = true;
                                            break;
                                        }else {
                                            currentUser = false;
                                        }

                                    }
                                    if(currentUser == true){
                                        System.out.println("Current = true");
                                        //Dans ce for on garde les factures non-payer du utilisateur toujours connecté
                                        for (int p=0;p<LisFact.size();p++){
                                            if(LisFact.get(p).getEtat().equalsIgnoreCase("non-payer")){
                                                bdSamaFacture.createFactureTemp(LisFact.get(p).getLibelle(),LisFact.get(p).getDatePaiement(),LisFact.get(p).getMontant(),LisFact.get(p).getEtat(),LisFact.get(p).getUser_id(),LisFact.get(p).getFournisseur(),LisFact.get(p).getTypepaiement(),LisFact.get(p).getAnnee(),LisFact.get(p).getMois(),LisFact.get(p).getLocalState(),LisFact.get(p).getSyncOnLine(),LisFact.get(p).getIdFacture());
                                            }
                                        }
                                        bdSamaFacture.deleteFacture();

                                        List<Facture> LisFactTemp =  bdSamaFacture.getFacturesTempUser(idBabs);
                                        if(LisFactTemp != null){
                                            for (int k=0;k<LisFactTemp.size();k++){
                                                    System.out.println("Facture Non payer UserCurrent");
                                                    System.out.println(LisFactTemp.get(k).getId());
                                                    System.out.println(LisFactTemp.get(k).getLibelle());
                                                    bdSamaFacture.createFacture(LisFactTemp.get(k).getLibelle(),LisFactTemp.get(k).getDatePaiement(),LisFactTemp.get(k).getMontant(),LisFactTemp.get(k).getEtat(),LisFactTemp.get(k).getUser_id(),LisFactTemp.get(k).getFournisseur(),LisFactTemp.get(k).getTypepaiement(),LisFactTemp.get(k).getAnnee(),LisFactTemp.get(k).getMois(),LisFactTemp.get(k).getLocalState(),LisFactTemp.get(k).getSyncOnLine(),LisFactTemp.get(k).getIdFacture());

                                            }
                                            bdSamaFacture.deleteFactureTemp(idBabs);
                                        }
                                    }else {
                                        System.out.println("Current = false");
                                        //Dans ce for on garde les factures non-payer du dernier utilisateur connecté
                                        for (int l=0;l<LisFact.size();l++){
                                            if(LisFact.get(l).getEtat().equalsIgnoreCase("non-payer")){
                                                bdSamaFacture.createFactureTemp(LisFact.get(l).getLibelle(),LisFact.get(l).getDatePaiement(),LisFact.get(l).getMontant(),LisFact.get(l).getEtat(),LisFact.get(l).getUser_id(),LisFact.get(l).getFournisseur(),LisFact.get(l).getTypepaiement(),LisFact.get(l).getAnnee(),LisFact.get(l).getMois(),LisFact.get(l).getLocalState(),LisFact.get(l).getSyncOnLine(),LisFact.get(l).getIdFacture());
                                            }
                                        }
                                        bdSamaFacture.deleteFacture();
                                        //Dans ce for on charge les factures non payer du nouveau utilisateur connecté
                                        List<Facture> LisFactTemp2 =  bdSamaFacture.getFacturesTempUser(idBabs);
                                        if(LisFactTemp2 != null){
                                            for (int m=0;m<LisFactTemp2.size();m++){
                                                System.out.println("Facture Non payer Nouveau User");
                                                System.out.println(LisFactTemp2.get(m).getId());
                                                System.out.println(LisFactTemp2.get(m).getLibelle());
                                                bdSamaFacture.createFacture(LisFactTemp2.get(m).getLibelle(),LisFactTemp2.get(m).getDatePaiement(),LisFactTemp2.get(m).getMontant(),LisFactTemp2.get(m).getEtat(),LisFactTemp2.get(m).getUser_id(),LisFactTemp2.get(m).getFournisseur(),LisFactTemp2.get(m).getTypepaiement(),LisFactTemp2.get(m).getAnnee(),LisFactTemp2.get(m).getMois(),LisFactTemp2.get(m).getLocalState(),LisFactTemp2.get(m).getSyncOnLine(),LisFactTemp2.get(m).getIdFacture());

                                            }
                                            bdSamaFacture.deleteFactureTemp(idBabs);
                                        }
                                    }
                                }else {
                                    bdSamaFacture.createUser(user.getNom(),user.getPrenom(),user.getEmail(),user.getLogin(),user.getPassword(),user.getId());
                                }
                            }
                            final int idBabs2 = idBabs;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadFactureOnSqliteDatabase(idBabs2);
                                    progressBar.setVisibility(View.GONE);
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                    dialog.setIcon(R.mipmap.ic_launcher);
                                    dialog.setTitle("Connexion réussie");
                                    dialog.setMessage("Bienvenue dans Sama Facture!!");
                                    dialog.setPositiveButton("Continuez", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    dialog.show();
                                }
                            });
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadFactureOnSqliteDatabase(final int id) {
        //progressBar.setVisibility(View.VISIBLE);
        String url = "https://api-samafacture.herokuapp.com/api/facture/getall";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        String error = getString(R.string.error_connection);
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //ListFacture.clear();
                try{
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    if(jsonObject.getInt("success")==1){
                        JSONArray jsonArray=jsonObject.getJSONArray("facture");
                        Facture facture = new Facture();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            if(jsonObject1.getInt("user_id") == id){
                                facture.setIdFacture(jsonObject1.getInt("id"));
                                facture.setLibelle(jsonObject1.getString("libelle"));
                                facture.setDatePaiement(jsonObject1.getString("datePaiement"));
                                facture.setMontant(jsonObject1.getString("montant"));
                                facture.setEtat(jsonObject1.getString("etat"));
                                facture.setUser_id(jsonObject1.getInt("user_id"));
                                Gson gson = new Gson();
                                Fournisseur fournisseur = gson.fromJson(String.valueOf(jsonObject1.getJSONObject("fournisseur")), Fournisseur.class);

                                Gson gson2 = new Gson();
                                Mois mois = gson2.fromJson(String.valueOf(jsonObject1.getJSONObject("mois")), Mois.class);

                                Gson gson3 = new Gson();
                                Annee annee = gson3 .fromJson(String.valueOf(jsonObject1.getJSONObject("annee")), Annee.class);

                                Gson gson4 = new Gson();
                                Typepaiment typepaiment = gson4.fromJson(String.valueOf(jsonObject1.getJSONObject("typepaiement")), Typepaiment.class);
                                if(facture.getEtat().equalsIgnoreCase("payer")){
                                    bdSamaFacture.createFacture(facture.getLibelle(),facture.getDatePaiement(),facture.getMontant(),facture.getEtat(),facture.getUser_id(),fournisseur.getLibelle(),typepaiment.getLibelle(),annee.getLibelle(),mois.getLibelle(),facture.getEtat(),"Ok",facture.getIdFacture());
                                }

                            }


                        }
                        //ListAnneRecyclerView.notify();
                        /*System.out.println(ListAnnee);
                        for (Annee typ: ListAnnee){
                            System.out.println(typ.getId()+typ.getLibelle()+typ.getEtat());
                        }*/

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //progressBar.setVisibility(View.GONE);
                            //initRecyclerView();
                        }
                    });
                }catch (JSONException e){e.printStackTrace();}
            }
        });
    }

}
