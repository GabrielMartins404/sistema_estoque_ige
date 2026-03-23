package com.estoqueige.estoqueige.models.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestCadastroUsuarioDTO(
    @Size(max = 255, message = "O nome do usuário deve ter no máximo 255 caracteres.")
    @NotBlank(message = "O nome do usuário não pode ser nem vazio e nem nulo")
    String usuNome,

    @NotBlank(message = "O email do usuário não pode ser nem vazio e nem nulo")
    @Size(max = 255, message = "O e-mail do usuário deve ter no máximo 255 caracteres.")
    @Email(message = "O email do usuário precisa ser válido")
    String usuLogin,

    @NotBlank(message = "A senha do usuário não pode ser nem vazio e nem nulo")
    @Size(max = 100, message = "A senha do usuário deve ter no máximo 100 caracteres.")
    String usuSenha,

    
    PerfisUsuario usuPerfil
) {
    
}
