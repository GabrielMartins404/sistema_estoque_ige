package com.estoqueige.estoqueige.models.movimentacao.events;
import lombok.Getter;

public class MovimentacaoFinalizadaEvent {
    @Getter
    private final Long movimentacaoId;

    public MovimentacaoFinalizadaEvent(Long movimentacaoId) {
        this.movimentacaoId = movimentacaoId;
    }
}
