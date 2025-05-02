package com.estoqueige.estoqueige.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = ProdutoMovimentacao.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProdutoMovimentacao {
    public static final String TABLE_NAME = "produtoMovimentacao";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proMovId", unique = true)
    private Long proMovId;

    @Column(name = "proMovQtdProduto", nullable = false)
    @NotNull
    @PositiveOrZero(message = "A quantidade do item na movimentação não pode ser negativo")
    private Float proMovQtdProduto;

    @Column(name = "proMovCustoProduto", nullable = false)
    @NotNull(message = "O custo do produto não pode ser nulo")
    private Float proMovCustoProduto = 0f;

    /* Definição das chaves estrangeiras */
    @ManyToOne
    @JoinColumn(name = "proMovProduto", nullable = false)
    @JsonBackReference // Definição do lado "filho"
    private Produto proMovProduto;
    
    @ManyToOne
    @JoinColumn(name = "proMovMovimentacao", nullable = false)
    @JsonBackReference // Definição do lado "filho"
    @JsonIgnore
    private Movimentacao proMovMovimentacao;

}
