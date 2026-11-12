package mx.ito.roayact4.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.ito.roayact4.dto.AreaResponse;
import mx.ito.roayact4.entity.Area;
import mx.ito.roayact4.exception.RecursoNoEncontradoException;
import mx.ito.roayact4.repository.AreaRepository;

@Service
@Transactional(readOnly = true)
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<AreaResponse> listar() {
        return areaRepository.findAll(Sort.by("nombre"))
            .stream()
            .map(area -> new AreaResponse(
                area.getId(),
                area.getNombre()
            ))
            .toList();
    }

    public Area buscarEntidad(Long id) {
        return areaRepository.findById(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "El área con ID " + id + " no existe"
                )
            );
    }
}