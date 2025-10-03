package com.estoqueige.estoqueige.models.produtoRequisicao;

import com.estoqueige.estoqueige.models.produto.Produto;
import com.estoqueige.estoqueige.models.requisicao.Requisicao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = ProdutoRequisicao.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProdutoRequisicao {
    public static final String TABLE_NAME = "produtoRequisicao";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proReqId", unique = true)
    private Long proReqId;

    @Column(name = "proReqQtdProduto", nullable = false)
    private Float proReqQtdProduto;

     /* Definição das chaves estrangeiras */
    @ManyToOne
    @JoinColumn(name = "proReqProduto", nullable = false)
    private Produto proReqProduto;

    @ManyToOne
    @JoinColumn(name = "proReqRequisicao", nullable = false)
    private Requisicao proReqRequisicao;

}
