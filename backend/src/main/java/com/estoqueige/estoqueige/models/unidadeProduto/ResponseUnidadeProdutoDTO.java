package com.estoqueige.estoqueige.models.unidadeProduto;

import java.util.List;
import java.util.stream.Collectors;

public record ResponseUnidadeProdutoDTO(
    Long unId,
    String unNome,
    String unSigla
) {
    public static ResponseUnidadeProdutoDTO fromEntity(UnidadeProduto unidadeProduto){
        return new ResponseUnidadeProdutoDTO(
            unidadeProduto.getUnId(),
            unidadeProduto.getUnNome(),
            unidadeProduto.getUnSigla()
        );
    }   
    public static List<ResponseUnidadeProdutoDTO> fromEntityList(List<UnidadeProduto> unidadeProdutoList){
        return unidadeProdutoList.stream().map(ResponseUnidadeProdutoDTO::fromEntity).collect(Collectors.toList());
    }
}
