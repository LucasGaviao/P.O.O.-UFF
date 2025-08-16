package com.lucasgaviao.service;

import com.lucasgaviao.dao.FaturaDAO;
import com.lucasgaviao.dao.ItemFaturadoDAO;
import com.lucasgaviao.exception.*;
import com.lucasgaviao.model.*;
import com.lucasgaviao.util.FabricaDeDaos;

import java.util.ArrayList;
import java.util.List;

public class FaturaService {
    private final FaturaDAO faturaDAO = FabricaDeDaos.getDAO(FaturaDAO.class);

    private final ItemFaturadoDAO itemFaturadoDAO = FabricaDeDaos.getDAO(ItemFaturadoDAO.class);

    private final ItemFaturadoService itemFaturadoService = new ItemFaturadoService();

    public Fatura incluir(Fatura fatura, Pedido pedido){
        faturaDAO.incluir(fatura);
        fatura.getCliente().getFaturas().add(fatura); // adiciona a fatura à lista de faturas do cliente
        pedido.getFaturas().add(fatura);              // adiciona a fatura á lista de faturas do pedido
        System.out.println("\n Fatura incluida com sucesso!");
        return fatura;
    }


    public Fatura recuperarFaturaPorId(int id) {
        Fatura fatura = faturaDAO.recuperarPorId(id);
        if (fatura == null)
            throw new EntidadeNaoEncontradaException("\n Não existe uma fatura com esse Id!");
        return fatura;
    }

//    public Fatura remover(int id){ // Remove atualizando o estoque
//        Fatura fatura = recuperarFaturaPorId(id);
//        if (fatura == null) {
//            throw new EntidadeNaoEncontradaException("\n A fatura não existe ou ja foi removida!");
//        }
//        List<ItemFaturado> itensFaturados = fatura.getItensFaturados();
//        // Para remover uma fatura é preciso remover cada item faturado nela:
//        for(ItemFaturado itemFaturado : itensFaturados){
//            Livro livro = itemFaturado.getItemDePedido().getLivro();
//            livro.setQtdEstoque(livro.getQtdEstoque() + itemFaturado.getQtdFaturada());
//            itemFaturadoDAO.remover(itemFaturado.getId());
//        }
//        faturaDAO.remover(fatura.getId());
//        System.out.println("\n Fatura " + fatura.getId() + " removida com sucesso!");
//        return fatura;
//    }


    public Fatura faturarPedido(Cliente cliente, Pedido pedido, String dataEmissao){
        // tratando as exeções
        if(pedido.getStatus().equals("Cancelado"))
            throw new PedidoCanceladoException("Pedido cancelado! impossível faturar!\n");

        if(pedido.getStatus().equals("Integralmente faturado"))
            throw new PedidoIntegralmenteFaturadoException("Pedido Integralmente faturado! impossível faturar!\n");

        Fatura fatura = new Fatura(dataEmissao, cliente);

        double valorTotal = 0;
        int contaVazio = 0;
        int faturarNovamente = 0;
        ItemFaturado itemFaturado;

        // Para todos os itens de pedido daquele pedido eu gero itens faturados e adiciono à lista de itensFaturadoss da fatura
        List<ItemDePedido> itemDePedidos = pedido.getItemDePedidos();
        for (ItemDePedido itemDePedido : itemDePedidos) {
            Livro livro = itemDePedido.getLivro();

            if(itemDePedido.getQtdRestante() > 0) { //só pega os itens de pedido que não foram faturados ainda
                if (livro.getQtdEstoque() > 0) { // se o livro estiver em estique
                    //se a qtdPedida for maior do que a qtdEstoque daquele livro
                    if (itemDePedido.getQtdRestante() > livro.getQtdEstoque()) { //verifico a qtdEstoque daquele livro
                        itemFaturado = new ItemFaturado(livro.getQtdEstoque(), itemDePedido);
                        // inclui itemFaturado no Dao
                        itemFaturadoService.incluir(itemFaturado);
                        fatura.adicinaItensFaturados(fatura, itemFaturado);

                        valorTotal += itemFaturado.getQtdFaturada() * livro.getPreco();
                        itemDePedido.getItensFaturados().add((itemFaturado));
                        itemDePedido.setQtdRestante(itemDePedido.getQtdRestante() - livro.getQtdEstoque());
                        livro.setQtdEstoque(0);
                    }

                    //a qtdPedida é menor ou igual a qtdEstoque daquele livro
                    else {
                        itemFaturado = new ItemFaturado(itemDePedido.getQtdRestante(), itemDePedido);
                        itemFaturadoService.incluir(itemFaturado);
                        fatura.adicinaItensFaturados(fatura, itemFaturado);

                        valorTotal += itemFaturado.getQtdFaturada() * livro.getPreco();
                        itemDePedido.getItensFaturados().add((itemFaturado));
                        livro.setQtdEstoque(livro.getQtdEstoque() - itemDePedido.getQtdRestante());
                        itemDePedido.setQtdRestante(0);
                    }
                } else {
                    contaVazio += 1;
                }
            }
        }

        // tratando as exeções
        if(contaVazio == pedido.getItemDePedidos().size()) {
            throw new EstoquesVaziosException("Impossível faturar o pedido de Id= " + pedido.getId() + "! Todos os estoques estão vazios\n");
        }

        for (ItemDePedido itemDePedido : itemDePedidos){
            if(itemDePedido.getQtdRestante() > 0){
                faturarNovamente = 1;
            }
        }

        if(faturarNovamente == 1){
            pedido.setStatus("Não integralmente faturado");
        }

        else{
            pedido.setStatus("Integralmente faturado");
        }


        fatura.setValortotal(valorTotal);
        pedido.getFaturas().add(fatura);

        if(cliente.getQtdDeFaturasNaoCanceladas(cliente.getFaturas()) >= 4){
            fatura.setValorTotalDoDesconto(valorTotal * 0.05); //Valor do desconto
        }

        return fatura;

    }

