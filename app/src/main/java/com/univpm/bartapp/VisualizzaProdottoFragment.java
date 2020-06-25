package com.univpm.bartapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.univpm.bartapp.R;

public class VisualizzaProdottoFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView nomeOggetto, nomeVenditore, prezzoOggetto, nomeOggettoOfferta, differenzaPrezzo, introduzione;
    private Button btnOfferta, btnConferma, btnCambia, btnElimina;
    private FirebaseUser currentUser;
    private ImageView immagineOggetto;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

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

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Dettagli del prodotto");


        introduzione = view.findViewById(R.id.header_offerta);
        nomeOggetto = view.findViewById(R.id.nome_oggetto1);
        nomeOggetto.setText(getArguments().getString("Nome1"));
        nomeVenditore = view.findViewById(R.id.nome_venditore1);
        nomeVenditore.setText(getArguments().getString("NomeVend"));
        prezzoOggetto = view.findViewById(R.id.prezzo1);
        prezzoOggetto.setText(getArguments().getString("Prezzo1"));


        final String idUser = getArguments().getString("idUser");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String utente = mAuth.getUid();
        final String IdOggetto = getArguments().getString("IdOggetto");

        immagineOggetto = view.findViewById(R.id.immagine_oggetto1);
        String immagine = getArguments().getString("Immagine1");
        Bitmap bitmap = BitmapFactory.decodeFile(immagine);
        immagineOggetto.setImageBitmap(bitmap);
        nomeOggettoOfferta = view.findViewById(R.id.prodotto_offerta);
        btnOfferta = (Button) view.findViewById(R.id.btn_offerta);
        differenzaPrezzo = view.findViewById(R.id.diff_prezzo);
        btnConferma = (Button) view.findViewById(R.id.btn_conferma);
        btnConferma.setVisibility(View.INVISIBLE);
        btnCambia = (Button) view.findViewById(R.id.btn_cambia);
        btnCambia.setVisibility(View.INVISIBLE);
        btnElimina = (Button) view.findViewById(R.id.button_elimina);
        btnElimina.setVisibility(View.INVISIBLE);


        if (idUser.equals(utente)) {
            btnOfferta.setVisibility(View.INVISIBLE);
            btnElimina.setVisibility(View.VISIBLE);
            //introduzione.setText("è un oggetto da te messo in vendita");
        } else {
            btnOfferta.setVisibility(View.VISIBLE);

        }

        btnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = databaseReference.orderByChild("idUser").equalTo(utente);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            Log.i("a", ds.getRef().toString());
                            if (ds.getRef().toString().equals(IdOggetto)) {
                                ds.getRef().removeValue();
                                Toast.makeText(getContext(), "Prodotto cancellato correttamente",Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().popBackStack(); //Serve per eliminare il fragment dallo stack
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });

        btnOfferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MieiOggettiFragment mieiOggettiFragment = new MieiOggettiFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_visualizza, mieiOggettiFragment);
                fragmentTransaction.commit();
            }
        });
            return view;
        }


}

