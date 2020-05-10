package com.univpm.bartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Inserimento extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    EditText nomeProdotto, input_descrizione, input_prezzo;
    Button inviodati;
    Oggetto oggetto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento);

        nomeProdotto = findViewById(R.id.nome_prodotto);
        input_descrizione = findViewById(R.id.descrizione);
        input_prezzo = findViewById(R.id.input_prezzo);
        inviodati = findViewById(R.id.btn_invio_prodoto);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        oggetto= new Oggetto();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        inviodati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String nome = nomeProdotto.getText().toString();
                    final int prezzo = Integer.parseInt(input_prezzo.getText().toString());
                    final String nome_venditore = currentUser.getDisplayName();
                    //final Timestamp data = Timestamp.now();
                    final String descrizione = input_descrizione.getText().toString();

                    oggetto.setPrezzo(prezzo);
                    oggetto.setNomeVenditore(nome_venditore);
                    oggetto.setNome(nome);
                    oggetto.setDescrizione(descrizione);
                    //oggetto.setData(data);
                    databaseReference.push().setValue(oggetto);
                    Toast.makeText(Inserimento.this, "Oggetto inserito", Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(Inserimento.this, HomeScreen.class);
                    startActivity(intent);

        } catch (NullPointerException e) {
            Toast.makeText(Inserimento.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e){
            Toast.makeText(Inserimento.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
        }
            }
        });
    }

    /*protected void invioDati () {
        try {
            final String nome = nomeProdotto.getText().toString();
            final int prezzo1 = Integer.parseInt(prezzo.getText().toString());
            final String nomeVenditore = currentUser.getDisplayName();
            final Timestamp data = Timestamp.now();
            final String descrizione1 = descrizione.getText().toString();

            oggetto.setPrezzo(prezzo1);
            oggetto.setNomeVenditore(nomeVenditore);
            oggetto.setNome(nome);
            oggetto.setDescrizione(descrizione1);
            oggetto.setData(data);
            databaseReference.push().setValue(oggetto);
            /*databaseReference.child("nome").setValue(nome);
            databaseReference.child("prezzo").setValue(prezzo1);
            databaseReference.child("descrizione").setValue(descrizione1);
            databaseReference.child("nome_venditore").setValue(nomeVenditore);
            databaseReference.child("data").setValue(data);

        } catch (NullPointerException e) {
            Toast.makeText(Inserimento.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e){
            Toast.makeText(Inserimento.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        invioDati();
    }*/
}
