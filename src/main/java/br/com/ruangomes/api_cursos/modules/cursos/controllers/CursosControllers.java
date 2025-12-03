package br.com.ruangomes.api_cursos.modules.cursos.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.ruangomes.api_cursos.modules.cursos.dto.ProfileCursoResponseDTO;
import br.com.ruangomes.api_cursos.modules.cursos.entities.CursosEntity;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.CreateCursoUseCase;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.ListCursosUseCase;
import br.com.ruangomes.api_cursos.modules.cursos.useCases.ProfileCursoUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/professor/")
public class CursosControllers {

    @Autowired
    private CreateCursoUseCase createCursoUseCase;

    @Autowired
    private ProfileCursoUseCase profileCursoUseCase;

    @Autowired
    private ListCursosUseCase listCursosUseCase;

    @PostMapping("/cursos")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Object> create( @Valid @RequestBody CursosEntity cursosEntity) {
        try {
            var result = this.createCursoUseCase.execute(cursosEntity);
            return ResponseEntity.ok().body(result);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
        
    }    

    @GetMapping("/cursos/")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<ProfileCursoResponseDTO>> listarCursos() {
        var result = this.listCursosUseCase.execute();
        return ResponseEntity.ok().body(result);
    }
    
    
    
}
