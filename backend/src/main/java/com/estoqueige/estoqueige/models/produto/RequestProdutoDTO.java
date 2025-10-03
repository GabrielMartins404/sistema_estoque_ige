package com.estoqueige.estoqueige.models.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record RequestProdutoDTO(
    @NotBlank(message = "O nome do produto não pode ser vazio e nem nulo")
    @Size(max = 255, message = "O nome do produto deve ter no máximo 255 caracteres.")
    String proNome,

    @NotBlank(message = "O código SIPAC do produto não pode ser vazio e nem nulo")
    @Size(max = 100, message = "O código SIPAC do produto deve ter no máximo 100 caracteres.")
    String proSipac,
    String proDescricao,
    Float proCusto,

    @PositiveOrZero(message = "O estoque mínimo do produto deve ser maior ou igual a zero.")
    Float proEstoqueMin,
    Long proUnId,
    Long proCategoriaId
) {
    
}
