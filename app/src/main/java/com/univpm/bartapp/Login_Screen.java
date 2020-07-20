package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Screen extends AppCompatActivity {

    private Button btnRegistra;
    private FirebaseAuth auth;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);

        auth = FirebaseAuth.getInstance();
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                //Metodo predefinito per effettuare il login su firebase
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(Login_Screen.this, HomeScreen.class);
                            startActivity(intent);
                        }
                        else{
                            task.getException();
                            Toast.makeText(Login_Screen.this, "Email/Password errati!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        btnRegistra= (Button) findViewById(R.id.btn_registra);
        //bottone che chiama un listener per registrarsi
        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Screen.this, Registrazione.class);
                startActivity(intent);
            }
        });
    }
}
