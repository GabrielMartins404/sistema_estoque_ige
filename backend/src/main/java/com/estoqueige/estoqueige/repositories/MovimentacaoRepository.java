package com.estoqueige.estoqueige.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estoqueige.estoqueige.models.Movimentacao;
import com.estoqueige.estoqueige.repositories.projections.ResumoRequisitantesProdutosProjection;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    @Query(value = "SELECT * FROM movimentacao WHERE mov_tipo = :tipo AND mov_status = :status", nativeQuery = true)
    List<Movimentacao> buscarMovimentacaosPorTipo(@Param("tipo")String tipo, @Param("status")String status);
    
    //Essa query serve para retornar as quantidades de movimentações. Está presente na tela de dashboard
    @Query(value = """
        select count(m.mov_id) as QTD_MOV from movimentacao as m
        where month(m.mov_data) = month(curdate())
        AND YEAR(m.mov_data) = YEAR(CURDATE())
        and mov_tipo = :tipo
        and mov_status = :status
    """, nativeQuery = true)
    Long buscarQtdMov(@Param("tipo")String tipo, @Param("status")String status);

    //Essa query serve para buscar quais os requisintates que mais solicitara produtos
    @Query(value = """
        SELECT 
            r.req_nome AS requisitante,
            COUNT(DISTINCT m.mov_id) AS totalMovimentacao,
            SUM(pe.pro_mov_qtd_produto) AS totalProdutos
        FROM produto_movimentacao AS pe
            INNER JOIN produto AS p ON p.pro_id = pe.pro_mov_produto
            INNER JOIN movimentacao AS m ON m.mov_id = pe.pro_mov_movimentacao
            INNER JOIN requisitante AS r ON r.req_id = m.mov_requisitante
        WHERE 
            m.mov_tipo = 'SAIDA' 
            AND m.mov_status = 'FINALIZADO' 
            AND MONTH(m.mov_data) = MONTH(CURDATE())
            AND YEAR(m.mov_data) = YEAR(CURDATE())
        GROUP BY r.req_id
        ORDER BY totalProdutos DESC
        LIMIT 10
        """, nativeQuery = true)
        List<ResumoRequisitantesProdutosProjection> buscarRequisitantesComMaisProdutos();

}
