package com.lucasgaviao.service;

import com.lucasgaviao.dao.PedidoDAO;
import com.lucasgaviao.exception.*;
import com.lucasgaviao.model.Cliente;
import com.lucasgaviao.model.Fatura;
import com.lucasgaviao.model.Pedido;
import com.lucasgaviao.model.Livro;
import com.lucasgaviao.util.EstendeThread;
import com.lucasgaviao.util.FabricaDeDaos;

import com.lucasgaviao.model.ItemDePedido;
import com.lucasgaviao.dao.ItemDePedidoDAO;

import java.text.ParsePosition;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

public class PedidoService {
    private final PedidoDAO pedidoDAO = FabricaDeDaos.getDAO(PedidoDAO.class);

    private final ItemDePedidoDAO itemDePedidoDAO =  FabricaDeDaos.getDAO(ItemDePedidoDAO.class);
    private final ItemDePedidoService itemDePedidoService = new ItemDePedidoService();
    private static final DateTimeFormatter DTF_WITHOUT_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public Pedido incluir(Pedido pedido, ItemDePedido itemDePedido){
        EstendeThread thread = new EstendeThread(pedido.getCliente());
        thread.start();
        pedidoDAO.incluir(pedido);
        itemDePedidoDAO.incluir(itemDePedido); //atualiza o id do itemDePedido cadastrado do pedido
        pedido.getCliente().getPedidos().add(pedido);
        return pedido;
    }

    public Pedido recuperarPedidoPorId(int id) {
        Pedido pedido = pedidoDAO.recuperarPorId(id);
        if (pedido == null)
            throw new EntidadeNaoEncontradaException(" \n Pedido inexistente.");
        return pedido;
    }
    public boolean verificaData(final CharSequence text){
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ParsePosition pos = new ParsePosition(0);
        TemporalAccessor temporalAccessor = DTF_WITHOUT_TIME.parseUnresolved(text, pos);
        System.out.println(temporalAccessor);
        if (temporalAccessor == null || pos.getIndex() < text.length()) {
            return false;
        }
        return true;
    }

    public Pedido remover(int id){
        Pedido pedido = this.recuperarPedidoPorId(id);

        if (pedido == null) {
            throw new EntidadeNaoEncontradaException("Pedido inexistente.");
        }

        List<ItemDePedido> itemDePedidos = pedido.getItemDePedidos();
        for (ItemDePedido itemDePedido : itemDePedidos) {
            itemDePedidoService.remover(itemDePedido.getId());
        }
        pedidoDAO.remover(pedido.getId());
        return pedido;
    }

    public Pedido fazerPedido(Cliente cliente, Livro livro, String dataEmissao, int qtdPedida){
        Pedido pedido = new Pedido(dataEmissao, cliente);
        pedido.setStatus("Não faturado");

        ItemDePedido itemDePedido = new ItemDePedido(qtdPedida, qtdPedida, livro.getPreco(), livro);
        pedido.getItemDePedidos().add(itemDePedido);
        livro.getItemDePedidos().add(itemDePedido);

        incluir(pedido, itemDePedido);

        return pedido;
    }

    public Pedido adicionaItemDePedido(Pedido pedido, Livro livro, int qtdPedida){
        ItemDePedido itemDePedido = new ItemDePedido(qtdPedida, qtdPedida, livro.getPreco(), livro);
        itemDePedidoService.incluir(itemDePedido);
        pedido.getItemDePedidos().add(itemDePedido);
        livro.getItemDePedidos().add(itemDePedido);
        return pedido;
    }

    public Pedido cancelarPedido(Cliente cliente, Pedido pedido, String dataCancelamento) {
        boolean nenhuma_fatura = false;
        boolean faturas_canceladas = true;
        if (cliente.getPedidos().isEmpty()) {
            throw new ClienteSemPedidosException("\nO cliente " + cliente.getId() + " não possui pedidos!\n");
        }

        if (pedido.getStatus().equals("Cancelado")) {
            throw new PedidoCanceladoException("\nO pedido " + pedido.getId() + " já está cancelado!\n");
        }

        if (pedido.getStatus().equals("Integralmente faturado") || pedido.getStatus().equals("Não integralmente faturado")) {
            throw new PedidoFaturadoException("\nO pedido " + pedido.getId() + " foi faturado, não é possível cancelar!\n");
        }

        //verificando se alguma fatura foi gerada a partir de um item de pedido desse pedido
        //Só é possível cancelar um pedido se ele não tiver nenhuma fatura
        if (pedido.getFaturas().isEmpty()) {
            nenhuma_fatura = true;
        }

        //verificando se todas as faturas desse pedido estão canceladas
        //só verifica se o pedido tiver faturas
        //Só é possível cancelar um pedido se todas as faturas tiverem sido canceladas
        if (nenhuma_fatura == false) {
            List<Fatura> faturas = pedido.getFaturas();
            for (Fatura fatura : faturas) {
                if (fatura.getDataCancelamento() == null) { //esta fatura não tem uma dataCancelamento
                    faturas_canceladas = false;
                    break;
                }
            }
        }

        //só cancela se o pedido não tiver faturas ou se todas as faturas desse pedido estiverem canceladas
        if (nenhuma_fatura || faturas_canceladas) {
            pedido.setDataCancelamento(dataCancelamento);
            pedido.setStatus("Cancelado");
        } else {
            throw new ImpossivelCancelarPedidoException("\nNão é possível cancelar o pedido " + pedido.getId() + " !\n");
        }

        return pedido;
    }

    public void listarPedidosDoCliente(Cliente cliente){
        if(cliente.getPedidos().isEmpty()){
            throw new ClienteSemPedidosException("\nO cliente " + cliente.getId() + " não possui pedidos!\n");
        }
        for (Pedido pedido : cliente.getPedidos()) {
            System.out.println(pedido);
            List<ItemDePedido> itemDePedidos = pedido.getItemDePedidos();
            for (ItemDePedido itemDePedido : itemDePedidos) {
                System.out.println(itemDePedido);
            }
            System.out.println("\n");
        }
    }

    public void listarTodosOsPedidos(List<Pedido> pedidos){
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
            List<ItemDePedido> itemDePedidos = pedido.getItemDePedidos();
            for (ItemDePedido itemDePedido : itemDePedidos) {
                System.out.println(itemDePedido);
            }
            System.out.println("\n");
        }
    }

    public List<Pedido> recuperarPedidos(){
        return pedidoDAO.recuperarTodos();
    }

}
