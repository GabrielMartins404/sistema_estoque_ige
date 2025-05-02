package com.estoqueige.estoqueige.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ErroAutorizacao extends AccessDeniedException{
    public ErroAutorizacao(String mensagem){
        super(mensagem);
    }
}
