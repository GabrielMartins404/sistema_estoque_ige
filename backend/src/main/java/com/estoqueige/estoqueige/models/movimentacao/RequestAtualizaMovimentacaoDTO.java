package com.estoqueige.estoqueige.models.movimentacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestAtualizaMovimentacaoDTO(
    @Size(max = 255, message = "O número da nota fiscal deve ter no máximo 255 caracteres.")
    String movNf,
    @NotNull(message = "O número da requisição é obrigatório")
    String movNumRequisicao,
    String movObservacao
) {
    
}
