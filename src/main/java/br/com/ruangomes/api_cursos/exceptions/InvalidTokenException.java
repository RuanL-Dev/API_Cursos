package br.com.ruangomes.api_cursos.exceptions;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Não autorizado - Token inválido ou ausente");
    }

}
