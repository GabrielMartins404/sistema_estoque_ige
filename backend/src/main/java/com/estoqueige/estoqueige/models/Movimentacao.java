package com.estoqueige.estoqueige.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.estoqueige.estoqueige.models.enums.MovOrigem;
import com.estoqueige.estoqueige.models.enums.MovStatus;
import com.estoqueige.estoqueige.models.enums.MovTipo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
@Entity
@Table(name = Movimentacao.TABLE_NAME)
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "movId"
)
@AllArgsConstructor //Função que gera o construtor vazio
@NoArgsConstructor //Função que gera o contrutor com todos os argumentos
@Getter //Função que gera os get
@Setter //Função que gera os set
@EqualsAndHashCode //Função que gera o equals e HashCode
public class Movimentacao {
    public static final String TABLE_NAME = "movimentacao";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movId", unique = true)
    private Long movId;

    @Column(name = "movNf", nullable = true)
    private Long movNf;

    @Column(name = "movNumRequisicao", nullable = true)
    private Long movNumRequisicao;

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

    /*
    Nesse caso, a classe movimentação receberá uma lista de vários produtos, contudo, no banco de dados
    a ID da requisição deverá ser passado a movimentação
    */
    @OneToMany(mappedBy = "proMovMovimentacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // Definição do lado "pai"
    private List<ProdutoMovimentacao> produtosMov;

}
