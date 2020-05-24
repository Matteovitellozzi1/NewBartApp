package com.univpm.bartapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VisualizzaProdotto extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView textView1, textView2, textView3;
    private Button btnOfferta;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_prodotto);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dettagli");
        actionBar.setDisplayHomeAsUpEnabled(true);


        textView1 = findViewById(R.id.nome_oggetto1);
        textView2 = findViewById(R.id.nome_venditore1);
        textView3 = findViewById(R.id.prezzo1);
        btnOfferta = (Button) findViewById(R.id.btn_offerta);

        String nome = getIntent().getStringExtra("Nome1");
        textView1.setText(nome);
        String nomeVend = getIntent().getStringExtra("NomeVend");
        textView2.setText(nomeVend);
        String prezzo = getIntent().getStringExtra("Prezzo1");
        textView3.setText(prezzo);
        String idUser = getIntent().getStringExtra("idUser");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String utente = mAuth.getUid();
        if (idUser.equals(utente)) {
            btnOfferta.setVisibility(View.INVISIBLE);
        } else {
            btnOfferta.setVisibility(View.VISIBLE);
        }

        btnOfferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        this.finish();
        return true;
    }
}
