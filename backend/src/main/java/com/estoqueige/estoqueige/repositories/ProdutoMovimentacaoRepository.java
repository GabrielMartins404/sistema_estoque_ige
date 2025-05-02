package com.estoqueige.estoqueige.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estoqueige.estoqueige.models.ProdutoMovimentacao;

@Repository
public interface ProdutoMovimentacaoRepository extends JpaRepository<ProdutoMovimentacao, Long> {
    
}
