package com.univpm.bartapp;

public class Riepilogo {
    private String emailVend;
    private String emailAcq;
    private String nomeOggettoAcq;
    private String nomeOggettoVend;
    private String nomeUtenteVend;
    private String idVend;

    private String idAcq;

    public Riepilogo(String emailVend, String nomeOggettoAcq, String nomeOggettoVend, String nomeUtenteVend, String idVend, String idAcq) {
        this.emailVend = emailVend;
        this.nomeOggettoAcq = nomeOggettoAcq;
        this.nomeOggettoVend = nomeOggettoVend;
        this.nomeUtenteVend = nomeUtenteVend;
        this.idVend = idVend;
        this.idAcq = idAcq;
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

    public String getIdAcq() {
        return idAcq;
    }

    public void setIdAcq(String idAcq) {
        this.idAcq = idAcq;
    }

    public String getEmailAcq() {
        return emailAcq;
    }

    public void setEmailAcq(String emailAcq) {
        this.emailAcq = emailAcq;
    }


}
