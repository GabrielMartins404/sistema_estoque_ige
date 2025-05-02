package com.estoqueige.estoqueige.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MovStatus {
    FINALIZADO("F"),
    CANCELADO("C");
    

    private final String statusMovimentacao;

    MovStatus(String statusMov) {
        this.statusMovimentacao = statusMov;
    }

    @JsonValue
    public String getStatusMovimentacao(){
        return this.statusMovimentacao;
    }

    public static MovStatus movStatus(String movStatus){
        if(movStatus.equals("F")){
            return MovStatus.FINALIZADO;
        }else if(movStatus.equals("C")){
            return MovStatus.CANCELADO;
        }else{
            throw new IllegalArgumentException("Código de status de movimentação inválido: " + movStatus);
        }
    }
}
