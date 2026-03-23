package com.estoqueige.estoqueige.models.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestAtualizaSenhaUsuarioDTO(
    @NotBlank(message = "A antiga senha do usuário não pode ser nem vazio e nem nulo")
    @Size(max = 100, message = "A antiga senha do usuário deve ter no máximo 100 caracteres.")
    String usuSenhaAntiga,

    @NotBlank(message = "A nova senha do usuário não pode ser nem vazio e nem nulo")
    @Size(max = 100, message = "A nova senha do usuário deve ter no máximo 100 caracteres.")
    String usuSenhaNova
) {
    
}
