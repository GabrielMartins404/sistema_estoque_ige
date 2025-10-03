package com.estoqueige.estoqueige.models.produto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.estoqueige.estoqueige.models.categoriaProduto.CategoriaProduto;
import com.estoqueige.estoqueige.models.unidadeProduto.UnidadeProduto;

public record ResponseProdutoDTO(
    Long proId,
    String proNome,
    String proSipac,
    String proDescricao,
    Float proCusto,
    Float proEstoqueMin,
    Float proQtd,
    Boolean isAbaixoMin,
    Boolean isAtivo,
    Long proUnId,
    String proUnNome,
    Long proCategoriaId,
    String proCategoriaNome
) {
    public static ResponseProdutoDTO fromEntity(Produto produto){
        String unidade = Optional.ofNullable(produto.getProUn())
            .map(UnidadeProduto::getUnNome)
            .orElse(null);
        
        Long unidadeId = Optional.ofNullable(produto.getProUn())
            .map(UnidadeProduto::getUnId)
            .orElse(null);
        
        Long categoriaId = Optional.ofNullable(produto.getProCategoria())
            .map(CategoriaProduto::getCatProId)
            .orElse(null);
        
        String categoria = Optional.ofNullable(produto.getProCategoria())
            .map(CategoriaProduto::getCatProNome)
            .orElse(null);

        return new ResponseProdutoDTO(
            produto.getProId(),
            produto.getProNome(),
            produto.getProSipac(),
            produto.getProDescricao(),
            produto.getProCusto(),
            produto.getProEstoqueMin(),
            produto.getProQtd(),
            produto.getIsAbaixoMin(),
            produto.getIsAtivo(),
            unidadeId,
            unidade,
            categoriaId,
            categoria
        );
    }
    public static List<ResponseProdutoDTO> fromEntityList(List<Produto> produtoList){
        return produtoList.stream()
            .map(ResponseProdutoDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
