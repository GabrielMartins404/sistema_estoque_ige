package com.estoqueige.estoqueige.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estoqueige.estoqueige.models.Requisicao;

@Repository
public interface RequisicaoRepository extends JpaRepository<Requisicao, Long>{
    
}
