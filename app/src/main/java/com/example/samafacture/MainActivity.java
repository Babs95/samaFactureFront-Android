package com.example.samafacture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtLogin, txtPassword;
    private Button btnConnect, btnSignIn;
    private  String login, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        btnConnect = findViewById(R.id.btnConnect);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = txtLogin.getText().toString().trim();
                password = txtPassword.getText().toString();

                if(login.isEmpty() || password.isEmpty()) {
                    String error = getString(R.string.error_fields);
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                } else {
                    //authentification(login, password);
                    String logn = "Bbasb";
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("ExtraLogin", logn);
                    startActivity(intent);
                }
            }
        });
    }
}
