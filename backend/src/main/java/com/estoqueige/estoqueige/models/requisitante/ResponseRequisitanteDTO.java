package com.estoqueige.estoqueige.models.requisitante;

import java.util.List;
import java.util.stream.Collectors;

public record ResponseRequisitanteDTO(
    Long reqId,
    String reqNome,
    Long facRequisitanteId,
    String facNome,
    String facSigla

) {
    public static ResponseRequisitanteDTO fromEntity(Requisitante requisitante){
        return new ResponseRequisitanteDTO(
            requisitante.getReqId(),
            requisitante.getReqNome(),
            requisitante.getFacRequisitante() != null ? requisitante.getFacRequisitante().getFacId() : null,
            requisitante.getFacRequisitante() != null ? requisitante.getFacRequisitante().getFacNome() : null,
            requisitante.getFacRequisitante() != null ? requisitante.getFacRequisitante().getFacSigla() : null
        );
    }
    public static List<ResponseRequisitanteDTO> fromEntityList(List<Requisitante> requisitanteList){
        return requisitanteList.stream().map(ResponseRequisitanteDTO::fromEntity).collect(Collectors.toList());
    }
}  
