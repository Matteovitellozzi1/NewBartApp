package com.univpm.bartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //Schermata iniziale
    Button bottone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottone = (Button) findViewById(R.id.button2);
    }

    public void SchermataLogin(View view){
            Intent intent = new Intent(this, Login_Screen.class);
            startActivity(intent);
    }
}