package com.univpm.bartapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MyAdapterOfferteInviate extends FirestoreRecyclerAdapter<Offerta, MyAdapterOfferteInviate.FirestoreViewHolder> {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public MyAdapterOfferteInviate (@NonNull FirestoreRecyclerOptions<Offerta> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyAdapterOfferteInviate.FirestoreViewHolder viewHolder, int position, @NonNull final Offerta offerta) {

        firebaseFirestore= FirebaseFirestore.getInstance();
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        storageReference.child("Image").child("ImmaginiOggetti").child(offerta.getIdVend()).child(offerta.getNomeOggettoVend()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(viewHolder.immagineOggettoProposto);
            }
        });

        storageReference.child("Image").child("ImmaginiOggetti").child(offerta.getIdAcq()).child(offerta.getNomeOggettoAcq()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(viewHolder.immagineOggettoRichiesto);
            }
        });

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String a = documentSnapshot.getId();
        viewHolder.nomeVendProposto.setText(offerta.getNomeVend());
        viewHolder.nomeVendRichiesto.setText(offerta.getNomeAcq());
        viewHolder.nomeOggettoProposto.setText(offerta.getNomeOggettoVend());
        viewHolder.nomeOggettoRichiesto.setText(offerta.getNomeOggettoAcq());
        viewHolder.prezzoProposto.setText(offerta.getPrezzoOggettoVend());
        viewHolder.prezzoRichiesto.setText(offerta.getPrezzoAcq());

        final Long keyId = this.getItemId(position);

    }

    @NonNull
    @Override
    public MyAdapterOfferteInviate.FirestoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapterOfferteInviate.FirestoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_offerte_inviate, parent, false));
    }

    static class FirestoreViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeOggettoRichiesto, nomeOggettoProposto;
        public TextView prezzoRichiesto, prezzoProposto;
        public TextView nomeVendRichiesto, nomeVendProposto;
        public ImageView immagineOggettoRichiesto, immagineOggettoProposto;
        public Button elimina;

        public FirestoreViewHolder(@NonNull View v) {
            super(v);
            nomeOggettoProposto = (TextView) v.findViewById(R.id.nome_oggetto_proposto);
            nomeOggettoRichiesto = (TextView) v.findViewById(R.id.nome_oggetto_richiesto);
            prezzoProposto = (TextView) v.findViewById(R.id.prezzo_proposto);
            prezzoRichiesto = (TextView) v.findViewById(R.id.prezzo_richiesto);
            nomeVendRichiesto = (TextView) v.findViewById(R.id.nome_venditore_richiesto);
            nomeVendProposto = (TextView) v.findViewById(R.id.nome_venditore_proposto);
            immagineOggettoRichiesto = (ImageView) v.findViewById(R.id.immagine_oggetto_richiesto);
            immagineOggettoProposto = (ImageView) v.findViewById(R.id.immagine_oggetto_proposto);
            elimina = (Button) v.findViewById(R.id.button_elimina);
        }

    }
}
