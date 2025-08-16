package com.lucasgaviao;

import com.lucasgaviao.exception.*;
import com.lucasgaviao.model.*;
import com.lucasgaviao.service.ClienteService;
import com.lucasgaviao.service.FaturaService;
import com.lucasgaviao.service.LivroService;
import com.lucasgaviao.service.PedidoService;

import java.util.concurrent.TimeUnit;
import java.util.List;

public class TesteSistema {
    // inicializando services:
    private final PedidoService pedidoService = new PedidoService();

    private final ClienteService clienteService = new ClienteService();

    private final FaturaService faturaService = new FaturaService();

    private final LivroService livroService = new LivroService();
    public void principal() {
        //1) Cadastrar 5 livros
        System.out.println("1) Cadastrar 5 livros:");
        Livro livro_1 = new Livro("10", "Aaa", "Aaaa", 10, 100);
        livroService.incluir(livro_1);

        Livro livro_2 = new Livro("20", "Bbb", "Bbbb", 20, 200);
        livroService.incluir(livro_2);

        Livro livro_3 = new Livro("30", "Ccc", "Cccc", 30, 300);
        livroService.incluir(livro_3);

        Livro livro_4 = new Livro("40", "Ddd", "Dddd", 40, 400);
        livroService.incluir(livro_4);

        Livro livro_5 = new Livro("50", "Eee", "Eeee", 50, 500);
        livroService.incluir(livro_5);
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //2) Listar os produtos cadastrados. Ao listar os produtos, o nome do livro e a quantidade devem ser exibidos
        // listarLivros precisa de uma List<Livro> que é fornecida pelo metodo recuperarLivros:
        System.out.println("2) Listar os produtos cadastrados:");
        livroService.listarLivros(livroService.recuperarLivros());
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //3) Cadastrar 2 clientes.
        System.out.println("3) Cadastrar 2 clientes:");
        Cliente cliente_1 = new Cliente("111", "Xxxx", "xxxx@gmail.com", "111111");
        clienteService.incluir(cliente_1);

        Cliente cliente_2 = new Cliente("222", "Yyyy", "yyyy@gmail.com", "222222");
        clienteService.incluir(cliente_2);

        System.out.println("Clientes cadastrados:");
        clienteService.listarClientes(clienteService.recuperarClientes());

        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");



        //4) Cadastrar 5 pedidos (ao cadastrar cada pedido a thread que simula o envio de email deverá ser executada):
        /* Para facilitar o processo fiz uma função auxiliar que otimiza o processo detalhado no enuncuado
        Essa função só comporta 1 livro ao criar o pedido, mas ela retorna um new Pedido oq permite acrescentar
        itensDePedido posteriormente.
        */

        System.out.println("4) Cadastrar 5 pedidos:");
        // pedido 1
        Pedido pedido_1 = pedidoService.fazerPedido(
                clienteService.recuperarClientePorId(1),
                livroService.recuperarLivroPorId(1),
                "01/01/2025",
                5);

        pedidoService.adicionaItemDePedido(
                pedido_1,
                livroService.recuperarLivroPorId(2),
                15);

        // pedido 2
        Pedido pedido_2 = pedidoService.fazerPedido(
                clienteService.recuperarClientePorId(1),
                livroService.recuperarLivroPorId(1),
                "02/01/2025",
                10);
        pedidoService.adicionaItemDePedido(
                pedido_2,
                livroService.recuperarLivroPorId(3),
                40);

        // pedido 3
        Pedido pedido_3 = pedidoService.fazerPedido(
                clienteService.recuperarClientePorId(1),
                livroService.recuperarLivroPorId(1),
                "03/01/2025",
                5);

        pedidoService.adicionaItemDePedido(
                pedido_3,
                livroService.recuperarLivroPorId(3),
                10);

        // pedido 4
        Pedido pedido_4 = pedidoService.fazerPedido(
                clienteService.recuperarClientePorId(1),
                livroService.recuperarLivroPorId(2),
                "04/01/2025",
                10);

        pedidoService.adicionaItemDePedido(
                pedido_4,
                livroService.recuperarLivroPorId(3),
                10);
        pedidoService.adicionaItemDePedido(
                pedido_4,
                livroService.recuperarLivroPorId(4),
                10);

        // pedido 5
        Pedido pedido_5 = pedidoService.fazerPedido(
                clienteService.recuperarClientePorId(1),
                livroService.recuperarLivroPorId(2),
                "05/01/2025",
                5);

        pedidoService.adicionaItemDePedido(
                pedido_5,
                livroService.recuperarLivroPorId(3),
                5);
        pedidoService.adicionaItemDePedido(
                pedido_5,
                livroService.recuperarLivroPorId(4),
                5);


        try {
            TimeUnit.MILLISECONDS.sleep(600);
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }

        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //5) Listar todos os pedidos exibindo para cada pedido o seu status e as quantidades pedidas de cada livro.
        System.out.println("5) Listar todos os pedidos:");
        pedidoService.listarTodosOsPedidos(pedidoService.recuperarPedidos());
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //6) Listar os Livros com suas respectivas quantidades em estoque. Deverão ser exibidos os seguintes dados:
        System.out.println("6) Listar os Livros com suas respectivas quantidades em estoque:");
        livroService.listarLivros(livroService.recuperarLivros());
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //7) Faturar os pedidos 1 e 2, nesta ordem, para 10 de janeiro de 2025.
        System.out.println("7) Faturar os pedidos 1 e 2, nesta ordem, para 10 de janeiro de 2025:");
        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_1, "10/01/2025");
            faturaService.incluir(fatura, pedido_1);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_2, "10/01/2025");
            faturaService.incluir(fatura, pedido_2);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //8) Cancelar a fatura 2.
        System.out.println("8) Cancelar a fatura 2:");
        try {
            faturaService.cancelarFatura(
                    faturaService.recuperarFaturaPorId(2).getCliente(),
                    faturaService.recuperarFaturaPorId(2),
                    "11/01/2025"
            );
        } catch (PedidoCanceladoException | PedidoIntegralmenteFaturadoException |
                 FaturaCanceladaException | EstoquesVaziosException |
                 ClienteComNumeroInsuficienteDeFaturasException e) {
            System.out.println(e.getMessage() + "\n");
        }

        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //9) Faturar os pedidos 3 e 4, nesta ordem, para 20 de janeiro de 2025.
        System.out.println("9) Faturar os pedidos 3 e 4, nesta ordem, para 20 de janeiro de 2025:\n");
        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_3, "20/01/2025");
            faturaService.incluir(fatura, pedido_3);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_4, "20/01/2025");
            faturaService.incluir(fatura, pedido_4);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n");
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");
        //10) Faturar o pedido 5 para 28 de fevereiro de 2025.
        System.out.println("10) Faturar o pedido 5 para 28 de fevereiro de 2025:");
        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_5, "28/02/2025");
            faturaService.incluir(fatura, pedido_5);
            if(cliente_1.getQtdDeFaturasNaoCanceladas(cliente_1.getFaturas()) >= 4) fatura.setValorTotalDoDesconto(fatura.getValorTotal() * 0.05);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //11) Listar os Livros com suas respectivas quantidades em estoque. Deverão ser exibidos os seguintes dados:
        System.out.println("11) Listar os Livros com suas respectivas quantidades em estoque:");
        livroService.listarLivros(livroService.recuperarLivros());
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //12) Listar todos as faturas. Deverão ser exibidos os dados abaixo.
        System.out.println("12) Listar todoas as faturas:");
        faturaService.listarFaturasFormatado(faturaService.recuperarFaturas());
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //13) Cancelar o pedido 5 informando a data de cancelamento: 28/02/2025.
        System.out.println("13) Cancelar o pedido 5 informando a data de cancelamento: 28/02/2025:");
        try {
            pedidoService.cancelarPedido(
                    pedidoService.recuperarPedidoPorId(5).getCliente(),
                    pedidoService.recuperarPedidoPorId(5),
                    "28/02/2025"
            );
        } catch (PedidoCanceladoException | PedidoIntegralmenteFaturadoException | PedidoFaturadoException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //14) Cancelar a fatura 3 informando a data de cancelamento: 06/01/2025.
        System.out.println("14) Cancelar a fatura 3 informando a data de cancelamento: 06/01/2025:");
        try{
            Fatura umaFatura = faturaService.recuperarFaturaPorId(3);
            faturaService.cancelarFatura(cliente_1, umaFatura, "06/01/2025");
        } catch(ClienteSemFaturaException | EntidadeNaoEncontradaException | FaturaCanceladaException |
                ClienteComNumeroInsuficienteDeFaturasException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //15) Remover a fatura 3.
        System.out.println("15) Remover a fatura 3:");
        try {
            faturaService.remover(3, cliente_1);
        } catch (EntidadeNaoEncontradaException | FaturaCanceladaException | ClienteSemFaturaException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //16) Remover a fatura 4.
        System.out.println("16) Remover a fatura 4:");
        try {
            faturaService.remover(4, cliente_1);
        } catch (FaturaCanceladaException | ClienteComNumeroInsuficienteDeFaturasException |
                 PedidoIntegralmenteFaturadoException e) {
            System.out.println(e.getMessage() + "\n");
        }
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //17) Listar os Livros com suas respectivas quantidades em estoque. Deverão ser exibidos os seguintes dados:
        System.out.println("17) Listar os Livros com suas respectivas quantidades em estoque:");
        livroService.listarLivros(livroService.recuperarLivros());
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //18) Abastecer o estoque adicionando as quantidades abaixo aos livros:
        System.out.println("18) Abastecer o estoque adicionando as quantidades abaixo aos livros:");
        livroService.alterarQtdEstoque(livroService.recuperarLivroPorId(1), livroService.recuperarLivroPorId(1).getQtdEstoque() + 100);
        livroService.alterarQtdEstoque(livroService.recuperarLivroPorId(2), livroService.recuperarLivroPorId(2).getQtdEstoque() + 200);
        livroService.alterarQtdEstoque(livroService.recuperarLivroPorId(3), livroService.recuperarLivroPorId(3).getQtdEstoque() + 300);
        livroService.alterarQtdEstoque(livroService.recuperarLivroPorId(4), livroService.recuperarLivroPorId(4).getQtdEstoque() + 400);
        livroService.alterarQtdEstoque(livroService.recuperarLivroPorId(5), livroService.recuperarLivroPorId(5).getQtdEstoque() + 500);
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //19) Listar os Livros com suas respectivas quantidades em estoque. Deverão ser exibidos os seguintes dados:
        System.out.println("19) Listar os Livros com suas respectivas quantidades em estoque:");
        livroService.listarLivros(livroService.recuperarLivros());
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");


        //20) Faturar os pedidos 1 a 5, nesta ordem, para o mês de fevereiro de 2025.
        System.out.println("20) Faturar os pedidos de 1 a 5, nesta ordem, para o mês de fevereiro de 2025:\n");
        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_1, "01/02/2025");
            faturaService.incluir(fatura, pedido_1);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_2, "01/02/2025");
            faturaService.incluir(fatura, pedido_2);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_3, "01/02/2025");
            faturaService.incluir(fatura, pedido_3);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_4, "01/02/2025");
            faturaService.incluir(fatura, pedido_4);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            Fatura fatura = faturaService.faturarPedido(cliente_1, pedido_5, "01/02/2025");
            faturaService.incluir(fatura, pedido_5);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");


        //21) Executar o relatório 1 - Lista contendo a quantidade faturada, o nome do livro e a data da fatura de itens faturados do livro 1 para janeiro de 2025. Deverá ser exibido:
        System.out.println("21) Executar o relatorio1:");
        faturaService.relatorio1(faturaService.recuperarFaturas(), livroService.recuperarLivroPorId(1), 1, 2025);
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //22) Executar o relatório 2
        System.out.println("22) Executar o relatorio2:");
        faturaService.relatorio2(livroService.recuperarLivros());
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

        //23) Executar o relatório 3
        System.out.println("23) Executar o relatorio3:");
        faturaService.relatorio3(cliente_1.getFaturas(), livroService.recuperarLivros(), 2, 2025);
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");


    }
}
