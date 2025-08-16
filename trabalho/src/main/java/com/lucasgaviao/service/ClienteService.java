package com.lucasgaviao.service;

import com.lucasgaviao.dao.ClienteDAO;
import com.lucasgaviao.exception.ClienteComPedidosException;
import com.lucasgaviao.exception.EntidadeNaoEncontradaException;
import com.lucasgaviao.exception.SemClientesException;
import com.lucasgaviao.model.Cliente;
import com.lucasgaviao.util.FabricaDeDaos;

import java.util.List;

public class ClienteService {
    private final ClienteDAO clienteDAO = FabricaDeDaos.getDAO(ClienteDAO.class);

    public Cliente incluir(Cliente cliente) { return clienteDAO.incluir(cliente); }

    // Verifico se o Cliente existe
    public Cliente recuperarClientePorId(int id){
        Cliente cliente = clienteDAO.recuperarPorId(id);
        if (cliente == null)
            throw new EntidadeNaoEncontradaException(" \n Cliente inexistente!");
        return cliente;
    }

    // Verifico se o Cliente existe
    // Só permito a remoção do Cliente caso ele não tenha pedidos
    public Cliente remover(int id) {
        Cliente cliente = recuperarClientePorId(id);
        if (cliente == null) {
            throw new EntidadeNaoEncontradaException("Cliente inexistente.");
        }
        if (!(cliente.getPedidos().isEmpty())) {
            throw new ClienteComPedidosException(
                    "Este cliente possui pedidos e não pode ser removido.");
        }
        clienteDAO.remover(cliente.getId());
        return cliente;
    }

    public Cliente alterarCPF(Cliente cliente, String novoCPF){
        cliente.setCpf(novoCPF);
        return cliente;
    }

    public Cliente alterarNome(Cliente cliente, String novoNome){
        cliente.setNome(novoNome);
        return cliente;
    }

    public Cliente alterarEmail(Cliente cliente, String novoEmail){
        cliente.setEmail(novoEmail);
        return cliente;
    }

    public Cliente alterarTelefone(Cliente cliente, String novoTelefone){
        cliente.setTelefone(novoTelefone);
        return cliente;
    }

    public void listarClientes(List<Cliente> clientes){
        if(clientes.isEmpty()){
            throw new SemClientesException("\nNão há clientes!");
        }
        else{
            for (Cliente cliente : clientes) {
                System.out.println(cliente);
            }
        }
    }

    public List<Cliente> recuperarClientes(){
        return clienteDAO.recuperarTodos();
    }

}
