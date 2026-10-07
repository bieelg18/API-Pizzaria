package dev.bieelg18.Pizzaria.exception;

public class ClienteInvalidoException extends RuntimeException{

    public ClienteInvalidoException(String mensagem){
        super(mensagem);
    }

}
