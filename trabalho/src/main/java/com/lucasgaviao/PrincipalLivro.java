package com.lucasgaviao;

import com.lucasgaviao.exception.EntidadeComListaNaoVaziaException;
import com.lucasgaviao.exception.EntidadeNaoEncontradaException;
import com.lucasgaviao.exception.SemLivrosException;
import com.lucasgaviao.model.Livro;
import com.lucasgaviao.service.LivroService;
import corejava.Console;

import java.util.List;

public class PrincipalLivro {
    // inicializando services:
    private final LivroService livroService = new LivroService();

    public void principal(){

        String isbn;
        String titulo;
        String descricao;
        int qtdEstoque;
        double preco;
        Livro umLivro;

        boolean continua = true;
        while(continua){

            System.out.println('\n' + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.println('\n' + "Selecione uma opção: ");
            System.out.println('\n' + "1. Cadastrar um Livro");
            System.out.println("2. Alterar um Livro");
            System.out.println("3. Remover um Livro");
            System.out.println("4. Listar todos os Livros");
            System.out.println("5. Voltar");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            System.out.println();

            switch (opcao) {
                case 1 -> {
                    isbn = Console.readLine("Insira o ISBN do livro:");
                    titulo = Console.readLine("Insira o titulo do livro:");
                    descricao = Console.readLine("Insira a descricao do livro:");
                    qtdEstoque = Console.readInt("Insira a quantidade:");
                    preco = Console.readInt("Insira o preco do livro:");
                    umLivro = new Livro(isbn, titulo, descricao, qtdEstoque, preco);
                    livroService.incluir(umLivro);
                    System.out.println("\nLivro cadastrado com sucesso!");

                }
                case 2 -> {
                    int id = Console.readInt("Insira o id do livro que você deseja alterar: ");

                    try {
                        umLivro = livroService.recuperarLivroPorId(id);
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' + "O que você deseja alterar?");
                    System.out.println('\n' + "1. ISBN");
                    System.out.println("2. Titulo");
                    System.out.println("3. Descricao");
                    System.out.println("4. QtdEstoque");
                    System.out.println("5. Preco");

                    int opcaoAlteracao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

                    switch (opcaoAlteracao) {
                        case 1 -> {
                            String novoISBN = Console.readLine("Insira o novo ISBN: ");
                            livroService.alterarIsbn(umLivro, novoISBN);
                            System.out.println('\n' + "Alteração do ISBN realizada com sucesso!");
                        }
                        case 2 -> {
                            String novoTitulo = Console.readLine("Insira o novo Titulo: ");
                            livroService.alterarTitulo(umLivro, novoTitulo);
                            System.out.println('\n' + "Alteração do Titulo realizada com sucesso!");
                        }

                        case 3 -> {
                            String novaDescricao = Console.readLine("Insira o novo Descricao: ");
                            livroService.alterarDescricao(umLivro, novaDescricao);
                            System.out.println('\n' + "Alteração da Descricao realizada com sucesso!");
                        }

                        case 4 -> {
                            int novaQtdEstoque = Console.readInt("Insira a nova QtdEstoque: ");
                            livroService.alterarQtdEstoque(umLivro, novaQtdEstoque);
                            System.out.println('\n' + "Alteração da QtdEstoque realizada com sucesso!");
                        }

                        case 5 -> {
                            int novoPreco = Console.readInt("Insira o novo Preco: ");
                            livroService.alterarPreco(umLivro, novoPreco);
                            System.out.println('\n' + "Alteração do Preco realizada com sucesso!");
                        }

                        default -> System.out.println('\n' + "Opção inválida!");
                    }
                }
                case 3 -> {
                    int id = Console.readInt("Insira o id do Livro que deseja remover: ");

                    try {
                        livroService.remover(id);
                        System.out.println('\n' + "Livro removido com sucesso!");
                    } catch (EntidadeNaoEncontradaException | EntidadeComListaNaoVaziaException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }
                case 4 -> {
                    List<Livro> livros = livroService.recuperarLivros();

                    try {
                        livroService.listarLivros(livros);
                    } catch (SemLivrosException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println("\nLivros listados com sucesso!");
                }
                case 5 ->{
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}
