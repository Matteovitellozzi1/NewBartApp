package com.univpm.bartapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VisualizzaProdotto extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView nomeOggetto, nomeVenditore, prezzoOggetto, nomeOggettoOfferta, differenzaPrezzo;
    private Button btnOfferta;
    private FirebaseUser currentUser;
    private String prezzoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_prodotto);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dettagli");
        actionBar.setDisplayHomeAsUpEnabled(true);


        nomeOggetto = findViewById(R.id.nome_oggetto1);
        nomeVenditore = findViewById(R.id.nome_venditore1);
        prezzoOggetto = findViewById(R.id.prezzo1);
        nomeOggettoOfferta = findViewById(R.id.prodotto_offerta);
        btnOfferta = (Button) findViewById(R.id.btn_offerta);
        differenzaPrezzo = findViewById(R.id.diff_prezzo);

        final String nome = getIntent().getStringExtra("Nome1");
        nomeOggetto.setText(nome);
        final String nomeVend = getIntent().getStringExtra("NomeVend");
        nomeVenditore.setText(nomeVend);
        final String prezzo = getIntent().getStringExtra("Prezzo1");
        prezzoOggetto.setText(prezzo);
        final String idUser = getIntent().getStringExtra("idUser");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String utente = mAuth.getUid();
        if (idUser.equals(utente)) {
            btnOfferta.setVisibility(View.INVISIBLE);
        } else {
            btnOfferta.setVisibility(View.VISIBLE);
        }
        int prezzoOgg = Integer.parseInt(prezzo);
        nomeOggettoOfferta.setText(getIntent().getStringExtra("nome_offerta"));
        differenzaPrezzo.setText(prezzoff);
        String prezoooooo = getIntent().getStringExtra("prezzo_offerta");
        prezzoff = String.valueOf(Integer.parseInt(prezoooooo) - prezzoOgg);
        btnOfferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizzaProdotto.this, SceltaProdotto.class);
                intent.putExtra("prezzo", prezzo );
                intent.putExtra("nome", nome);
                intent.putExtra("nomevend", nomeVend );
                intent.putExtra("idUser", idUser);
                startActivity(intent);
            }
        });





    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        this.finish();
        return true;
    }

}
