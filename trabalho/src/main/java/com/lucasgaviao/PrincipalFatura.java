package com.lucasgaviao;

import com.lucasgaviao.exception.*;
import com.lucasgaviao.model.Pedido;
import com.lucasgaviao.service.PedidoService;
import com.lucasgaviao.model.Cliente;
import com.lucasgaviao.service.ClienteService;
import com.lucasgaviao.model.Fatura;
import com.lucasgaviao.service.FaturaService;
import corejava.Console;

public class PrincipalFatura {

    private final PedidoService pedidoService = new PedidoService();

    private final ClienteService clienteService = new ClienteService();

    private final FaturaService faturaService = new FaturaService();

    public void principal(){

        String dataEmissao;
        String dataCancelamento;
        Cliente umCliente;
        Pedido umPedido;
        Fatura umaFatura;

        boolean continua = true;
        while(continua){

            System.out.println('\n' + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.println('\n' + "Selecione uma opção:");
            System.out.println('\n' + "1. Faturar um Pedido");
            System.out.println("2. Cancelar uma Fatura");
            System.out.println("3. Remover uma Fatura");
            System.out.println("4. Listar todas as Faturas de um Cliente");
            System.out.println("5. Voltar");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            System.out.println();

            switch(opcao){
                case 1 ->{
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        umCliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    if(umCliente.getPedidos().isEmpty()){
                        System.out.println("\nO Cliente não tem pedidos!");
                        break;
                    }

                    int idPedido = Console.readInt("Insira o id do Pedido:");

                    try{
                        umPedido = pedidoService.recuperarPedidoPorId(idPedido);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    dataEmissao = Console.readLine("Insira a data e hora da Fatura no formato dd/MM/aaaa/hh:MM:ss");
                    // se a data nao for valida
                    if(!pedidoService.verificaData(dataEmissao)){
                        System.out.println("Data Inválida!");
                        break;
                    }

                    try{
                        Fatura fatura = faturaService.faturarPedido(umCliente, umPedido, dataEmissao);
                        faturaService.incluir(fatura, umPedido);
                    } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
                        System.out.println(e.getMessage());
                    }

                    System.out.println("Faturamento concluído!");
                }

                case 2 ->{
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        umCliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    int idFatura = Console.readInt("Insira o id da Fatura:");

                    try{
                        umaFatura = faturaService.recuperarFaturaPorId(idFatura);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    dataCancelamento = Console.readLine("Insira a data e hora do Cancelamento do Pedido no formato dd/MM/aaaa/hh:MM:ss");
                    // se a data nao for valida
                    if(!pedidoService.verificaData(dataCancelamento)){
                        System.out.println("Data Inválida!");
                        break;
                    }

                    try{
                        faturaService.cancelarFatura(umCliente, umaFatura, dataCancelamento);
                    } catch(ClienteSemFaturaException | EntidadeNaoEncontradaException | FaturaCanceladaException |
                            ClienteComNumeroInsuficienteDeFaturasException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nFatura cancelada com sucesso!");
                }

                case 3 ->{
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        umCliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    int idFatura = Console.readInt("Insira o id da Fatura:");

                    try {
                        faturaService.remover(idFatura, umCliente);
                    } catch (EntidadeNaoEncontradaException | FaturaCanceladaException | ClienteSemFaturaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nFatura removida com sucesso!");
                }

                case 4 ->{
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        umCliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        faturaService.listarFaturasFormatado(umCliente.getFaturas());
                    } catch(ClienteSemFaturaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("Faturas listadas com sucesso!");
                }

                case 5 ->{
                    continua = false;
                }

                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}
