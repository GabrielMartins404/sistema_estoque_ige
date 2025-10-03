package com.estoqueige.estoqueige.models.requisitante;

import com.estoqueige.estoqueige.models.faculdade.Faculdade;

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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = Requisitante.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Requisitante {
    public static final String TABLE_NAME = "requisitante";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reqId", unique = true)
    private Long reqId;

    @Column(name = "reqNome", length = 255, nullable = false)
    @NotBlank(message = "O nome do requisitante não pode ser vazio e nem nulo")
    @Size(max = 255, message = "O nome do requisitante deve ter no máximo 255 caracteres.")
    private String reqNome;

    @Column(name = "isAtivo", columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    @NotNull
    private Boolean isAtivo = true;

    /* Anotações das chaves estrangeiras */
    @ManyToOne
    @JoinColumn(name = "facRequisitante", nullable = true)
    private Faculdade facRequisitante;
}
