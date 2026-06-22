package uni.edu.ni.Battle.ioAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.edu.ni.Battle.ioAPI.modelos.UsuarioUI;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioUI, Long> {

    Optional<UsuarioUI> findByEmail(String email);

    Optional<UsuarioUI> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}