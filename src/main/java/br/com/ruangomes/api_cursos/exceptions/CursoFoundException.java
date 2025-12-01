package br.com.ruangomes.api_cursos.exceptions;

public class CursoFoundException extends RuntimeException {
    public CursoFoundException() {
        super("Curso ou nome já cadastrado");
    }
    
}
