package com.estoqueige.estoqueige.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

//Esse enum tem como principal propósito, deixar fixo os valores de movimentacao, são
/*
 E -> Entrada
 S -> Saida
 */
public enum MovTipo {
    ENTRADA("E"), 
    SAIDA("S");
    
    private final String tipoMovimentacao;

    MovTipo(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }
    
    @JsonValue
    public String getTipoMovimentacao(){
        return this.tipoMovimentacao;
    }

    public static MovTipo movTipo(String tipoMov){
        if(tipoMov.equals("E")){
            return ENTRADA;
        }else if(tipoMov.equals("S")){
            return SAIDA;
        }else{
            throw new IllegalArgumentException("Código de tipo de movimentação inválido: " + tipoMov);
        }
    }
}
