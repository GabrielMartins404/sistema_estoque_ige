package com.estoqueige.estoqueige.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = Requisitante.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Requisitante {
    public static final String TABLE_NAME = "requisitante";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reqId", unique = true)
    private Long reqId;

    @Column(name = "reqNome", length = 100, nullable = false)
    @NotBlank(message = "O nome do requisitante não pode ser vazio e nem nulo")
    private String reqNome;

    @Column(name = "isAtivo", columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    @NotNull
    private Boolean isAtivo = true;

    /* Anotações das chaves estrangeiras */
    @ManyToOne
    @JoinColumn(name = "facRequisitante", nullable = true)
    private Faculdade facRequisitante;
}
