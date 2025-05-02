package com.estoqueige.estoqueige.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ErroValidacaoLogica extends RuntimeException{
    public ErroValidacaoLogica(String mensagem){
        super(mensagem);
    }
}
