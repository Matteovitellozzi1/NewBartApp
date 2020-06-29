package com.univpm.bartapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapterScelta extends FirebaseRecyclerAdapter<Oggetto, MyAdapter.FirebaseViewHolder> {

    private ArrayList<Oggetto> oggetto;
    private DatabaseReference databaseReference;
    private String idOggetto;

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

    public MyAdapterScelta(@NonNull FirebaseRecyclerOptions<Oggetto> option) {
        super(option);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyAdapter.FirebaseViewHolder firebaseViewHolder, final int i, @NonNull Oggetto oggetto) {
        firebaseViewHolder.nome.setText(oggetto.getNome());
        Log.i("a", "SONO QUI3");
        firebaseViewHolder.nomeVenditore.setText(oggetto.getNomeVenditore());
        firebaseViewHolder.prezzo.setText(String.valueOf(oggetto.getPrezzo()));
        firebaseViewHolder.idUser.setText(oggetto.getIdUser());
        final String keyId = this.getRef(i).getKey();
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

                StorageReference storageReference = firebaseStorage.getReference();
                AppCompatActivity abc = (AppCompatActivity) v.getContext();
                RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
                abc.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_visualizza, recyclerViewFragment, "").addToBackStack(null).commit();

            }
        });

    }

    @NonNull
    @Override
    public MyAdapter.FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapter.FirebaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row, parent, false));
    }
}
