package com.univpm.bartapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.remote.WriteStream;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapterScelta extends FirebaseRecyclerAdapter<Oggetto, MyAdapter.FirebaseViewHolder> {

    private ArrayList<Oggetto> oggetto;
    private DatabaseReference databaseReference;
    private String idOggAcquisto;
    private String idOggettoVenduto;
    private String idUserOggAcquisto;
    Context context;

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

    public MyAdapterScelta(@NonNull FirebaseRecyclerOptions<Oggetto> option, Context context, String idOggettoSceltoCompro) {
        super(option);
        this.context = context;
        this.idOggAcquisto = idOggettoSceltoCompro;
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyAdapter.FirebaseViewHolder firebaseViewHolder, final int i, @NonNull Oggetto oggetto) {
        firebaseViewHolder.nome.setText(oggetto.getNome());
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
                TextView idUser1 = v.findViewById(R.id.id_venditore);
                String idUserVenduto = idUser1.getText().toString(); //sono io che propongo l'oggetto
                invioOfferta(v, keyId, idUserVenduto);
            }
        });

    }

    @NonNull
    @Override
    public MyAdapter.FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapter.FirebaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row, parent, false));
    }

    protected void invioOfferta(final View v, final String idOggettoVenduto, final String idUserVenduto) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Riepilogo Offerta");
        dialog.setCancelable(false);
        dialog.setMessage("Sei sicuro di voler proporre lo scambio?");
        dialog.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                invioDati(idOggettoVenduto, idUserVenduto);
                AppCompatActivity abc = (AppCompatActivity) v.getContext();
                RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
                abc.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_visualizza, recyclerViewFragment, "").addToBackStack(null).commit();
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



    protected void invioDati(final String idOggettoVenduto, final String idUserVenduto) {
        DatabaseReference databaseReferencedati = FirebaseDatabase.getInstance().getReference();
        databaseReferencedati.child("oggetti").child(idOggAcquisto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idUserOggAcquisto = dataSnapshot.child("idUser").getValue().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final Map<String, Object> scambio = new HashMap<>();
                scambio.put("idAcq", idUserOggAcquisto);
                scambio.put("idProdAcq", idOggAcquisto);
                scambio.put("idVend", idUserVenduto);
                scambio.put("idProdVend", idOggettoVenduto);
                db.collection("scambi").document().set(scambio);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}
