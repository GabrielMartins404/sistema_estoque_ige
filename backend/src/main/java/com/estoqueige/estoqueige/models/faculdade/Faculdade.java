package com.estoqueige.estoqueige.models.faculdade;

import java.util.ArrayList;
import java.util.List;

import com.estoqueige.estoqueige.models.requisitante.Requisitante;

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
@Table(name = Faculdade.TABLE_NAME)
@AllArgsConstructor //Função que gera o construtor vazio
@NoArgsConstructor //Função que gera o contrutor com todos os argumentos
@Data
public class Faculdade {
    public static final String TABLE_NAME = "faculdade";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facId", unique = true)
    private Long facId;

    @Column(name = "facNome", length = 255, nullable = false)
    @NotBlank(message = "O nome da faculdade não pode ser nulo nem vazio")
    @Size(max = 255, message = "O nome da faculdade deve ter no máximo 255 caracteres.")
    private String facNome;

    @Column(name = "facSigla", length = 50, nullable = true)
    @Size(max = 50, message = "O sigla da faculdade deve ter no máximo 50 caracteres.")
    private String facSigla;

    @Column(name = "isAtivo", columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    private Boolean isAtivo = true;

    /* Anotações das chaves estrangeiras */
    @OneToMany(mappedBy = "facRequisitante")
    private List<Requisitante> requisitantes = new ArrayList<>(); 
}
