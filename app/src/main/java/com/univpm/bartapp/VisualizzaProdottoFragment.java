package com.univpm.bartapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.univpm.bartapp.R;

public class VisualizzaProdottoFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView nomeOggetto, nomeVenditore, prezzoOggetto, descrizione;
    private Button btnOfferta, btnElimina;
    private FirebaseUser currentUser;
    private ImageView immagineOggetto;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String nome, nomeVend, prezzo, utente;
    private String IdOggetto;
   

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        databaseReference.keepSynced(true);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_visualizza_prodotto, container, false);

        //Prendo i dati del bundle e li inietto nella vista
        descrizione = view.findViewById(R.id.descrizione);
        descrizione.setText(getArguments().getString("descrizione"));
        nomeOggetto = view.findViewById(R.id.nome_oggetto1);
        nomeOggetto.setText(getArguments().getString("Nome1"));
        nome = getArguments().getString("Nome1");
        nomeVenditore = view.findViewById(R.id.nome_venditore1);
        nomeVenditore.setText(getArguments().getString("NomeVend"));
        nomeVend = getArguments().getString("NomeVend");
        prezzoOggetto = view.findViewById(R.id.prezzo1);
        prezzoOggetto.setText(getArguments().getString("Prezzo1"));
        prezzo = getArguments().getString("Prezzo1");


        final String idUser = getArguments().getString("idUser");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        utente = mAuth.getUid();
        IdOggetto = getArguments().getString("IdOggetto");

        immagineOggetto = view.findViewById(R.id.immagine_oggetto1);
        btnOfferta = (Button) view.findViewById(R.id.btn_offerta);
        btnElimina = (Button) view.findViewById(R.id.button_elimina);
        btnElimina.setVisibility(View.INVISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        databaseReference.keepSynced(true);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //Inserisco l'immagine nel visualizza prodotto del Visualizza prodotto
        storageReference.child("Image").child("ImmaginiOggetti").child(idUser).child(nome).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(immagineOggetto);
            }
        });

        if (idUser.equals(utente)) { //Controllo se l'oggetto che ho cercato di visualizzare e' stato inserito da me
            // se così è rendo invisibile il bottone per effettuare un'offerta, viceversa lo mostro
            btnOfferta.setVisibility(View.INVISIBLE);
            btnElimina.setVisibility(View.VISIBLE);
        } else {
            btnOfferta.setVisibility(View.VISIBLE);
        }

        btnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminaAlert();
            }
        });

        //Al click sull'offerta creo un bundle per la nuova vista e cambio fragment con lo scelta prodotto fragment
        btnOfferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("oggetto", IdOggetto);
                SceltaProdottoFragment sceltaProdottoFragment = new SceltaProdottoFragment();
                sceltaProdottoFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_visualizza, sceltaProdottoFragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void eliminaAlert() { //Funzione per eliminare l'oggetto anche qui
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setTitle("Attenzione!");
        dialog.setCancelable(false);
        dialog.setMessage("Sei sicuro di voler eliminare l'oggetto?");
        dialog.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query query = databaseReference.orderByChild("idUser").equalTo(utente);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dataSnapshot.child(IdOggetto).getRef().removeValue();
                        String mAuth= FirebaseAuth.getInstance().getCurrentUser().getUid();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image").child("ImmaginiOggetti").child(mAuth);
                        storageReference.child(nome).delete();
                        Toast.makeText(getContext(), "Prodotto cancellato correttamente", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack(); //Serve per eliminare il fragment dallo stack
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        dialog.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }


}

