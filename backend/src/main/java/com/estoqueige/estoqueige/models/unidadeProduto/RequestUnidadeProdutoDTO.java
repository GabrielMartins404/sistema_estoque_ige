package com.estoqueige.estoqueige.models.unidadeProduto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestUnidadeProdutoDTO(
    @NotBlank(message = "O nome da unidade não pode ser nulo nem vazio")
    @Size(max = 100, message = "O nome do unidade deve ter no máximo 100 caracteres.")
    String unNome,

    @NotBlank(message = "A sigla da unidade não pode ser nulo nem vazio")
    @Size(max = 50, message = "A sigla da unidade deve ter no máximo 50 caracteres.")
    String unSigla
) {
    
}
