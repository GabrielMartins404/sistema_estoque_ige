package com.estoqueige.estoqueige.models.produtoMovimentacao;

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
import lombok.Data;
import lombok.NoArgsConstructor;

import com.estoqueige.estoqueige.models.movimentacao.Movimentacao;
import com.estoqueige.estoqueige.models.produto.Produto;

@Entity
@Table(name = ProdutoMovimentacao.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
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
    private Float proMovCustoProduto = 0f;

    /* Definição das chaves estrangeiras */
    @ManyToOne
    @JoinColumn(name = "proMovProduto", nullable = false)
    private Produto proMovProduto;
    
    @ManyToOne
    @JoinColumn(name = "proMovMovimentacao", nullable = false)
    private Movimentacao proMovMovimentacao;
}
