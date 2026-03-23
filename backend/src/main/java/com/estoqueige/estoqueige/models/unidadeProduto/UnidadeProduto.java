package com.estoqueige.estoqueige.models.unidadeProduto;

import java.util.ArrayList;
import java.util.List;

import com.estoqueige.estoqueige.models.produto.Produto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = UnidadeProduto.TABLE_NAME)
@AllArgsConstructor //Função que gera o construtor vazio
@NoArgsConstructor //Função que gera o contrutor com todos os argumentos
@Data
public class UnidadeProduto {
    public static final String TABLE_NAME = "unidadeProduto";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unId", unique = true)
    private Long unId;

    @Column(name = "unNome", length = 100, nullable = false)
    @NotBlank(message = "O nome da unidade não pode ser nulo nem vazio")
    @Size(max = 100, message = "O nome do unidade deve ter no máximo 100 caracteres.")
    private String unNome;

    @Column(name = "unSigla", length = 50, nullable = false)
    @NotBlank(message = "A sigla da unidade não pode ser nulo nem vazio")
    @Size(max = 50, message = "A sigla da unidade deve ter no máximo 50 caracteres.")
    private String unSigla;

    @Column(name = "isAtivo", columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    private Boolean isAtivo = true;

    /* Anotações das chaves estrangeiras */
    @OneToMany(mappedBy = "proUn")
    private List<Produto> produtos = new ArrayList<>(); 
}
