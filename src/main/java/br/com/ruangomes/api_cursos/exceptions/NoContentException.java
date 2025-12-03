package br.com.ruangomes.api_cursos.exceptions;

public class NoContentException extends RuntimeException {
    public NoContentException() {
        super("No content available/registered");
    }
    
}
