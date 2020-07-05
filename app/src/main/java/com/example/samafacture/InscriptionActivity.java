package com.example.samafacture;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InscriptionActivity extends AppCompatActivity {

    private TextInputLayout txtInputNom, txtInputPrenom, txtInputEmail, txtInputLogin, txtInputPassword;
    private Button btnSaveUser;
    private String nomInput, prenomInput, emailInput,  loginInput, passwordInput;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Wave wave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        txtInputNom = findViewById(R.id.txt_input_nom);
        txtInputPrenom = findViewById(R.id.txt_input_prenom);
        txtInputEmail = findViewById(R.id.txt_input_email);
        txtInputLogin = findViewById(R.id.txt_input_login);
        txtInputPassword = findViewById(R.id.txt_input_password);
        btnSaveUser = findViewById(R.id.btnSaveUser);

        //Button Loading effect
        wave = new Wave();
        wave.setBounds(0, 0, 100, 100);
        wave.setColor(getResources().getColor(R.color.colorAccent));
        btnSaveUser.setCompoundDrawables(wave, null, null, null);

        btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateNom() | !validatePrenom() | !validateEmail() | !validateLogin()
                   | !validatePassword()) {
                    return;
                }else {
                    wave.start();
                    String postBody="{\n" +
                            "    \"nom\": \""+nomInput+"\",\n" +
                            "    \"prenom\": \""+prenomInput+"\",\n" +
                            "    \"email\": \""+emailInput+"\",\n" +
                            "    \"login\": \""+loginInput+"\",\n" +
                            "    \"password\": \""+passwordInput+"\"\n" +
                            "}";
                    System.out.println(postBody);
                    postUser(postBody);
                }
            }
        });
    }



    private boolean validateEmail() {
         emailInput = txtInputEmail.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()) {
            txtInputEmail.setError(getString(R.string.error_fields));
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            txtInputEmail.setError(getString(R.string.error_email_format));
            return false;
        } else {
            txtInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateLogin() {
         loginInput = txtInputLogin.getEditText().getText().toString().trim();
        if(loginInput.isEmpty()) {
            txtInputLogin.setError(getString(R.string.error_fields));
            return false;
        } else if(loginInput.length() > 20) {
            txtInputLogin.setError(getString(R.string.error_login_length));
            return false;
        }else {
            txtInputLogin.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
         passwordInput = txtInputPassword.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()) {
            txtInputPassword.setError(getString(R.string.error_fields));
            return false;
        } else {
            txtInputPassword.setError(null);
            return true;
        }
    }

    private boolean validateNom() {
         nomInput = txtInputNom.getEditText().getText().toString().trim();
        if(nomInput.isEmpty()) {
            txtInputNom.setError(getString(R.string.error_fields));
            return false;
        } else {
            txtInputNom.setError(null);
            return true;
        }
    }

    private boolean validatePrenom() {
         prenomInput = txtInputPrenom.getEditText().getText().toString().trim();
        if(prenomInput.isEmpty()) {
            txtInputPrenom.setError(getString(R.string.error_fields));
            return false;
        } else {
            txtInputPrenom.setError(null);
            return true;
        }
    }

    public void postUser(String postBody) {
        try {

            String url = "https://api-samafacture.herokuapp.com/api/user/store";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, postBody);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String error = getString(R.string.error_connection);
                            Toast.makeText(InscriptionActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jo = new JSONObject(response.body().string());
                        //message2 = response.body().string();
                        final String myResponse = response.body().string();
                        if(response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(myResponse);
                                    wave.stop();
                                    String message = "Utilisateur crée avec succèss";
                                    Toast.makeText(InscriptionActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    }catch (Exception e){

                    }
                }
            });

        } catch (Exception e){

        }
    }
}
