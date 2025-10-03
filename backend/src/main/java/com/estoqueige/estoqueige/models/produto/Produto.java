package com.estoqueige.estoqueige.models.produto;

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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.estoqueige.estoqueige.models.categoriaProduto.CategoriaProduto;
import com.estoqueige.estoqueige.models.logMovimentacaoEstoque.LogMovimentacaoEstoque;
import com.estoqueige.estoqueige.models.produtoMovimentacao.ProdutoMovimentacao;
import com.estoqueige.estoqueige.models.produtoRequisicao.ProdutoRequisicao;
import com.estoqueige.estoqueige.models.unidadeProduto.UnidadeProduto;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacaoLogica;



@Entity
@Table(name = Produto.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Produto {
    public static final String TABLE_NAME = "produto"; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proId", unique = true)
    private Long proId;

    @Column(name = "proNome", length = 255, nullable = false)
    @NotBlank(message = "O nome do produto não pode ser vazio e nem nulo")
    @Size(max = 255, message = "O nome do produto deve ter no máximo 255 caracteres.")
    private String proNome;

    @Column(name = "proSipac", length = 100, nullable = false)
    @NotBlank(message = "O código SIPAC do produto não pode ser vazio e nem nulo")
    @Size(max = 100, message = "O código SIPAC do produto deve ter no máximo 100 caracteres.")
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
    private List<ProdutoMovimentacao> produtoMovimentacoes = new ArrayList<>();

    @OneToMany(mappedBy = "proReqProduto")
    private List<ProdutoRequisicao> produtoRequisicoes = new ArrayList<>();

    @OneToMany(mappedBy = "movEstProduto")
    private List<LogMovimentacaoEstoque> movimentacaoEstoque = new ArrayList<>();

    /* Métodos */
    public void darEntrada(Float quantidade) {
        this.proQtd += quantidade;
        this.verificarStatusEstoqueMinimo(proQtd);
    }

    public void darSaida(Float quantidade) {
        if (this.proQtd < quantidade) {
            throw new ErroValidacaoLogica("Estoque insuficiente para o produto " + this.proNome);
        }
        this.proQtd -= quantidade;
        this.verificarStatusEstoqueMinimo(proQtd);
    }

    public void verificarStatusEstoqueMinimo(Float qtd){
        if(qtd < proEstoqueMin){
            setIsAbaixoMin(true);
        }else{
            setIsAbaixoMin(false);
        }
    }
    
}
