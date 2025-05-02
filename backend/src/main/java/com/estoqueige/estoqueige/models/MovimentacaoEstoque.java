package com.estoqueige.estoqueige.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = MovimentacaoEstoque.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter //Função que gera os get
@Setter //Função que gera os set
@EqualsAndHashCode //Função que gera o equals e HashCode
public class MovimentacaoEstoque {
    public static final String TABLE_NAME = "movimentacaoEstoque";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movEstId", unique = true)
    private Long movEstId;

    @Column(name = "movEstData", nullable = false)
    @NotNull
    private LocalDate movEstData;

    @Column(name = "movDataHorario", nullable = false)
    @NotNull
    private LocalTime movEstHorario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private MovTipo movEstTipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private MovStatus movEstStatus;

    @Column(name = "movEstQtd", nullable = false)
    @NotNull
    private Float movEstQtd;

    @Column(name = "movEstQtdAnterior", nullable = false)
    @NotNull
    private Float movEstQtdAnterior;

    @Column(name = "movEstQtdPosterior", nullable = false)
    @NotNull
    private Float movEstQtdPosterior;

    /* Chaves estrangeiras */

    @ManyToOne
    @JoinColumn(name = "movEstProduto", nullable = false)
    @JsonBackReference // Definição do lado "filho"
    private Produto movEstProduto;
    
    @ManyToOne
    @JoinColumn(name = "movEstMovimentacao", nullable = false)
    private Movimentacao movEstMovimentacao;
}
