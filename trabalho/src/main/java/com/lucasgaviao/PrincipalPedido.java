package com.lucasgaviao;

import com.lucasgaviao.exception.*;
import com.lucasgaviao.model.Pedido;
import com.lucasgaviao.service.PedidoService;
import com.lucasgaviao.model.Cliente;
import com.lucasgaviao.service.ClienteService;
import com.lucasgaviao.model.Livro;
import com.lucasgaviao.service.LivroService;
import corejava.Console;

public class PrincipalPedido {
    // inicializando services:
    private final PedidoService pedidoService = new PedidoService();

    private final ClienteService clienteService = new ClienteService();

    private final LivroService livroService = new LivroService();

    public void principal(){

        Pedido pedido;
        Cliente cliente;
        Livro livro;

        String dataEmissao;
        String dataCancelamento;

        int qtdPedida;
        int idLivro;

        boolean continua = true;
        while(continua){

            System.out.println('\n' + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.println('\n' + "Selecione uma opção: ");
            System.out.println('\n' + "1. Fazer um Pedido");
            System.out.println("2. Cancelar um Pedido");
            System.out.println("3. Listar todos os Pedidos de um Cliente");
            System.out.println("4. Voltar");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            System.out.println();

            switch (opcao) {
                case 1 -> {
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        cliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    dataEmissao = Console.readLine("Insira a data e hora do Pedido no formato dd/MM/aaaa/hh:MM:ss");
                    // se a data nao for valida
                    if(!pedidoService.verificaData(dataEmissao)){
                        System.out.println("Data Inválida!");
                        break;
                    }
                    qtdPedida = Console.readInt("Insira a quantidade desejada: ");
                    idLivro = Console.readInt("Insira o id do livro: ");

                    try{
                        livro = livroService.recuperarLivroPorId(idLivro);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    pedido = pedidoService.fazerPedido(cliente, livro, dataEmissao, qtdPedida);
                    System.out.println("\nPedido realizado com sucesso!");

                    while(true){
                        String op = Console.readLine("\nDeseja adicionar mais itens de pedido? s/n");
                        if(op.equals("n") || op.equals("N")){
                            System.out.println("\nPedido cadastrado com sucesso!");
                            break;
                        }

                        if(op.equals("s") || op.equals("S")){
                            qtdPedida = Console.readInt("Insira a quantidade desejada: ");
                            idLivro = Console.readInt("Insira o id do livro: ");

                            try{
                                livro = livroService.recuperarLivroPorId(idLivro);
                            } catch(EntidadeNaoEncontradaException e){
                                System.out.println(e.getMessage());
                                break;
                            }

                            pedido = pedidoService.adicionaItemDePedido(pedido, livro, qtdPedida);
                            System.out.println("\nItem de pedido adicionado com sucesso!");
                        }

                        else {
                            System.out.println('\n' + "Opção inválida! Item não contabilizado!");
                        }
                    }
                }

                case 2 -> {
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        cliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    int idPedido = Console.readInt("Insira o id do Pedido:");

                    try{
                        pedido = pedidoService.recuperarPedidoPorId(idPedido);
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
                        pedidoService.cancelarPedido(cliente, pedido, dataCancelamento);
                    } catch(ClienteSemPedidosException | PedidoCanceladoException | PedidoFaturadoException |
                            ImpossivelCancelarPedidoException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nO Pedido " + pedido.getId() + " foi cancelado com sucesso!\n");

                }

                case 3 -> {
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        cliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        pedidoService.listarPedidosDoCliente(cliente);
                    } catch(ClienteSemPedidosException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nTodos os pedidos listados com sucesso!");
                }
                case 4 ->{
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}
