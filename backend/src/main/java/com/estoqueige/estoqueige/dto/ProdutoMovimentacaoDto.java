package com.estoqueige.estoqueige.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProdutoMovimentacaoDto {
    private ProdutoDto produto;

    private Float qtdProduto;


    public ProdutoMovimentacaoDto() {
    }

    public ProdutoMovimentacaoDto(ProdutoDto produto, Float qtdProduto) {
        this.produto = produto;
        this.qtdProduto = qtdProduto;
    }
    
}
