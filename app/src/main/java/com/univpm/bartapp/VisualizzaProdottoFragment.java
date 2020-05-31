package com.univpm.bartapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
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
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_visualizza_prodotto, container, false);

        Toolbar toolbar= getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Dettagli del prodotto");



        introduzione= view.findViewById(R.id.header_offerta);
        nomeOggetto = view.findViewById(R.id.nome_oggetto1);
        nomeOggetto.setText(getArguments().getString("Nome1"));
        nomeVenditore = view.findViewById(R.id.nome_venditore1);
        nomeVenditore.setText(getArguments().getString("NomeVend"));
        prezzoOggetto = view.findViewById(R.id.prezzo1);
        prezzoOggetto.setText(getArguments().getString("Prezzo1"));

        immagineOggetto = view.findViewById(R.id.immagine_oggetto1);
        String immagine= getArguments().getString("Immagine1");
        Bitmap bitmap= BitmapFactory.decodeFile(immagine);
        immagineOggetto.setImageBitmap(bitmap);
        nomeOggettoOfferta = view.findViewById(R.id.prodotto_offerta);
        btnOfferta = (Button) view.findViewById(R.id.btn_offerta);
        differenzaPrezzo = view.findViewById(R.id.diff_prezzo);
        btnConferma= (Button) view.findViewById(R.id.btn_conferma);
        btnConferma.setVisibility(View.INVISIBLE);
        btnCambia=(Button) view.findViewById(R.id.btn_cambia);
        btnCambia.setVisibility(View.INVISIBLE);
        btnElimina = (Button) view.findViewById(R.id.button_elimina);
        btnElimina.setVisibility(View.INVISIBLE);

        return view;
    }
}

