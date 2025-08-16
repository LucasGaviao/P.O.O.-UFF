package com.lucasgaviao.model;

import com.lucasgaviao.util.Id;

import java.io.Serializable;

public class ItemFaturado implements Serializable {
    @Id
    private int id;
    private int qtdFaturada;
    private ItemDePedido itemDePedido;

    public ItemFaturado(int qtdFaturada, ItemDePedido itemDePedido) {
        this.qtdFaturada = qtdFaturada;
        this.itemDePedido = itemDePedido;
    }

    @Override
    public String toString() {
        return "Item Faturado: " +
                "id: " + id +
                ", Quantidade Faturada: " + qtdFaturada +
                '.';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemDePedido getItemDePedido() {
        return itemDePedido;
    }

    public int getQtdFaturada() {
        return qtdFaturada;
    }

    public void setQtdFaturada(int qtdFaturada) {
        this.qtdFaturada = qtdFaturada;
    }

}
