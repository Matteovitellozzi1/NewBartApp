package com.univpm.bartapp;

import android.util.Log;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class Oggetto {
    private String nome;
    private com.google.firebase.Timestamp data;
    private String nomeVenditore;
    private int prezzo;
    private String descrizione;

    public Oggetto(String nome, String nomeVenditore, String descrizione, int prezzo){
        this.nome=nome;
        this.nomeVenditore=nomeVenditore;
        this.descrizione=descrizione;
        this.prezzo=prezzo;
    }
    public Oggetto (){
        Log.i("a", "COSTRUTTORE OGGETTO NO PARAMS");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public void setData(Timestamp data) {
        this.data = data;
    }

    public String getNomeVenditore() {
        return nomeVenditore;
    }

    public void setNomeVenditore(String nomeVenditore) {
        this.nomeVenditore = nomeVenditore;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}