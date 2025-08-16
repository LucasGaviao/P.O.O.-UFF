package com.lucasgaviao.service;

import com.lucasgaviao.dao.ItemFaturadoDAO;
import com.lucasgaviao.exception.EntidadeNaoEncontradaException;
import com.lucasgaviao.model.Fatura;
import com.lucasgaviao.model.ItemFaturado;
import com.lucasgaviao.util.FabricaDeDaos;

import java.util.List;

public class ItemFaturadoService {
    private final ItemFaturadoDAO itemFaturadoDAO = FabricaDeDaos.getDAO(ItemFaturadoDAO.class);

    public ItemFaturado incluir(ItemFaturado itemFaturado){
        return itemFaturadoDAO.incluir(itemFaturado);
    }

    public ItemFaturado recuperarItemFaturadoPorId(int id){
        ItemFaturado itemFaturado = itemFaturadoDAO.recuperarPorId(id);
        if (itemFaturado == null)
            throw new EntidadeNaoEncontradaException(" \n ItemFaturado inexistente!");
        return itemFaturado;
    }

    public ItemFaturado remover(int id) {
        ItemFaturado itemFaturado = recuperarItemFaturadoPorId(id);
        if (itemFaturado == null) {
            throw new EntidadeNaoEncontradaException("ItemFaturado inexistente.");
        }
        itemFaturadoDAO.remover(itemFaturado.getId());
        return itemFaturado;
    }

    public List<ItemFaturado> recuperarItemFaturados(){
        return itemFaturadoDAO.recuperarTodos();
    }

}
