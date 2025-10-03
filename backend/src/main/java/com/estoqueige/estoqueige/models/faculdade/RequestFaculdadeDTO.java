package com.estoqueige.estoqueige.models.faculdade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestFaculdadeDTO(
    @NotBlank(message = "O nome da faculdade não pode ser nulo nem vazio")
    @Size(max = 255, message = "O nome da faculdade deve ter no máximo 255 caracteres.")
    String facNome,

    @Size(max = 50, message = "O sigla da faculdade deve ter no máximo 50 caracteres.")
    String facSigla
) {
    
}
