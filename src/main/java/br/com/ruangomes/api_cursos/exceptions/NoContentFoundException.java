package br.com.ruangomes.api_cursos.exceptions;

public class NoContentFoundException extends RuntimeException {
    public NoContentFoundException() {
        super("No content available/registered");
    }

}
