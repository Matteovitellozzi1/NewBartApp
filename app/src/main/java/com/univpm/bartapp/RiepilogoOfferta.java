package com.univpm.bartapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RiepilogoOfferta extends Fragment {


    private FirebaseAuth mAuth;
    private TextView nomeOggettoScelta, nomeVenditoreScelta, prezzoOggettoScelta, oggetto, prezzo, nomeVend;
    private Button btnOfferta, btnConferma, btnCambia, btnElimina;
    private FirebaseUser currentUser;
    private ImageView immagineOggetto;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.riepilogo_offerta, container, false);

        //offerente
        String idOggetto_scelta = getArguments().getString("oggetto_scelta");
        nomeOggettoScelta = view.findViewById(R.id.nome_oggetto_scelta);
        nomeOggettoScelta.setText(getArguments().getString("nome_scelta"));
        nomeVenditoreScelta = view.findViewById(R.id.nomeVend_scelta);
        nomeVenditoreScelta.setText(getArguments().getString("nomeVend_scelta"));
        prezzoOggettoScelta = view.findViewById(R.id.prezzo_oggetto_scelta);
        prezzoOggettoScelta.setText(getArguments().getString("prezzo_scelta"));
        final String idUser_scelta = getArguments().getString("idUser_scelta");

        //colui che riceve l'offerta
        String idOggetto = getArguments().getString("oggetto");
        oggetto = view.findViewById(R.id.oggetto);
        oggetto.setText(getArguments().getString("nome"));
        nomeVend = view.findViewById(R.id.nomeVend);
        nomeVend.setText(getArguments().getString("nomeVend"));
        prezzo = view.findViewById(R.id.prezzo);
        prezzo.setText(getArguments().getString("prezzo"));
        final String idUser = getArguments().getString("idUser");
        return view;
    }

}
