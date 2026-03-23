package com.estoqueige.estoqueige.models.usuario;

import java.util.List;
import java.util.stream.Collectors;

public record ResponseUsuarioDTO(
    Long usuId,
    String usuNome,
    String usuEmail,
    Integer usuPerfil,
    String usuPerfilDescricao
) {
    public static ResponseUsuarioDTO fromEntity(Usuario usuario){
        return new ResponseUsuarioDTO(
            usuario.getUsuId(),
            usuario.getUsuNome(),
            usuario.getUsuLogin(),
            usuario.getUsuPerfil().getCodigo(),
            usuario.getUsuPerfil().getDescricao() == "ROLE_ALMOXARIFADO" ? "Almoxarifado" : "Usuário"
        );
    }

    public static List<ResponseUsuarioDTO> fromEntity(List<Usuario> usuarios){
        return usuarios.stream().map(ResponseUsuarioDTO::fromEntity).collect(Collectors.toList());
    }
}
