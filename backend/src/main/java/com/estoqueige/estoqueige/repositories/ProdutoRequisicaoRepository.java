package com.estoqueige.estoqueige.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estoqueige.estoqueige.models.produtoRequisicao.ProdutoRequisicao;

@Repository
public interface ProdutoRequisicaoRepository extends JpaRepository<ProdutoRequisicao, Long> {
    
}