    public Fatura cancelarFatura(Cliente cliente, Fatura fatura, String dataCancelamento){
        // tratando as exeções
        if(cliente.getFaturas().isEmpty()){
            throw new ClienteSemFaturaException("\nO cliente de Id=" + cliente.getId() + " não possui faturas!\n");
        }

        if (fatura == null) {
            throw new EntidadeNaoEncontradaException("Fatura não encontrada.");
        }

        if (fatura.getDataCancelamento() != null){
            throw new FaturaCanceladaException("\nA fatura de Id=" + fatura.getId() + " já está cancelado!\n");
        }

        if(cliente.getFaturas().size() < 3){
            throw new ClienteComNumeroInsuficienteDeFaturasException("\nPara cancelar uma fatura é necessário o faturamento de pelo menos 3 pedidos!");
        }

        List<ItemFaturado> itemFaturados = fatura.getItensFaturados();
        // pra cada itemFaturado "devolve" a qtdFaturada ao stoque
        for (ItemFaturado itemFaturado : itemFaturados){
            Livro livro = itemFaturado.getItemDePedido().getLivro();
            livro.setQtdEstoque(livro.getQtdEstoque() + itemFaturado.getQtdFaturada());
        }

        fatura.setDataCancelamento(dataCancelamento);

        return fatura;

    }

    public Fatura remover(int id, Cliente cliente){ //remove atualizando o estoque
        Fatura fatura = recuperarFaturaPorId(id);
        // tratando as exeções
        if (fatura == null) {
            throw new EntidadeNaoEncontradaException("Fatura não encontrada.");
        }

        if (fatura.getDataCancelamento() != null){
            throw new FaturaCanceladaException("\nA fatura " + id + " está cancelada! Impossível remover!\n");
        }

        if(cliente.getFaturas().isEmpty()){
            throw new ClienteSemFaturaException("\nO cliente " + cliente.getId() + " não possui faturas!\n");
        }

        List<ItemFaturado> itensFaturados = fatura.getItensFaturados();

        // se itensFaturados for nao vazio
        if(!itensFaturados.isEmpty()) {
            for (ItemFaturado itemFaturado : itensFaturados) {
                Livro livro = itemFaturado.getItemDePedido().getLivro();
                livro.setQtdEstoque(livro.getQtdEstoque() + itemFaturado.getQtdFaturada());
                itemFaturadoDAO.remover(itemFaturado.getId());
            }
        }
        faturaDAO.remover(fatura.getId());
        return fatura;
    }

    public void listarFaturasFormatado(List<Fatura> faturas){
        if(faturas.isEmpty()){
            throw new ClienteSemFaturaException("\nO cliente não tem faturas!");
        }
        int aux = 0;
        for (Fatura fatura : faturas){
            if(fatura.getDataCancelamento() != null){
                aux += 1;
            }
            fatura.imprimeFatura(fatura, aux);
            System.out.println("\n");
        }
    }

