package com.estoqueige.estoqueige.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
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


@Entity
@Table(name = Requisicao.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Requisicao {
    public static final String TABLE_NAME = "requisicao";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reqId", unique = true)
    private Long reqId;

    @Column(name = "reqData", nullable = false)
    @NotNull
    private LocalDate reqData;

    @Column(name = "reqDataCancelamento", nullable = true)
    private LocalDate reqDataCancelamento;

    @Column(name = "reqStatus", length = 2,nullable = false)
    @NotBlank
    private String reqStatus;

    @Column(name = "isAnonima", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isAnonima;

    /* Chaves estrangeiras */

    @ManyToOne
    @JoinColumn(name = "reqUsuario", nullable = true)
    private Usuario reqUsuario;
    
    @ManyToOne
    @JoinColumn(name = "reqRequisitante", nullable = true)
    private Requisitante reqRequisitante;

    @OneToMany(mappedBy = "proReqRequisicao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoRequisicao> proReqRequisicao;

}
