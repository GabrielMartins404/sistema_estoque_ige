package com.estoqueige.estoqueige.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estoqueige.estoqueige.models.logMovimentacaoEstoque.LogMovimentacaoEstoque;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<LogMovimentacaoEstoque, Long>{
    
}
