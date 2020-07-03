package com.univpm.bartapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MyAdapterRiepilogo extends FirestoreRecyclerAdapter<Riepilogo, MyAdapterRiepilogo.FirestoreViewHolder> {
    Context context;

    public MyAdapterRiepilogo(FirestoreRecyclerOptions<Riepilogo> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyAdapterRiepilogo.FirestoreViewHolder holder, int position, @NonNull Riepilogo model) {

    }

    @NonNull
    @Override
    public MyAdapterRiepilogo.FirestoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapterRiepilogo.FirestoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_riepilogo, parent, false));
    }

    static class FirestoreViewHolder extends RecyclerView.ViewHolder {
        TextView nomeOggettoAcq, emailVend,nomeOggettoVend,nomeVend;
        Button btnCancella;

        public FirestoreViewHolder(@NonNull View v) {
            super(v);
            nomeOggettoAcq = (TextView) v.findViewById(R.id.nome_oggetto);
            nomeOggettoVend =(TextView) v.findViewById(R.id.nome_oggetto_vend);
            emailVend=(TextView) v.findViewById(R.id.email_venditore);
            nomeVend=(TextView) v.findViewById(R.id.nome_venditore);
            btnCancella=(Button) v.findViewById(R.id.btn_elimina);
        }
    }

}
