package uni.edu.ni.Battle.ioAPI.repository;

import uni.edu.ni.Battle.ioAPI.modelos.UsuarioUI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioUI, Long> {
    UsuarioUI findByEmail(String email);
    UsuarioUI findByUsername(String username);
}
