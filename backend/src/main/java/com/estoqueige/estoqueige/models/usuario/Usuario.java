package com.estoqueige.estoqueige.models.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Usuario.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Usuario {
    public static final String TABLE_NAME = "usuario";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuId", unique = true)
    private Long usuId;

    @Column(name = "usuNome", length = 100, nullable = false)
    @Size(max = 100, message = "O nome do usuário deve ter no máximo 100 caracteres.")
    @NotBlank(message = "O nome do usuário não pode ser nem vazio e nem nulo")
    private String usuNome;

    @Column(name = "usuLogin", length = 60, nullable = false, unique = true)
    @NotBlank(message = "O email do usuário não pode ser nem vazio e nem nulo")
    @Size(max = 100, message = "O e-mail do usuário deve ter no máximo 60 caracteres.")
    @Email(message = "O email do usuário precisa ser válido")
    private String usuLogin;

    @Column(name = "usuSenha", length = 60, nullable = false)
    @NotBlank(message = "A senha do usuário não pode ser nem vazio e nem nulo")
    @Size(max = 100, message = "A senha do usuário deve ter no máximo 60 caracteres.")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String usuSenha;

    @Column(name = "isAtivo", columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    @NotNull
    private Boolean isAtivo = true;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "usuPerfil", nullable = false)
    private PerfisUsuario usuPerfil;
}
