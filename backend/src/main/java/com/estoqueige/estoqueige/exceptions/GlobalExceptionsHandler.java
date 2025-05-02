package com.estoqueige.estoqueige.exceptions;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.estoqueige.estoqueige.services.exceptions.ErroAoBuscarObjetos;
import com.estoqueige.estoqueige.services.exceptions.ErroAutorizacao;
import com.estoqueige.estoqueige.services.exceptions.ErroCamposFixos;
import com.estoqueige.estoqueige.services.exceptions.ErroMovimentacaoCancelada;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacaoLogica;
import com.estoqueige.estoqueige.services.exceptions.ErroValidacoesObjRepetidos;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice //Adiciono para que o Spring inicie essa classe junto com a aplicação
@Slf4j(topic = "ERROR_CAPTURADOS_GLOBALMENTE") //Aqui defino um log para poder capturar os erros no console
public class GlobalExceptionsHandler extends ResponseEntityExceptionHandler implements AuthenticationFailureHandler{
    
    //Esse campo tem como propósito, definir se devo ou não mostrar as listas de erros
    //Só devo mostrar quando estiver em desenvolvimento
    @Value("${server.error.include-exception}") //Defino nas minhas propriedades quando devo tornar disponível essa parte acima
    private boolean mostrarPilhasDeErros; 

    //Esse método gerencia erros que ocorrem de campos não preenchidos corretamente, a quais são obrigatório no Banco de dados
    @Override
    //@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders headers, HttpStatusCode  status, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Ocorreu um erro nas validações dos campos! Verifique os 'erros' para maiores detalhes"
        );
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }
    

    //Esse método é chamado em cada erros de exceção, a qual retorna as pilhas de erros
    private ResponseEntity<Object> buildErrorResponse(Exception exception, String mensagem, HttpStatus httpStatus, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), mensagem);
        if(this.mostrarPilhasDeErros){
            errorResponse.setPilhaDeErros(ExceptionUtils.getStackTrace(exception));
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    //Mesmo método acima mas sem a mensagem
    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request){

        return buildErrorResponse(exception,exception.getMessage(), httpStatus, request);
    }

    //Método para erros em gerais, que envolvam a aplicação
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> errosDesconhecidos(Exception exception, WebRequest request){
        final String mensagemError = "Ocorreu um erro desconhecido";
        log.error(mensagemError, exception);
        return buildErrorResponse(exception, mensagemError, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    //Método para erros de integridade e duplicação de dados únicas
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> errosDeViolacaoDeIntegridadeDasEntidades(DataIntegrityViolationException dataIntegrityViolationException, WebRequest request){
        String mensagemError = dataIntegrityViolationException.getMostSpecificCause().getMessage();
        log.error("Falha ao salvar dados no Banco de dados: {}",mensagemError, dataIntegrityViolationException);
        return buildErrorResponse(dataIntegrityViolationException, mensagemError, HttpStatus.CONFLICT, request);
    }

    //Método para validação de Erro de autenticação
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> errosDeAutenticacao(AuthenticationException authenticationException, WebRequest request){
        log.error("Erro de autenticação: {}", authenticationException);
        return buildErrorResponse(authenticationException, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    //Método para validação de Erro de acesso negado
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> errosDeAutorizacao(AccessDeniedException accessDeniedException, WebRequest request){
        log.error("Permissão de usuário negada: {}", accessDeniedException);
        return buildErrorResponse(accessDeniedException, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    //Método para validação de Erro de elementos inválidos
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> errosDeViolacaoDeCampos(ConstraintViolationException constraintViolationException, WebRequest request){
        log.error("Falha ao validar elemento: {}", constraintViolationException);
        return buildErrorResponse(constraintViolationException, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /* Abaixo são erros personalizados */
    //Método para validar objetos não encontrados vindo dos services
    @ExceptionHandler(ErroAoBuscarObjetos.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> erroDeObjetosNaoEncontrados(ErroAoBuscarObjetos erroAoBuscarObjetos, WebRequest request){
        log.error("Falha ao buscar elemento elemento: {}", erroAoBuscarObjetos);
        return buildErrorResponse(erroAoBuscarObjetos, HttpStatus.NOT_FOUND, request);
    }

    //Método mais especilizado para evitar duplicidade de informações únicas
    @ExceptionHandler(ErroValidacoesObjRepetidos.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> errosDeDuplicidadeDeObjUnicos(ErroValidacoesObjRepetidos erroValidacoesObjRepetidos, WebRequest request){
        log.error("Falha ao inserir elemento. Dado repetido: {}", erroValidacoesObjRepetidos);
        return buildErrorResponse(erroValidacoesObjRepetidos, HttpStatus.CONFLICT, request);
    }

    //Método para impedir a inserção de itens com quantidade negativa
    @ExceptionHandler(ErroValidacaoLogica.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> erroQtdNegativa(ErroValidacaoLogica erroQtdNegativaProduto, WebRequest request){
        log.error("Não é possível inserir itens na movimentacao com estoque negativo: {}", erroQtdNegativaProduto);
        return buildErrorResponse(erroQtdNegativaProduto, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    //Método para impedir cancelar movimetação já cancelada
    @ExceptionHandler(ErroMovimentacaoCancelada.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> erroMovimentacaoCancelamento(ErroMovimentacaoCancelada erroMovimentacaoCancelada, WebRequest request){
        log.error("Não é possível cancelar a movimentação pois a mesma já está cancelada: {}", erroMovimentacaoCancelada);
        return buildErrorResponse(erroMovimentacaoCancelada, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

     //Método para violação de BadRequest de campos fixos
     @ExceptionHandler(ErroCamposFixos.class)
     @ResponseStatus(HttpStatus.BAD_REQUEST)
     public ResponseEntity<Object> handleBadRequestExceptions(ErroCamposFixos erroCamposFixos, WebRequest request) {
        
        log.error("Erro dos campos fixos: {}", erroCamposFixos);
        return buildErrorResponse(erroCamposFixos, HttpStatus.BAD_REQUEST, request);
     }

     //Método para falha de permissão
     @ExceptionHandler(ErroAutorizacao.class)
     @ResponseStatus(HttpStatus.UNAUTHORIZED)
     public ResponseEntity<Object> erroAutorizacaoUsuario(ErroAutorizacao erroAutorizacao, WebRequest request) {
        
        log.error("Usuário não possui permissão: {}", erroAutorizacao);
        return buildErrorResponse(erroAutorizacao, HttpStatus.BAD_REQUEST, request);
     }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Integer status = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(status);
        response.setContentType("application/json");

        ErrorResponse errorResponse = new ErrorResponse(status, "Login ou senha inválidos");
        response.getWriter().append(errorResponse.toJson());

    }
}