    public void imprimeItemFaturado(ItemFaturado itemFaturado, Fatura fatura){
        System.out.println("QtdFaturada : " + itemFaturado.getQtdFaturada());
        System.out.println("Nome do Livro : " + itemFaturado.getItemDePedido().getLivro().getTitulo());
        int dia = fatura.getDataEmissao().getDayOfMonth(); //pega o dia da dataEmissão
        int mes = fatura.getDataEmissao().getMonthValue(); //pega o mês da dataEmissão
        int ano = fatura.getDataEmissao().getYear(); //pega o ano da dataEmissão
        String data;
        if (dia >= 10 && mes >= 10){
            data = " " + dia + " /" + " " + mes + " /" + " " + ano; // ex: ex: 10/10/2000
            System.out.println("Data da Fatura : " + data);
        }

        if (dia < 10 && mes < 10){
            data = "0" + dia + " /" + " 0" + mes + " /" + " " + ano; // ex: 01/01/2000
            System.out.println("Data da Fatura : " + data);
        }

        if (dia < 10 && mes >= 10){
            data = "0" + dia + " /" + " " + mes + " /" + " " + ano; // ex: 01/10/2000
            System.out.println("Data da Fatura : " + data);
        }

        if (dia >= 10 && mes < 10){
            data = " " + dia + " /" + " 0" + mes + " /" + " " + ano; // ex: 10/01/2000
            System.out.println("Data da Fatura : " + data);
        }
    }

    public List<Fatura> pegaFaturasDeAcordoComMesAno(int month, int year, List<Fatura> faturas){
        List<Fatura> faturas_MesAno = new ArrayList<>();
        for(Fatura fatura : faturas){
            int mes = fatura.getDataEmissao().getMonthValue(); //pega o mês da dataEmissão
            int ano = fatura.getDataEmissao().getYear(); //pega o ano da dataEmissao
            if(mes == month && ano == year) {
                faturas_MesAno.add(fatura);
            }
        }
        return faturas_MesAno;
    }

    public void relatorio1(List<Fatura> faturas, Livro livro1, int mes, int ano){
        List<Fatura> faturas_Janeiro = pegaFaturasDeAcordoComMesAno(mes, ano, faturas); //pego as faturas de janeiro
        for (Fatura fatura : faturas_Janeiro){ //percorro as faturas de janeiro
            for (ItemFaturado itemFaturado : fatura.getItensFaturados()){ //percorro os itens faturados de janeiro
                ItemDePedido itemDePedido = itemFaturado.getItemDePedido(); //recupero o livro do item faturado
                if(itemDePedido.getLivro() == livro1){ //se o item faturado for referente ao livro 1
                    imprimeItemFaturado(itemFaturado, fatura);
                    System.out.println("\n");
                }
            }
        }

    }

    public void relatorio2(List<Livro> livros){
        int todos_faturados = 1;
        for (Livro livro : livros){
            if(livro.getItemDePedidos().isEmpty()){ //nunca houve um pedido para aquele livro
                todos_faturados = 0;
                System.out.println("O livro " + livro.getId() + " nunca foi faturado.");
                System.out.println("\n");
            }

            else{ //o item de pedido daquele livro nunca gerou um item faturado
                for(ItemDePedido itemDePedido : livro.getItemDePedidos()){
                    if(itemDePedido.getItensFaturados().isEmpty()){
                        todos_faturados = 0;
                        System.out.println("O livro " + livro.getId() + " nunca foi faturado.");
                        System.out.println("\n");
                    }
                }
            }
        }

        if(todos_faturados == 1) {
            System.out.println("Todos os livros já foram faturados pelo menos uma vez!\n");
        }
    }

    public void relatorio3(List<Fatura> faturas, List<Livro> livros, int mes, int ano){
        int cont = 0;
        List<Fatura> faturas_fevereiro = pegaFaturasDeAcordoComMesAno(mes, ano, faturas);

        for (Livro livro : livros){
            for (Fatura fatura : faturas_fevereiro){
                for (ItemFaturado itemFaturado : fatura.getItensFaturados()){
                    ItemDePedido itemDePedido = itemFaturado.getItemDePedido();
                    if(itemDePedido.getLivro() == livro){
                        cont += itemFaturado.getQtdFaturada();
                    }
                }
            }
            System.out.println("Livro " + livro.getId() + " - QtdFaturada = " + cont);
            cont = 0;
        }

    }

    public List<Fatura> recuperarFaturas(){
        return faturaDAO.recuperarTodos();
    }
}
