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

import com.example.samafacture.Models.User;
import com.example.samafacture.SqLiteDatabase.BdSamaFacture;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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

        setContentView(R.layout.activity_main);
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        btnConnect = findViewById(R.id.btnConnect);
        btnSignIn = findViewById(R.id.btnSignIn);

        List<User> listUser =  bdSamaFacture.ListUser();
        for (int i=0;i<listUser.size();i++){
            System.out.println(listUser.get(i).getId());
            System.out.println(listUser.get(i).getNom());
            System.out.println(listUser.get(i).getPrenom());
            System.out.println(listUser.get(i).getEmail());
            System.out.println(listUser.get(i).getLogin());
            System.out.println(listUser.get(i).getPassword());
            System.out.println(listUser.get(i).getUser_id());
        }
        //boolean b = bdSamaFacture.checkUserExist();
        //System.out.println("Nombre User");
        //System.out.println(b);


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
                                System.out.println("nom");
                                System.out.println(user.getNom());
                                System.out.println("Prenom");
                                System.out.println(user.getPrenom());
                                System.out.println("user_id");
                                System.out.println(user.getUser_id());
                                boolean check =bdSamaFacture.checkUserExist();
                                if(check == true){
                                    bdSamaFacture.updateUser(1,user.getNom(),user.getPrenom(),user.getEmail(),user.getLogin(),user.getPassword(),user.getId());
                                }else {
                                    bdSamaFacture.createUser(user.getNom(),user.getPrenom(),user.getEmail(),user.getLogin(),user.getPassword(),user.getId());
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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

}
