package com.estoqueige.estoqueige.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.estoqueige.estoqueige.models.ProdutoMovimentacao;
import com.estoqueige.estoqueige.models.Requisitante;
import com.estoqueige.estoqueige.models.Usuario;
import com.estoqueige.estoqueige.models.enums.MovOrigem;
import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

//Essas classe serve unicamente para me auxiliar no retorno ao front
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacaoDto {
    private Long movId;
    private LocalDate movData;
    private LocalDate movDataCancelamento;
    private LocalTime movHorario;
    private LocalTime movHorarioCancelamento;
    private Long movNf;
    private Long movNumRequisicao;
    private String movObservacao;
    private MovStatus movStatus;
    private MovOrigem movOrigem;
    private MovTipo movTipo;
    private String movUsuario;
    private String movRequisitante;
    private List<ProdutoMovimentacaoDto> proMovProduto = null;

}
