package com.univpm.bartapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter {

    class CViewHolder extends RecyclerView.ViewHolder {
        ImageView immagineOggetto;
        TextView nomeOggetto, nomeVenditore, prezzo;
        CViewHolder (View itemView) {
            super(itemView);
            immagineOggetto = itemView.findViewById(R.id.immagine_oggetto);
            nomeOggetto = itemView.findViewById(R.id.nome_oggetto);
            nomeVenditore = itemView.findViewById(R.id.nome_venditore);
            prezzo = itemView.findViewById(R.id.prezzo);
        }
    }

    private ArrayList<Oggetto> oggetti;
    public MyAdapter (ArrayList<Oggetto> oggetti) {
        this.oggetti=oggetti;
    }

    public int getItemCount() {
        return oggetti.size();
    }

    public CViewHolder onCreateViewHolder (ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from (viewGroup.getContext())
                .inflate(R.layout.rv_row, viewGroup, false);
        return new CViewHolder(v);
    }

    public void onBindViewHoder(MyAdapter.CViewHolder cViewHolder, int position) {

    }
}
