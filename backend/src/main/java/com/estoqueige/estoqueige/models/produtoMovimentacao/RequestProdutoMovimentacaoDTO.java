package com.estoqueige.estoqueige.models.produtoMovimentacao;

public record RequestProdutoMovimentacaoDTO(
    Long proMovProduto,
    Float proMovQtdProduto,
    Float proMovCustoProduto

) {
}
