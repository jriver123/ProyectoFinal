package uni.edu.ni.Battle.ioAPI.repository;

import uni.edu.ni.Battle.ioAPI.modelos.usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface usuarioRepo extends JpaRepository<usuario, Long> {
}
