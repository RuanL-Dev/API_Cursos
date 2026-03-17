package br.com.ruangomes.api_cursos.exceptions;

public class ProfessorNotFoundException extends RuntimeException {
    public ProfessorNotFoundException() {
        super("Professor not found");
    }
}
