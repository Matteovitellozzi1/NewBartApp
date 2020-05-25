package com.univpm.bartapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class VisualizzaProdotto extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView nomeOggetto, nomeVenditore, prezzoOggetto, nomeOggettoOfferta, differenzaPrezzo, introduzione;
    private Button btnOfferta, btnConferma, btnCambia;
    private FirebaseUser currentUser;
    private ImageView immagineOggetto;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_prodotto);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dettagli");
        actionBar.setDisplayHomeAsUpEnabled(true);

        introduzione= findViewById(R.id.header_offerta);
        nomeOggetto = findViewById(R.id.nome_oggetto1);
        nomeVenditore = findViewById(R.id.nome_venditore1);
        prezzoOggetto = findViewById(R.id.prezzo1);
        immagineOggetto = findViewById(R.id.immagine_oggetto1);
        nomeOggettoOfferta = findViewById(R.id.prodotto_offerta);
        btnOfferta = (Button) findViewById(R.id.btn_offerta);
        differenzaPrezzo = findViewById(R.id.diff_prezzo);
        btnConferma= (Button) findViewById(R.id.btn_conferma);
        btnConferma.setVisibility(View.INVISIBLE);
        btnCambia=(Button) findViewById(R.id.btn_cambia);
        btnCambia.setVisibility(View.INVISIBLE);

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

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        storageReference.child("Image").child("ImmaginiOggetti").child(idUser).child(nome).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(immagineOggetto);
            }
        });

        if (idUser.equals(utente)) {
            btnOfferta.setVisibility(View.INVISIBLE);
            //introduzione.setText("è un oggetto da te messo in vendita");
        } else {
            btnOfferta.setVisibility(View.VISIBLE);
        }
        String nome_offerta=getIntent().getStringExtra("nome_offerta");
        String prezzo_offerta=getIntent().getStringExtra("prezzo_offerta");
        if((nome_offerta != null)&&(prezzo_offerta != null)) { //faccio un doppio controllo solo per sicurezza, ne basta uno.
            nomeOggettoOfferta.setText("Nome: "+nome_offerta+"    Prezzo: "+ prezzo_offerta);
            int prezzoOgg = Integer.parseInt(prezzo);
            int prezzoOff = Integer.parseInt(prezzo_offerta);
            int differenza= prezzoOgg - prezzoOff;
            if(differenza < 0) {
                introduzione.setText("La tua offerta: ");
                differenzaPrezzo.setText("La differenza di valore "+ "("+String.valueOf(differenza)+") "+"verrà detratta dalle tue Monete Virtuali");

            }
            btnCambia.setVisibility(View.VISIBLE);
            btnConferma.setVisibility(View.VISIBLE);

        }

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


        btnCambia.setOnClickListener(new View.OnClickListener() {
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

        btnConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VisualizzaProdotto.this, "Offerta inviata", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        this.finish();
        return true;
    }

}