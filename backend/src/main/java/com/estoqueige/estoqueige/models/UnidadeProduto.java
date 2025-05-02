package com.estoqueige.estoqueige.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = UnidadeProduto.TABLE_NAME)
@AllArgsConstructor //Função que gera o construtor vazio
@NoArgsConstructor //Função que gera o contrutor com todos os argumentos
@Getter //Função que gera os get
@Setter //Função que gera os set
@EqualsAndHashCode //Função que gera o equals e HashCode
public class UnidadeProduto {
    public static final String TABLE_NAME = "unidadeProduto";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unId", unique = true)
    private Long unId;

    @Column(name = "unNome", length = 50, nullable = false)
    @NotBlank(message = "O nome da unidade não pode ser nulo nem vazio")
    private String unNome;

    @Column(name = "unSigla", length = 10, nullable = false)
    @NotBlank(message = "A sigla da unidade não pode ser nulo nem vazio")
    private String unSigla;

    @Column(name = "isAtivo", columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    private Boolean isAtivo = true;

    /* Anotações das chaves estrangeiras */
    @OneToMany(mappedBy = "proUn")
    //@JsonBackReference
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Produto> produtos = new ArrayList<>(); 
}
