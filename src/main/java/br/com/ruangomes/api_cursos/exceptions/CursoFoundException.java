package br.com.ruangomes.api_cursos.exceptions;

public class CursoFoundException extends RuntimeException {
    public CursoFoundException() {
        super("Nome de curso já cadastrado");
    }
    
}
