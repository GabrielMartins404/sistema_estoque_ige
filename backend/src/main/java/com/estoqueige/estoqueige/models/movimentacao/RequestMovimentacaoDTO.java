package com.estoqueige.estoqueige.models.movimentacao;

import java.util.List;

import com.estoqueige.estoqueige.models.enums.MovOrigem;
import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.estoqueige.estoqueige.models.produtoMovimentacao.RequestProdutoMovimentacaoDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestMovimentacaoDTO(
    @Size(max = 255, message = "O número da nota fiscal deve ter no máximo 255 caracteres.")
    String movNf,
    String movNumRequisicao,
    
    String movObservacao,
    
    //Aqui uso Enums, que estão no pacote de enums
    @NotNull(message = "O status da movimentação é obrigatório")
    MovStatus movStatus,
    @NotNull(message = "O tipo da movimentação é obrigatório")
    MovTipo movTipo,
    @NotNull(message = "A origem da movimentação é obrigatória")
    MovOrigem movOrigem,

    /* Chaves estrangeiras */
    @NotNull(message = "O ID do requisitante é obrigatório")
    Long movRequisitanteId,

    @NotNull(message = "A lista de produtos é obrigatória")
    @NotEmpty(message = "A lista de produtos não pode estar vazia")
    List<RequestProdutoMovimentacaoDTO> produtosMov
) {
    
}
