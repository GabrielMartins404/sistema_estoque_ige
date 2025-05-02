package com.estoqueige.estoqueige.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;



@Entity
@Table(name = Produto.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Produto {
    public static final String TABLE_NAME = "produto"; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proId", unique = true)
    private Long proId;

    @Column(name = "proNome", length = 100, nullable = false)
    @NotBlank(message = "O nome do produto não pode ser vazio e nem nulo")
    private String proNome;

    @Column(name = "proSipac", length = 30, nullable = false)
    @NotBlank(message = "O código SIPAC do produto não pode ser vazio e nem nulo")
    private String proSipac;

    @Column(name = "proDescricao", columnDefinition = "TEXT", nullable = true)
    private String proDescricao;

    @Column(name = "proCusto", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private Float proCusto = 0f;

    @Column(name = "proQtd", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    @NotNull
    private Float proQtd;

    @Column(name = "proEstoqueMin", nullable = true, columnDefinition = "FLOAT DEFAULT 0")
    private Float proEstoqueMin = 0f;

    @Column(name = "isAbaixoMin", nullable = true, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isAbaixoMin = false;

    @Column(name = "isAtivo", columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    private Boolean isAtivo = true;

    /* Definição das chaves estrangeiras */
    @ManyToOne
    @JoinColumn(name = "proUn", nullable = false)
    private UnidadeProduto proUn;

    @ManyToOne
    @JoinColumn(name = "proCategoria", nullable = true)
    private CategoriaProduto proCategoria;

    @OneToMany(mappedBy = "proMovProduto")
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<ProdutoMovimentacao> produtoMovimentacoes = new ArrayList<>();

    @OneToMany(mappedBy = "proReqProduto")
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<ProdutoRequisicao> produtoRequisicoes = new ArrayList<>();

    @OneToMany(mappedBy = "movEstProduto")
    private List<MovimentacaoEstoque> movimentacaoEstoque = new ArrayList<>();

    
}
