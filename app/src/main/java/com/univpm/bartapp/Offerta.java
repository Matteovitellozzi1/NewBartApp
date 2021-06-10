package com.univpm.bartapp;

import android.util.Log;

public class Offerta {
    private String idAcq;
    private String idVend;
    private String idProdAcq;
    private String idProdVend;
    private String prezzoAcq;
    private String prezzoOggettoVend;
    private String nomeOggettoAcq;
    private String nomeOggettoVend;
    private String nomeVend;
    private String nomeAcq;
    private String emailVend;
    private int ciao;

    public Offerta (){

    }
    public Offerta(String idAcq, String idVend, String idProdAcq, String idProdVend, String prezzoAcq, String prezzoOggettoVend,
                   String nomeOggettoAcq, String nomeOggettoVend, String nomeVend, String nomeAcq, String emailVend) {
        this.idAcq = idAcq;
        this.idVend = idVend;
        this.idProdAcq = idProdAcq;
        this.idProdVend = idProdVend;
        this.prezzoAcq = prezzoAcq;
        this.prezzoOggettoVend = prezzoOggettoVend;
        this.nomeOggettoAcq = nomeOggettoAcq;
        this.nomeOggettoVend = nomeOggettoVend;
        this.nomeVend = nomeVend;
        this.nomeAcq = nomeAcq;
        this.emailVend = emailVend;
    }

    public String getIdAcq() {
        return idAcq;
    }

    public void setIdAcq(String idAcq) {
        this.idAcq = idAcq;
    }

    public String getIdVend() {
        return idVend;
    }

    public void setIdVend(String idVend) {
        this.idVend = idVend;
    }

    public String getIdProdAcq() {
        return idProdAcq;
    }

    public void setIdProdAcq(String idProdAcq) {
        this.idProdAcq = idProdAcq;
    }

    public String getIdProdVend() {
        return idProdVend;
    }

    public void setIdProdVend(String idProdVend) {
        this.idProdVend = idProdVend;
    }

    public String getPrezzoAcq() {
        return prezzoAcq;
    }

    public void setPrezzoAcq(String prezzoAcq) {
        this.prezzoAcq = prezzoAcq;
    }

    public String getPrezzoOggettoVend() {
        return prezzoOggettoVend;
    }

    public void setPrezzoOggettoVend(String prezzoOggettoVend) {
        this.prezzoOggettoVend = prezzoOggettoVend;
    }

    public String getNomeOggettoAcq() {
        return nomeOggettoAcq;
    }

    public void setNomeOggettoAcq(String nomeOggettoAcq) {
        this.nomeOggettoAcq = nomeOggettoAcq;
    }

    public String getNomeOggettoVend() {
        return nomeOggettoVend;
    }

    public void setNomeOggettoVend(String nomeOggettoVend) {
        this.nomeOggettoVend = nomeOggettoVend;
    }

    public String getNomeVend() {
        return nomeVend;
    }

    public void setNomeVend(String nomeVend) {
        this.nomeVend = nomeVend;
    }

    public String getNomeAcq() {
        return nomeAcq;
    }

    public void setNomeAcq(String nomeAcq) {
        this.nomeAcq = nomeAcq;
    }

    public String getEmailVend() {return emailVend;}
    public void setEmailVend(String emailVend) {this.emailVend = emailVend;}
}
