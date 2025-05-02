package com.estoqueige.estoqueige.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
public class RequisitanteDto {
    private Long reqId;
    private String reqNome;
    private Boolean isAtivo;
    private String reqFacNome = null;
    private String reqFacSigla = null;
    private Long reqFaqId = null;
}
