package com.estoqueige.estoqueige.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estoqueige.estoqueige.models.Usuario;
import com.estoqueige.estoqueige.services.UsuarioServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UsuarioServices usuarioServices;

    @PostMapping("/validarToken")
    public ResponseEntity<?> validarToken(@RequestHeader("Authorization") String authHeader) {
        Usuario usuario = this.usuarioServices.validarToken(authHeader);

        if(usuario != null){
            return ResponseEntity.ok(usuario);
        }

        return ResponseEntity.status(401).body("Token inválido ou expirado. Autentique-se novamente");
        
    }
    
}
