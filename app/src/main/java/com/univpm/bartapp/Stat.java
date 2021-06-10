package com.univpm.bartapp;

public class Stat {
    private String scambiati;
    private String esposti;
    private String espostialgiorno;
    private String valoretotale;
    private String valorealgiorno;
    private String accessi;





    public Stat(String scambiati, String esposti, String espostialgiorno, String valoretotale, String valorealgiorno, String accessi) {
        this.scambiati = scambiati;
        this.esposti = esposti;
        this.espostialgiorno = espostialgiorno;
        this.valoretotale = valoretotale;
        this.valorealgiorno = valorealgiorno;
        this.accessi = accessi;
    }

    public Stat () { }

    public void setScambiati(String scambiati) {
        this.scambiati = scambiati;
    }

    public String getEsposti() {
        return esposti;
    }

    public void setEsposti(String esposti) {
        this.esposti = esposti;
    }

    public String getEspostialgiorno() {
        return espostialgiorno;
    }

    public void setEspostialgiorno(String espostialgiorno) {
        this.espostialgiorno = espostialgiorno;
    }

    public String getValoretotale() {
        return valoretotale;
    }

    public void setValoretotale(String valoretotale) {
        this.valoretotale = valoretotale;
    }

    public String getValorealgiorno() {
        return valorealgiorno;
    }

    public void setValorealgiorno(String valorealgiorno) {
        this.valorealgiorno = valorealgiorno;
    }

    public String getAccessi() {
        return accessi;
    }

    public void setAccessi(String accessi) {
        this.accessi = accessi;
    }




}
