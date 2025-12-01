package br.com.ruangomes.api_cursos.modules.cursos.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ruangomes.api_cursos.exceptions.CursoFoundException;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.repositories.CursoRepository;

@Service
public class CreateCursoUseCase {
    
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CursosEntity execute(CursosEntity cursosEntity) {
        this.cursoRepository.findByNameOrCategory(cursosEntity.getName(), cursosEntity.getCategory())
        .ifPresent((user) -> {
            throw new CursoFoundException();
        });

        var password = passwordEncoder.encode(cursosEntity.getPassword());
        cursosEntity.setPassword(password);
    
        return this.cursoRepository.save(cursosEntity);
    }
}
