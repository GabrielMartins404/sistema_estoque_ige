package com.estoqueige.estoqueige.dto;

import java.time.LocalDate;
import java.util.Objects;

import com.estoqueige.estoqueige.models.Movimentacao;
import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovEstoqueDto {
    private Long movEstId;
    private LocalDate movEstData;
    private MovTipo movEstTipo;
    private MovStatus movEstStatus;
    private Float movEstQtd;
    private Float movEstQtdAnterior;
    private Float movEstQtdPosterior;
    private Movimentacao movEstMovimentacao;


    public MovEstoqueDto() {
    }

    public MovEstoqueDto(Long movEstId, LocalDate movEstData, MovTipo movEstTipo, MovStatus movEstStatus, Float movEstQtd, Float movEstQtdAnterior, Float movEstQtdPosterior, Movimentacao movEstMovimentacao) {
        this.movEstId = movEstId;
        this.movEstData = movEstData;
        this.movEstTipo = movEstTipo;
        this.movEstStatus = movEstStatus;
        this.movEstQtd = movEstQtd;
        this.movEstQtdAnterior = movEstQtdAnterior;
        this.movEstQtdPosterior = movEstQtdPosterior;
        Movimentacao movimentacao = movEstMovimentacao;
        movimentacao.setProdutosMov(null);
        this.movEstMovimentacao = movimentacao;
    }
}
