package mx.ito.roayact4.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.ito.roayact4.dto.AreaResponse;
import mx.ito.roayact4.dto.MateriaRequest;
import mx.ito.roayact4.dto.MateriaResponse;
import mx.ito.roayact4.dto.PaginaResponse;
import mx.ito.roayact4.entity.Area;
import mx.ito.roayact4.entity.Materia;
import mx.ito.roayact4.exception.RecursoNoEncontradoException;
import mx.ito.roayact4.repository.MateriaRepository;

@Service
@Transactional(readOnly = true)
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final AreaService areaService;

    public MateriaService(
            MateriaRepository materiaRepository,
            AreaService areaService) {

        this.materiaRepository = materiaRepository;
        this.areaService = areaService;
    }

    public PaginaResponse<MateriaResponse> listar(Pageable pageable) {
        Page<Materia> pagina = materiaRepository.findAll(pageable);

        return new PaginaResponse<>(
            pagina.getContent()
                .stream()
                .map(this::convertirRespuesta)
                .toList(),
            pagina.getNumber(),
            pagina.getSize(),
            pagina.getTotalElements(),
            pagina.getTotalPages(),
            pagina.isLast()
        );
    }

    public MateriaResponse buscarPorId(Long id) {
        Materia materia = buscarEntidad(id);
        return convertirRespuesta(materia);
    }

    @Transactional
    public MateriaResponse crear(MateriaRequest request) {
        Area area = areaService.buscarEntidad(request.areaId());

        Materia materia = new Materia();
        materia.setNombre(request.nombre().strip());
        materia.setArea(area);

        Materia guardada = materiaRepository.save(materia);

        return convertirRespuesta(guardada);
    }

    @Transactional
    public MateriaResponse actualizar(
            Long id,
            MateriaRequest request) {

        Materia materia = buscarEntidad(id);
        Area area = areaService.buscarEntidad(request.areaId());

        materia.setNombre(request.nombre().strip());
        materia.setArea(area);

        Materia actualizada = materiaRepository.save(materia);

        return convertirRespuesta(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        Materia materia = buscarEntidad(id);
        materiaRepository.delete(materia);
    }

    private Materia buscarEntidad(Long id) {
        return materiaRepository.findById(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "La materia con ID " + id + " no existe"
                )
            );
    }

    private MateriaResponse convertirRespuesta(Materia materia) {
        Area area = materia.getArea();

        AreaResponse areaResponse = new AreaResponse(
            area.getId(),
            area.getNombre()
        );

        return new MateriaResponse(
            materia.getId(),
            materia.getNombre(),
            areaResponse
        );
    }
}