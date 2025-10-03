package com.estoqueige.estoqueige.models.faculdade;

import java.util.List;
import java.util.stream.Collectors;

public record ResponseFaculdadeDTO(
    Long facId,
    String facNome,
    String facSigla,
    Boolean isAtivo
) {
    public static ResponseFaculdadeDTO fromEntity(Faculdade faculdade) {
        return new ResponseFaculdadeDTO(
            faculdade.getFacId(),
            faculdade.getFacNome(),
            faculdade.getFacSigla(),
            faculdade.getIsAtivo()
        );
    }
    public static List<ResponseFaculdadeDTO> fromEntityList(List<Faculdade> faculdades){
        return faculdades.stream().map(ResponseFaculdadeDTO::fromEntity).collect(Collectors.toList());
    }
}
