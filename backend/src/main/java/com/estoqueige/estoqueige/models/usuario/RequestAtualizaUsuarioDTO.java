package com.estoqueige.estoqueige.models.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestAtualizaUsuarioDTO(
    @Size(max = 255, message = "O nome do usuário deve ter no máximo 255 caracteres.")
    @NotBlank(message = "O nome do usuário não pode ser nem vazio e nem nulo")
    String usuNome,

    @NotNull(message = "O perfil do usuário não pode ser nulo")
    PerfisUsuario usuPerfil
) {
    
}
