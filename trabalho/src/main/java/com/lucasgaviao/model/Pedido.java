package com.lucasgaviao.model;

import com.lucasgaviao.util.Id;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido implements Serializable {
    @Id
    private int id;
    private LocalDate dataEmissao;
    private LocalDate dataCancelamento;
    private String status;
    private Cliente cliente;
    private List<ItemDePedido> itemDePedidos;
    private List<Fatura> faturas;
    private String enderecoEntrega;

    private static final DateTimeFormatter DTF_WITHOUT_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Pedido(String dataEmissao, Cliente cliente){
        setDataEmissao(dataEmissao);
        this.cliente = cliente;
        this.enderecoEntrega = enderecoEntrega;
        this.itemDePedidos = new ArrayList<>();
        this.faturas = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Pedido: " + id +
                "\nCliente: " + cliente.getNome() +
                "\nData de Emissao: " + getDataEmissao() +
                "\nStatus: " + getStatus() + "." +
                '\n';
    }

    public int getId() {
        return id;
    }

    public String getEnderecoEntrega() { return enderecoEntrega; }

    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }
    public void setDataEmissao(String dataEmissao) {
        try {
            this.dataEmissao = LocalDate.parse(dataEmissao, DTF_WITHOUT_TIME);
        }catch (StringIndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
        System.out.println("Erro ao definir dataEmissao: " + dataEmissao + ". Usando data padrão.");
        // Definindo a data para a data corrente para evitar valores nulos
        this.dataEmissao = LocalDate.now(ZoneId.of("UTC"));
        }

    }

    public LocalDate getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(String dataCancelamento) {
        try{
            this.dataCancelamento = LocalDate.parse(dataCancelamento, DTF_WITHOUT_TIME);
        }catch (StringIndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
            System.out.println("Erro ao definir dataEmissao: " + dataCancelamento+ ". Usando data corrente.");
            // Definindo a data para a data corrente para evitar valores nulos
            this.dataCancelamento = LocalDate.now(ZoneId.of("UTC"));
    }
    }
    public Cliente getCliente(){
        return cliente;
    }

    public List<ItemDePedido> getItemDePedidos(){
        return itemDePedidos;
    }

    public List<Fatura> getFaturas(){
        return faturas;
    }

}





