package com.estoqueige.estoqueige.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ErroValidacoesObjRepetidos extends DataIntegrityViolationException{
    public ErroValidacoesObjRepetidos(String mensagem){
        super(mensagem);
    }
}
