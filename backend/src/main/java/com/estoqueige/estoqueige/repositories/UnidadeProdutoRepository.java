package com.estoqueige.estoqueige.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estoqueige.estoqueige.models.UnidadeProduto;

@Repository
public interface UnidadeProdutoRepository extends JpaRepository<UnidadeProduto, Long> {
    @Query(value = "SELECT * FROM unidade_produto WHERE is_ativo = :status", nativeQuery = true)
    List<UnidadeProduto> buscarUnidades(@Param("status")Boolean status);
}
