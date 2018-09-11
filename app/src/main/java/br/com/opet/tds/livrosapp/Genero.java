package br.com.opet.tds.livrosapp;

import java.sql.Timestamp;

/**
 * Created by Diego on 09/09/2018.
 */

public class Genero {
    private int id;
    private String genero;

    public Genero(){}

    public Genero(int id, String genero) {
        this.id = id;
        this.genero = genero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public long generateTimeStamp(){
        return new Timestamp(System.currentTimeMillis()).getTime();
    }

}
