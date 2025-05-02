package com.estoqueige.estoqueige.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.persistence.EntityNotFoundException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ErroAoBuscarObjetos extends EntityNotFoundException{
    public ErroAoBuscarObjetos(String mensagem){
        super(mensagem);
    }
}
