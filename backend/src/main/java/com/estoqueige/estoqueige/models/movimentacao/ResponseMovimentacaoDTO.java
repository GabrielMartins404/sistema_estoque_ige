package com.estoqueige.estoqueige.models.movimentacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.estoqueige.estoqueige.models.enums.MovOrigem;
import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.estoqueige.estoqueige.models.produtoMovimentacao.ResponseProdutoMovimentacaoDTO;

public record ResponseMovimentacaoDTO(
    Long movId,
    String movNf,
    String movNumRequisicao,
    String movObservacao,
    LocalDate movData,
    LocalTime movHorario,
    LocalDate movDataCancelamento,
    LocalTime movHorarioCancelamento,
    MovStatus movStatus,
    MovTipo movTipo,
    MovOrigem movOrigem,
    Long movUsuarioId,
    String movUsuarioNome,
    Long movRequisitanteId,
    String movRequisitanteNome,
    List<ResponseProdutoMovimentacaoDTO> produtos
) {
    public static ResponseMovimentacaoDTO fromEntity(Movimentacao movimentacao){
        return new ResponseMovimentacaoDTO(
            movimentacao.getMovId(),
            movimentacao.getMovNf(),
            movimentacao.getMovNumRequisicao(),
            movimentacao.getMovObservacao(),
            movimentacao.getMovData(),
            movimentacao.getMovHorario(),
            movimentacao.getMovDataCancelamento(),
            movimentacao.getMovHorarioCancelamento(),
            movimentacao.getMovStatus(),
            movimentacao.getMovTipo(),
            movimentacao.getMovOrigem(),
            movimentacao.getMovUsuario().getUsuId() != null ? movimentacao.getMovUsuario().getUsuId() : null,
            movimentacao.getMovUsuario().getUsuNome() != null ? movimentacao.getMovUsuario().getUsuNome() : null,
            movimentacao.getMovRequisitante().getReqId() != null ? movimentacao.getMovRequisitante().getReqId() : null,
            movimentacao.getMovRequisitante().getReqNome() != null ? movimentacao.getMovRequisitante().getReqNome() : null,
            ResponseProdutoMovimentacaoDTO.fromEntityList(movimentacao.getProdutosMov())
        );
    }

    public static List<ResponseMovimentacaoDTO> fromEntityList(List<Movimentacao> movimentacaoList){
        return movimentacaoList.stream().map(ResponseMovimentacaoDTO::fromEntity).collect(Collectors.toList());
    }
}
