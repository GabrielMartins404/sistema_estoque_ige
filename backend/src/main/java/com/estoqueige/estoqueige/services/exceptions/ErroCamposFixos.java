package com.estoqueige.estoqueige.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ErroCamposFixos extends RuntimeException{
    public ErroCamposFixos(String mensagem){
        super(mensagem);
    }
}
