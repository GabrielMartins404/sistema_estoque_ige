package com.estoqueige.estoqueige.dto;

import com.estoqueige.estoqueige.models.Produto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


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
