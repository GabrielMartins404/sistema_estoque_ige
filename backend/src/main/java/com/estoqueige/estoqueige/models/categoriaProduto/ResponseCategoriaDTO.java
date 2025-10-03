package com.estoqueige.estoqueige.models.categoriaProduto;

import java.util.List;
import java.util.stream.Collectors;

public record ResponseCategoriaDTO(
    Long catProId,
    String catProNome,
    Boolean isAtivo
) {
    public static ResponseCategoriaDTO fromEntity(CategoriaProduto categoriaProduto){
        return new ResponseCategoriaDTO(
            categoriaProduto.getCatProId(),
            categoriaProduto.getCatProNome(),
            categoriaProduto.getIsAtivo()
        );
    }
    public static List<ResponseCategoriaDTO> fromEntityList(List<CategoriaProduto> categoriaProdutos){
        return categoriaProdutos.stream().map(ResponseCategoriaDTO::fromEntity).collect(Collectors.toList());
    }
}

