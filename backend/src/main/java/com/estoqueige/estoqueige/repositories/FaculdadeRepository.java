package com.estoqueige.estoqueige.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estoqueige.estoqueige.models.Faculdade;
import com.estoqueige.estoqueige.models.Produto;

@Repository
public interface FaculdadeRepository extends JpaRepository<Faculdade, Long> {
    @Query(value = "SELECT * FROM faculdade WHERE is_ativo = :status", nativeQuery = true)
    List<Faculdade> buscarFaculdades(@Param("status")Boolean status);
}
