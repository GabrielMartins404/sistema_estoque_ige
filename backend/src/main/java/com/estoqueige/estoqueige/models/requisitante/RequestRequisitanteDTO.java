package com.estoqueige.estoqueige.models.requisitante;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestRequisitanteDTO(
    @NotBlank(message = "O nome do requisitante não pode ser vazio e nem nulo")
    @Size(max = 255, message = "O nome do requisitante deve ter no máximo 255 caracteres.")
    String reqNome,

    Long facRequisitanteId
) {
    
}
