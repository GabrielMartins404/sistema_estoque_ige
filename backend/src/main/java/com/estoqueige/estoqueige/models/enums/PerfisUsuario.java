package com.estoqueige.estoqueige.models.enums;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

//Esse enum tem como objetivo definir os perfis de permissões dos usuários
@AllArgsConstructor
@Getter
public enum PerfisUsuario {
    ADMIN(0, "ROLE_ADMIN"),
    ALMOXARIFADO(1, "ROLE_ALMOXARIFADO");

    private Integer codigo;
    private String descricao;

    public static PerfisUsuario perfisUsuario(Integer codigo){
        if(Objects.isNull(codigo)){
            return null;
        }

        for (PerfisUsuario perfil : PerfisUsuario.values()) {
            if(codigo.equals(perfil.codigo)){
                return perfil;
            }
        }
        
        throw new IllegalArgumentException("Perfil de usuário inválido: "+codigo);
    }
}
