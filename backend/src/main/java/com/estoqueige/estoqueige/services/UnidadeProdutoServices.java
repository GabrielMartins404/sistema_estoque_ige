package com.estoqueige.estoqueige.services;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.estoqueige.estoqueige.models.UnidadeProduto;
import com.estoqueige.estoqueige.repositories.UnidadeProdutoRepository;
import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;

@Service
public class UnidadeProdutoServices {
    private final UnidadeProdutoRepository unidadeProdutoRepository;
    
    public UnidadeProdutoServices(UnidadeProdutoRepository unidadeProdutoRepository) {
        this.unidadeProdutoRepository = unidadeProdutoRepository;
    }

    /* Métodos dos services */

    public UnidadeProduto buscarUnidadeProdutoPorId(Long id){
        Optional<UnidadeProduto> unidadeProduto = this.unidadeProdutoRepository.findById(id);
        return unidadeProduto.orElseThrow(() -> new ErroAoBuscarObjetos("Falha ao buscar Unidade do Produto por ID: "+ id));
    }

    public List<UnidadeProduto> buscarTodasUnidadeProdutos(Boolean status){
        List<UnidadeProduto> unidadeProdutos = this.unidadeProdutoRepository.buscarUnidades(status);
        return unidadeProdutos;
    }

    @Transactional
    public UnidadeProduto cadastrarUnidadeProduto(UnidadeProduto unidadeProduto){
        unidadeProduto.setUnId(null);
        return this.unidadeProdutoRepository.save(unidadeProduto);
    }

    @Transactional
    public UnidadeProduto atualizarUnidadeProduto(UnidadeProduto unidadeProduto){
        UnidadeProduto newUnidadeProduto = this.buscarUnidadeProdutoPorId(unidadeProduto.getUnId());

        BeanUtils.copyProperties(unidadeProduto, newUnidadeProduto, "unId");
        this.unidadeProdutoRepository.save(newUnidadeProduto);
        return newUnidadeProduto;
    }

     public UnidadeProduto alterarStatusAtivoUnidadeProduto(Long id) {
        UnidadeProduto UnidadeProduto = this.buscarUnidadeProdutoPorId(id);

        UnidadeProduto.setIsAtivo(!UnidadeProduto.getIsAtivo());
        return this.atualizarUnidadeProduto(UnidadeProduto);
    }
}
