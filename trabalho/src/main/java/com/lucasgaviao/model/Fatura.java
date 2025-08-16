package com.lucasgaviao.model;

import com.lucasgaviao.util.Id;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Fatura implements Serializable {
    @Id

    private int id;
    private LocalDate dataEmissao;
    private LocalDate dataCancelamento;
    private Cliente cliente;
    private List<ItemFaturado> itensFaturados;

    public double valorTotal;
    public double valorTotalDoDesconto;

    private static final DateTimeFormatter DTF_WITHOUT_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Fatura(String dataEmissao, Cliente cliente){
        setDataEmissao(dataEmissao);
        this.cliente = cliente;
        this.itensFaturados= new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Fatura: " +
                "id: " + id +
                ", cliente: " + cliente +
                ", dataEmissao: " + dataEmissao +
                '.';
    }


    public int getId() {
        return id;
    }

    public void imprimeFatura(Fatura fatura, int faturasNaoCanceladas){
        if (faturasNaoCanceladas > 3) {
            System.out.println("Fatura " + fatura.getId() + "," + " valorTotalDaFatura: " + fatura.getValorTotal() + " valorTotalDoDesconto: " + fatura.getValorTotalDoDesconto() + "(5% de desconto para a quarta fatura em diante)");
        }
        else{
            System.out.println("Fatura " + fatura.getId() + "," + " valorTotalDaFatura: " + fatura.getValorTotal() + " valorTotalDoDesconto: " + fatura.getValorTotalDoDesconto());
        }

        for (ItemFaturado itemFaturado : fatura.getItensFaturados()){
            Livro livro = itemFaturado.getItemDePedido().getLivro();
            System.out.println("Livro " + livro.getId() + " - " + "qtdFaturada= " + itemFaturado.getQtdFaturada());
        }

    }

    public double getValorTotalDoDesconto() {
        return valorTotalDoDesconto;
    }

    public void setValorTotalDoDesconto(double valorTotalDoDesconto) {
        this.valorTotalDoDesconto = valorTotalDoDesconto;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValortotal(double valortotal) {
        this.valorTotal = valortotal;
    }

    public void setId(int id) {
        this.id = id;
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
        try {
            this.dataCancelamento = LocalDate.parse(dataCancelamento, DTF_WITHOUT_TIME);
        } catch (StringIndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
            System.out.println("Erro ao definir dataEmissao: " + dataCancelamento + ". Usando data padrão.");
            // Definindo a data para a data corrente para evitar valores nulos
            this.dataCancelamento = LocalDate.now(ZoneId.of("UTC"));
        }
    }

    public Cliente getCliente(){
        return cliente;
    }

    public List<ItemFaturado> getItensFaturados(){
        return itensFaturados;
    }

    public Fatura adicinaItensFaturados(Fatura fatura, ItemFaturado itemFaturado){
        fatura.getItensFaturados().add(itemFaturado);
        return fatura;
    }

    public void setItensFaturados(List<ItemFaturado> itensFaturados) {
        this.itensFaturados = itensFaturados;
    }

}
