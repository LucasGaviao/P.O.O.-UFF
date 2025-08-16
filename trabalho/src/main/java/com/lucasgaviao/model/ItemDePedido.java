package com.lucasgaviao.model;

import com.lucasgaviao.util.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemDePedido implements Serializable {
    @Id
    private int id;
    private int qtdPedida;
    private int qtdRestante; // quantidade restante a ser fatorada
    private double precoCobrado;  // preço do livro * qtdPedida
    private Livro livro;
    private List<ItemFaturado> itensFaturados;

    public ItemDePedido(int qtdPedida, int qtdRestante, double precoCobrado, Livro livro) {
        this.precoCobrado = precoCobrado;
        this.qtdRestante = qtdRestante;
        this.qtdPedida = qtdPedida;
        this.livro = livro;
        this.itensFaturados= new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ItemDePedido: " +
                "id: " + id +
                ", qtdPedida: " + qtdPedida +
                ", qtdRestante: " + qtdRestante +
                ", precoCobrado: " + precoCobrado +
                ", " + livro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecoCobrado() {
        return precoCobrado;
    }

    public void setPrecoCobrado(double precoCobrado) {
        this.precoCobrado = precoCobrado;
    }

    public int getQtdRestante() {
        return qtdRestante;
    }

    public void setQtdRestante(int qtdRestante) {
        this.qtdRestante = qtdRestante;
    }

    public int getQtdPedida() {
        return qtdPedida;
    }

    public void setQtdPedida(int qtdPedida) {
        this.qtdPedida = qtdPedida;
    }

    public Livro getLivro(){
        return livro;
    }

    public List<ItemFaturado> getItensFaturados(){
        return itensFaturados;
    }

}
