package dev.bieelg18.Pizzaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<String> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException e
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(ClienteInvalidoException.class)
    public ResponseEntity<String> tratarClienteInvalido(
            ClienteInvalidoException e
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(StatusIncorretoException.class)
    public ResponseEntity<String> tratarStatusIncorreto(
            StatusIncorretoException e
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

}
