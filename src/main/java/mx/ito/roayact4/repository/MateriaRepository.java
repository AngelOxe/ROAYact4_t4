package mx.ito.roayact4.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import mx.ito.roayact4.entity.Materia;

public interface MateriaRepository
        extends JpaRepository<Materia, Long> {

    @Override
    @EntityGraph(attributePaths = "area")
    Page<Materia> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "area")
    Optional<Materia> findById(Long id);
}