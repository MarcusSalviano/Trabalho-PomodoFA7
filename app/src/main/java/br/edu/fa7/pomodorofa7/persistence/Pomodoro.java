package br.edu.fa7.pomodorofa7.persistence;

import java.io.Serializable;

/**
 * Created by MF on 07/06/16.
 */
public class Pomodoro implements IModel, Serializable{

    private Integer id;
    private String titulo;
    private String descricao;
    private Integer numPomodoros;
    private Boolean isConcluido;

    public Pomodoro(String titulo, String descricao, Integer numPomodoros) {
        this(null, titulo, descricao, numPomodoros);
    }

    public Pomodoro(Integer id, String titulo, String descricao, Integer numPomodoros) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.numPomodoros = numPomodoros;
        this.isConcluido = false;
    }

    public Pomodoro(Integer id, String titulo, String descricao, Integer numPomodoros, Boolean isConcluido) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.numPomodoros = numPomodoros;
        this.isConcluido = isConcluido;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNumPomodoros() {
        return numPomodoros;
    }

    public void setNumPomodoros(Integer numPomodoros) {
        this.numPomodoros = numPomodoros;
    }

    public Boolean isConcluido() {
        return isConcluido;
    }

    public void setConcluido(Boolean concluido) {
        isConcluido = concluido;
    }
}
