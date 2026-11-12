package mx.ito.roayact4.controller;

import java.net.URI;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.ito.roayact4.dto.MateriaRequest;
import mx.ito.roayact4.dto.MateriaResponse;
import mx.ito.roayact4.dto.PaginaResponse;
import mx.ito.roayact4.exception.PeticionInvalidaException;
import mx.ito.roayact4.service.MateriaService;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public ResponseEntity<PaginaResponse<MateriaResponse>> listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {

        if (page < 0) {
            throw new PeticionInvalidaException(
                "La página no puede ser negativa"
            );
        }

        if (size < 1 || size > 100) {
            throw new PeticionInvalidaException(
                "El tamaño de página debe estar entre 1 y 100"
            );
        }

        PageRequest pageable = PageRequest.of(
            page,
            size,
            Sort.by("id").ascending()
        );

        return ResponseEntity.ok(materiaService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaResponse> buscarPorId(
            @PathVariable("id") Long id) {

        validarId(id);

        return ResponseEntity.ok(materiaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MateriaResponse> crear(
            @Valid @RequestBody MateriaRequest request) {

        MateriaResponse creada = materiaService.crear(request);

        URI ubicacion = URI.create("/api/materias/" + creada.id());

        return ResponseEntity.created(ubicacion).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaResponse> actualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody MateriaRequest request) {

        validarId(id);

        return ResponseEntity.ok(
            materiaService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(
            @PathVariable("id") Long id) {

        validarId(id);
        materiaService.eliminar(id);

        return ResponseEntity.ok(
            Map.of("mensaje", "Materia eliminada correctamente")
        );
    }

    private void validarId(Long id) {
        if (id <= 0) {
            throw new PeticionInvalidaException(
                "El ID debe ser mayor que cero"
            );
        }
    }
}