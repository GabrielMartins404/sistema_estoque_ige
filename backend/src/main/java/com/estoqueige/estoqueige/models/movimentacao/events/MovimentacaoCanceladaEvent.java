package com.estoqueige.estoqueige.models.movimentacao.events;

import lombok.Getter;

// Representa o fato de que uma movimentação foi cancelada.
public class MovimentacaoCanceladaEvent {
    @Getter
    private final Long movimentacaoId;

    public MovimentacaoCanceladaEvent(Long movimentacaoId) {
        this.movimentacaoId = movimentacaoId;
    }
}
