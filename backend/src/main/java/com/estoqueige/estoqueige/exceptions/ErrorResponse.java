package com.estoqueige.estoqueige.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor //Defino o construtor somente para as classes com final
@JsonInclude(JsonInclude.Include.NON_NULL) //Aqui só retorna no JSON o que não for NULL
public class ErrorResponse {
    private final int status;
    private final String mensagem;
    private String pilhaDeErros;
    private List<ValidationError> errors;

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class ValidationError { //Aqui é gerado uma classe static que lida com as mensagens e campos a quais ocorreu o erro
        private final String field;
        private final String mensagem;
        
    }


    //Esse método adiciona a List de ValidationErrors os erros que ocorrerem ao decorrer do programa
    public void addValidationError(String field, String mensagem){
        if(Objects.isNull(errors)){
            this.errors = new ArrayList<>();
        }

        this.errors.add(new ValidationError(field, mensagem));
    }

    public String toJson(){
        return "{\"status\": "+getStatus() + ", "+
                "\"message\": \"" + getMensagem() + "\"}";
    }
}
