package com.univpm.bartapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class VisualizzaProdotto extends AppCompatActivity {

    TextView textView1, textView2, textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_prodotto);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dettagli");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        /*@Override
        public boolean onSupportNavigateUp() {
            onBackPressed();
            return true;
        }*/


        textView1 = findViewById(R.id.nome_oggetto1);
        textView2 = findViewById(R.id.nome_venditore1);
        textView3 = findViewById(R.id.prezzo1);


        String nome = getIntent().getStringExtra("Nome1");
        textView1.setText(nome);
        String nomeVend = getIntent().getStringExtra("NomeVend");
        textView2.setText(nomeVend);
        String prezzo = getIntent().getStringExtra("Prezzo1");
        textView3.setText(prezzo);
    }
}
