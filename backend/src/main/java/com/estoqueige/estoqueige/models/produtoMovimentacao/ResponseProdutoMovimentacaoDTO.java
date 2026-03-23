package com.estoqueige.estoqueige.models.produtoMovimentacao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.estoqueige.estoqueige.models.produto.ResponseProdutoDTO;

public record ResponseProdutoMovimentacaoDTO(
    ResponseProdutoDTO produto,
    Float proMovQtdProduto,
    Float proMovCustoProduto
) {
    public static ResponseProdutoMovimentacaoDTO fromEntity(ProdutoMovimentacao produtoMovimentacao){
        ResponseProdutoDTO produtoDto = Optional.ofNullable(produtoMovimentacao.getProMovProduto())
            .map(ResponseProdutoDTO::fromEntity)
            .orElse(null);
            
        return new ResponseProdutoMovimentacaoDTO(
            produtoDto,
            produtoMovimentacao.getProMovQtdProduto(),
            produtoMovimentacao.getProMovCustoProduto()
        );
    }
    public static List<ResponseProdutoMovimentacaoDTO> fromEntityList(List<ProdutoMovimentacao> produtoMovimentacaoList){
        return produtoMovimentacaoList.stream()
            .map(ResponseProdutoMovimentacaoDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
