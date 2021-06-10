package com.univpm.bartapp;

import android.os.Parcelable;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.sql.Time;

public class Oggetto {
    private String nome;
    private String nomeVenditore;
    private int prezzo;
    private String descrizione;
    private String idUser;

    public Oggetto(String nome, String nomeVenditore, String descrizione, int prezzo, String idUser){
        this.nome=nome;
        this.nomeVenditore=nomeVenditore;
        this.descrizione=descrizione;
        this.prezzo=prezzo;
        this.idUser=idUser;
    }
    public Oggetto (){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

}
