package com.estoqueige.estoqueige.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MovOrigem {
    REQUISICAO("REQ"),
    NORMAL("NOR");

    private final String origemMovimentacao;

    MovOrigem(String origemMov) {
        this.origemMovimentacao = origemMov;
    }

    @JsonValue
    public String getOrigemMovimentacao(){
        return this.origemMovimentacao;
    }

    public static MovOrigem movOrigem(String movOrigem){
        if(movOrigem.equals("REQ")){
            return MovOrigem.REQUISICAO;
        }else if(movOrigem.equals("NOR")){
            return MovOrigem.NORMAL;
        }else{
            throw new IllegalArgumentException("Código de origem de movimentação inválido: " + movOrigem);
        }
    }
}
