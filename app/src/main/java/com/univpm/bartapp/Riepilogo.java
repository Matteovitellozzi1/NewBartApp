package com.univpm.bartapp;

public class Riepilogo {
    private String emailVend;
    private String nomeOggettoAcq;
    private String nomeOggettoVend;
    private String nomeUtenteVend;
    private String idVend;

    public Riepilogo(String emailVend, String nomeOggettoAcq, String nomeOggettoVend, String nomeUtenteVend, String idVend) {
        this.emailVend = emailVend;
        this.nomeOggettoAcq = nomeOggettoAcq;
        this.nomeOggettoVend = nomeOggettoVend;
        this.nomeUtenteVend = nomeUtenteVend;
        this.idVend = idVend;
    }

    public Riepilogo () {}

    public String getEmailVend() {
        return emailVend;
    }

    public void setEmailVend(String emailVend) {
        this.emailVend = emailVend;
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

    public String getNomeUtenteVend() {
        return nomeUtenteVend;
    }

    public void setNomeUtenteVend(String nomeUtenteVend) {
        this.nomeUtenteVend = nomeUtenteVend;
    }

    public String getIdVend() {return idVend;}

    public void setIdVend(String idVend) {this.idVend = idVend;}

}
