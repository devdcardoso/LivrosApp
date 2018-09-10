package br.com.opet.tds.livrosapp;

import java.sql.Timestamp;

/**
 * Created by Diego on 09/09/2018.
 */

public class Livro {
    private String titulo;
    private int nPaginas;
    private String genero;

    public Livro() {
    }

    public Livro(String titulo, int nPaginas, String genero) {
        this.titulo = titulo;
        this.nPaginas = nPaginas;
        this.genero = genero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getnPaginas() {
        return nPaginas;
    }

    public void setnPaginas(int nPaginas) {
        this.nPaginas = nPaginas;
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
