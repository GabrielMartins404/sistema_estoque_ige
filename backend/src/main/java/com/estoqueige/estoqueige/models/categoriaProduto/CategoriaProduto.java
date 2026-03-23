
package com.estoqueige.estoqueige.models.categoriaProduto;

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
@Table(name = CategoriaProduto.TABLE_NAME)
@AllArgsConstructor //Função que gera o construtor vazio
@NoArgsConstructor //Função que gera o contrutor com todos os argumentos
@Data
public class CategoriaProduto {
    public static final String TABLE_NAME = "categoriaProduto";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catProId", unique = true)
    private Long catProId;

    @Column(name = "catProNome", length = 255, nullable = false)
    @NotBlank(message = "O nome da categoria não pode ser nulo nem vazio")
    @Size(max = 255, message = "O nome da categoria deve ter no máximo 255 caracteres.")
    private String catProNome;

    @Column(name = "isAtivo", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isAtivo = true;

    /* Anotações das chaves estrangeiras */
    @OneToMany(mappedBy = "proCategoria")
    private List<Produto> produtos = new ArrayList<>(); 
}
