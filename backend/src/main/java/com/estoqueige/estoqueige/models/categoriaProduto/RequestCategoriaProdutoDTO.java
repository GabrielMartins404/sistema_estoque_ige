package com.estoqueige.estoqueige.models.categoriaProduto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestCategoriaProdutoDTO(
    @NotBlank(message = "O nome da categoria não pode ser nulo nem vazio")
    @Size(max = 255, message = "O nome da categoria deve ter no máximo 255 caracteres.")
    String catProNome
) {}
