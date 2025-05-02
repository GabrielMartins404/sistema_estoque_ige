package com.estoqueige.estoqueige.services;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.estoqueige.estoqueige.models.CategoriaProduto;
import com.estoqueige.estoqueige.repositories.CategoriaProdutoRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;

@Service
public class CategoriaProdutoServices {
    private final CategoriaProdutoRepository categoriaProdutoRepository;
    
    public CategoriaProdutoServices(CategoriaProdutoRepository categoriaProdutoRepository) {
        this.categoriaProdutoRepository = categoriaProdutoRepository;
    }

    /* Métodos dos services */

    public CategoriaProduto buscarCategoriaProdutoPorId(Long id){
        Optional<CategoriaProduto> categoriaProduto = this.categoriaProdutoRepository.findById(id);
        return categoriaProduto.orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar cagoria por ID: "+ id));
    }

    public List<CategoriaProduto> buscarTodasCategoriaProdutos(Boolean status){
        List<CategoriaProduto> categoriaProduto = this.categoriaProdutoRepository.buscarCategorias(status);
        return categoriaProduto;
    }

    @Transactional
    public CategoriaProduto cadastrarCategoriaProduto(CategoriaProduto categoriaProduto){
        categoriaProduto.setCatProId(null);
        return this.categoriaProdutoRepository.save(categoriaProduto);
    }

    @Transactional
    public CategoriaProduto atualizarCategoriaProduto(CategoriaProduto categoriaProduto){
        CategoriaProduto newCategoriaProduto = this.buscarCategoriaProdutoPorId(categoriaProduto.getCatProId());

        BeanUtils.copyProperties(categoriaProduto, newCategoriaProduto, "catProId");
        this.categoriaProdutoRepository.save(newCategoriaProduto);
        return newCategoriaProduto;
    }

     public CategoriaProduto alterarStatusAtivoCategoriaProduto(Long id) {
        CategoriaProduto categoriaProduto = this.buscarCategoriaProdutoPorId(id);

        categoriaProduto.setIsAtivo(!categoriaProduto.getIsAtivo());
        return this.atualizarCategoriaProduto(categoriaProduto);
    }
}
