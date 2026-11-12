package mx.ito.roayact4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.ito.roayact4.entity.Usuario;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}