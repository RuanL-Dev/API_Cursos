package br.com.ruangomes.api_cursos.exceptions;

public class ProfessorFoundException extends RuntimeException {
    public ProfessorFoundException() {
        super("Nome de professor já cadastrado");
    }
}
