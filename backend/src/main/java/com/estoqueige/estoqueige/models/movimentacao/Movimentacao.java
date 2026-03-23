package com.estoqueige.estoqueige.models.movimentacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.estoqueige.estoqueige.models.enums.MovOrigem;
import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.estoqueige.estoqueige.models.produto.Produto;
import com.estoqueige.estoqueige.models.produtoMovimentacao.ProdutoMovimentacao;
import com.estoqueige.estoqueige.models.requisitante.Requisitante;
import com.estoqueige.estoqueige.models.usuario.Usuario;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacaoLogica;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Table(name = Movimentacao.TABLE_NAME)
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "movId"
)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Movimentacao {
    public static final String TABLE_NAME = "movimentacao";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movId", unique = true)
    private Long movId;

    @Column(name = "movNf", nullable = true)
    private String movNf;

    @Column(name = "movNumRequisicao", nullable = true)
    private String movNumRequisicao;

    @Column(name = "movObservacao", columnDefinition = "TEXT", nullable = true)
    private String movObservacao;

    @Column(name = "movData", nullable = false)
    private LocalDate movData;

    @Column(name = "movDataHorario", nullable = false)
    private LocalTime movHorario;

    @Column(name = "movDataCancelamento", nullable = true)
    private LocalDate movDataCancelamento;

    @Column(name = "movHorarioCancelamento", nullable = true)
    private LocalTime movHorarioCancelamento;

    //Aqui uso Enums, que estão no pacote de enums
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovStatus movStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovTipo movTipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovOrigem movOrigem;

    /* Chaves estrangeiras */

    @ManyToOne
    @JoinColumn(name = "movUsuario", nullable = false)
    private Usuario movUsuario;
    
    @ManyToOne
    @JoinColumn(name = "movRequisitante", nullable = false)
    private Requisitante movRequisitante;

    @OneToMany(mappedBy = "proMovMovimentacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoMovimentacao> produtosMov;

    public static Movimentacao criar(Usuario usuario, Requisitante requisitante, RequestMovimentacaoDTO dto, List<ItemParaMovimentacao> itens){
      Movimentacao movimentacao = new Movimentacao(
        null,
        dto.movNf(),
        dto.movNumRequisicao(),
        dto.movObservacao(),
        LocalDate.now(),
        LocalTime.now(),
        null,
        null,
        MovStatus.FINALIZADO,
        dto.movTipo(),
        dto.movOrigem(),
        usuario,
        requisitante,
        new ArrayList<>()
      );

      itens.forEach(item ->{
        movimentacao.adicionarItem(item);
      }
        
      );
      return movimentacao;
    }

    public void adicionarItem(ItemParaMovimentacao item){
      // Validação 1: Quantidade
      if (item.getQuantidade() <= 0) {
          throw new ErroValidacaoLogica("A quantidade para o produto '" + item.getProduto().getProNome() + "' deve ser positiva.");
      }

      // Validação 2: Produto Ativo
      if (!item.getProduto().getIsAtivo()) {
          throw new ErroValidacaoLogica("O produto '" + item.getProduto().getProNome() + "' está inativo e não pode ser movimentado.");
      }

      // Validação 3: Estoque para Saída
      if (this.movTipo == MovTipo.SAIDA && item.getQuantidade() > item.getProduto().getProQtd()) {
          throw new ErroValidacaoLogica("Estoque insuficiente para o produto '" + item.getProduto().getProNome() + "'. Disponível: " + item.getProduto().getProQtd() + ", Pedido: " + item.getQuantidade());
      }

      ProdutoMovimentacao novoItem = new ProdutoMovimentacao(
        null,
        item.getQuantidade(),
        item.getCusto(),
        item.getProduto(),
        this
      );
      this.getProdutosMov().add(novoItem);
      
    }

    @Getter
    @AllArgsConstructor
    public static class ItemParaMovimentacao {
        private final Produto produto;
        private final Float quantidade;
        private final Float custo;
    }
}
