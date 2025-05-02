package com.estoqueige.estoqueige.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estoqueige.estoqueige.models.Produto;
import com.estoqueige.estoqueige.repositories.projections.ResumoQtdProdutoMovimentadoProjection;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
    @Query(value = "SELECT * FROM produto WHERE is_ativo = :status", nativeQuery = true)
    List<Produto> buscarProdutos(@Param("status")Boolean status);

    //Aqui verifico se existe um produto com uma determinada descrição
    boolean existsByProDescricaoAndIsAtivoTrue(String descricao);


    /* No futuro, essas querys deverão ser refatoradas para um repository apropriado */
    //Essa query serve para retornar a qtd de produtos ativos. Está presente na tela de dashboard
    @Query(value = """
        SELECT COUNT(p.pro_id)
        FROM produto AS p
        WHERE p.is_ativo = 1
    """, nativeQuery = true)
    Long buscarQtdProdutosAtivos();

    //Essa query serve para retornar a qtd de produtos abaixo do estoque minimo. Está presente na tela de dashboard
    @Query(value = """
        select count(p.pro_id) as QTD_Produto 
        from produto as p
	    where p.is_ativo = 1 and p.is_abaixo_min=1
    """, nativeQuery = true)
    Long buscarProdutosAbaixoMin();

    //Essa query serve para buscar os produtos que mais deram saídas ou entradas. A principio será somente saidas
    @Query(value = """
    SELECT 
        p.pro_nome AS produto, 
        COUNT(p.pro_id) AS qtdMov, 
        SUM(pe.pro_mov_qtd_produto) AS qtdTotal, 
        p.pro_qtd AS estoque, 
        p.is_abaixo_min AS isAbaixoMin
    FROM produto_movimentacao AS pe
    INNER JOIN produto AS p ON p.pro_id = pe.pro_mov_produto
    INNER JOIN movimentacao AS m ON m.mov_id = pe.pro_mov_movimentacao
    WHERE 
        m.mov_tipo = 'SAIDA' 
        AND m.mov_status = 'FINALIZADO' 
        AND MONTH(m.mov_data) = MONTH(CURDATE())
        AND YEAR(m.mov_data) = YEAR(CURDATE())
    GROUP BY pe.pro_mov_produto
    ORDER BY qtdMov DESC
    LIMIT 10
    """, nativeQuery = true)
    List<ResumoQtdProdutoMovimentadoProjection> buscarProdutosMaisMovimentados();

}
