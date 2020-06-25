package com.univpm.bartapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class MyAdapter extends FirebaseRecyclerAdapter<Oggetto, MyAdapter.FirebaseViewHolder> {

    private ArrayList<Oggetto> oggetto;
    private DatabaseReference databaseReference;


    public MyAdapter(@NonNull FirebaseRecyclerOptions<Oggetto> option) {
        super(option);
    }

    @Override
    protected void onBindViewHolder(@NonNull final FirebaseViewHolder firebaseViewHolder, final int i, @NonNull Oggetto oggetto) {
        firebaseViewHolder.nome.setText(oggetto.getNome());
        Log.i("a", "SONO QUI3");
        firebaseViewHolder.nomeVenditore.setText(oggetto.getNomeVenditore());
        firebaseViewHolder.prezzo.setText(String.valueOf(oggetto.getPrezzo()));
        firebaseViewHolder.idUser.setText(oggetto.getIdUser());
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("oggetti");
        databaseReference.keepSynced(true);

        // options = new FirebaseRecyclerOptions.Builder<Oggetto>().setQuery(databaseReference, Oggetto.class).build();

        String idUser = oggetto.getIdUser();
        String nome = oggetto.getNome();
        storageReference.child("Image").child("ImmaginiOggetti").child(idUser).child(nome).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(firebaseViewHolder.immagineOggetto);
            }
        });

        firebaseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView nome1 = v.findViewById(R.id.nome_oggetto);
                String Nome1 = nome1.getText().toString();
                TextView nomeVend = v.findViewById(R.id.nome_venditore);
                String NomeVend = nomeVend.getText().toString();
                TextView prezzo1 = v.findViewById(R.id.prezzo);
                String Prezzo1 = prezzo1.getText().toString();
                TextView idUser1 = v.findViewById(R.id.id_venditore);
                String IdUser1 = idUser1.getText().toString();
                String IdOggetto = getRef(i).toString();

                        /*Intent intent = new Intent(v.getContext(), VisualizzaProdottoFragment.class);
                        intent.putExtra("IdOggetto", IdOggetto);
                        intent.putExtra("Nome1", Nome1);
                        intent.putExtra("NomeVend", NomeVend);
                        intent.putExtra("Prezzo1", Prezzo1);
                        intent.putExtra("idUser", IdUser1);
                        startActivity(intent);*/
                StorageReference storageReference = firebaseStorage.getReference();
                Bundle bundle= new Bundle();
                bundle.putString("IdOggetto", IdOggetto);
                bundle.putString("Nome1", Nome1);
                bundle.putString("NomeVend", NomeVend);
                bundle.putString("Prezzo1", Prezzo1);

                bundle.putString( "Immagine1", storageReference.child("Image").child("ImmaginiOggetti").child(IdUser1).child(Nome1).getDownloadUrl().toString());
                bundle.putString("idUser", IdUser1);
                AppCompatActivity abc= (AppCompatActivity) v.getContext();
                VisualizzaProdottoFragment visualizzaProdottoFragment= new VisualizzaProdottoFragment();
                visualizzaProdottoFragment.setArguments(bundle);
                abc.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_visualizza, visualizzaProdottoFragment, "").addToBackStack(null).commit();

            }
        });

    }

    @NonNull
    @Override
    public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FirebaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row, parent, false));

    }


    static class FirebaseViewHolder extends RecyclerView.ViewHolder {
        public ImageView immagineOggetto;
        public TextView nome;
        TextView nomeVenditore, prezzo, idUser;

        public FirebaseViewHolder(@NonNull View itemView) {
            super(itemView);
            immagineOggetto = itemView.findViewById(R.id.immagine_oggetto);
            nome = itemView.findViewById(R.id.nome_oggetto);
            nomeVenditore = itemView.findViewById(R.id.nome_venditore);
            prezzo = itemView.findViewById(R.id.prezzo);
            idUser = itemView.findViewById(R.id.id_venditore);

        }
    }

}