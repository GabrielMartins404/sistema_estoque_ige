package com.estoqueige.estoqueige.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarSenhaDto {
    @NotBlank
    private String novaSenha;

    @NotBlank
    private String senhaAntiga;
}
