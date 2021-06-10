package com.univpm.bartapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MyAdapterRiepilogo extends FirestoreRecyclerAdapter<Riepilogo, MyAdapterRiepilogo.FirestoreViewHolder> {
    Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();


    public MyAdapterRiepilogo(FirestoreRecyclerOptions<Riepilogo> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyAdapterRiepilogo.FirestoreViewHolder holder, int position, @NonNull final Riepilogo model) {
        if (model.getIdAcq().equals(mAuth.getUid())) {
            holder.relativeLayout2.setVisibility(View.INVISIBLE);
            holder.nomeOggettoAcq.setText(model.getNomeOggettoAcq());
            holder.nomeOggettoVend.setText(model.getNomeOggettoVend());
            holder.nomeVend.setText(model.getNomeUtenteVend());
            holder.emailVend.setText(model.getEmailVend());
            DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
            final String id = documentSnapshot.getId();
            holder.btnCancella.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminaRiepilogo(id);
                }
            });

            holder.btnContatta.setOnClickListener(new View.OnClickListener() {
                final String email = model.getEmailVend();
                final String nomeOggetto = model.getNomeOggettoVend();
                final String nomeOggetto2 = model.getNomeOggettoAcq();

                @Override
                public void onClick(View v) {
                    contatta(email, nomeOggetto, nomeOggetto2, context);
                }
            });
        } else if (model.getIdVend().equals(mAuth.getUid())) {
            holder.relativeLayout1.setVisibility(View.INVISIBLE);
            holder.email2.setText("Email: " + model.getEmailAcq());
            holder.nomeOggettoProposto.setText("Oggetto proposto: " + model.getNomeOggettoVend());
            holder.nomeOggettoRichiesto.setText("Oggetto richiesto: " + model.getNomeOggettoAcq());
            holder.btnContatta2.setOnClickListener(new View.OnClickListener() {
                final String email = model.getEmailAcq();
                final String nomeOggetto = model.getNomeOggettoVend();
                final String nomeOggetto2 = model.getNomeOggettoAcq();

                @Override
                public void onClick(View v) {
                    contatta(email, nomeOggetto, nomeOggetto2, context);
                }
            });
        } else {
            holder.itemView.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public MyAdapterRiepilogo.FirestoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapterRiepilogo.FirestoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_riepilogo, parent, false));
    }

    static class FirestoreViewHolder extends RecyclerView.ViewHolder {
        TextView nomeOggettoAcq, emailVend, nomeOggettoVend, nomeVend, nomeOggettoProposto, nomeOggettoRichiesto, email2;
        ImageButton btnCancella;
        Button btnContatta, btnContatta2;
        RelativeLayout relativeLayout1, relativeLayout2;

        public FirestoreViewHolder(@NonNull View v) {
            super(v);
            nomeOggettoAcq = (TextView) v.findViewById(R.id.oggetto_mio);
            nomeOggettoVend = (TextView) v.findViewById(R.id.nome_oggetto_vend);
            emailVend = (TextView) v.findViewById(R.id.email_venditore);
            nomeVend = (TextView) v.findViewById(R.id.nome_venditore);
            btnCancella = (ImageButton) v.findViewById(R.id.btn_elimina);
            btnContatta = (Button) v.findViewById(R.id.contatta);
            relativeLayout1 = v.findViewById(R.id.relative_layout);
            relativeLayout2 = v.findViewById(R.id.relative_layout2);
            nomeOggettoProposto = v.findViewById(R.id.nome_oggetto_proposto);
            nomeOggettoRichiesto = v.findViewById(R.id.nome_oggetto_richiesto);
            email2 = v.findViewById(R.id.email2);
            btnContatta2 = v.findViewById(R.id.btn_contatta);
        }
    }

    public void eliminaRiepilogo(final String id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Attenzione!");
        dialog.setCancelable(false);
        dialog.setMessage("Sei sicuro di voler il riepilogo dell'offerta? Perderai tutti i dettagli relativi ad essa!");
        dialog.setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("riepilogoscambi").document(id).delete();
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

    public void contatta(String email, String nomeOggetto, String nomeOggetto2, Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Scambio Oggetto");
        intent.putExtra(Intent.EXTRA_TEXT, "Sono interessato all'oggetto " +
                nomeOggetto + " che ha inserito, in cambio del mio " + nomeOggetto2 + " possiamo effettuare lo scambio!");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        context.startActivity(Intent.createChooser(intent, "Invia email.."));
    }

}
