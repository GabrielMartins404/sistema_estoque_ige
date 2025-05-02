package com.estoqueige.estoqueige.repositories.projections;

public interface ResumoQtdProdutoMovimentadoProjection {
    String getProduto();
    Long getQtdMov();
    Long getQtdTotal();
    Long getEstoque();
    Boolean getIsAbaixoMin();

}
