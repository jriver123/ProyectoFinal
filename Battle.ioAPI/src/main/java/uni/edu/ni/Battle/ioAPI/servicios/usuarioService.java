package uni.edu.ni.Battle.ioAPI.servicios;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.edu.ni.Battle.ioAPI.modelos.usuario;
import uni.edu.ni.Battle.ioAPI.repository.usuarioRepo;
import java.util.List;

@Service
public class usuarioService {
    @Autowired
    private usuarioRepo usuarioRepo;

    public List<usuario> getAllUsuarios() {
        return usuarioRepo.findAll();
    }

    public usuario getUsuarioById(Long id) {
        return usuarioRepo.findById(id).orElse(null);
    }

    public usuario createUsuario(usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    public usuario updateUsuario(Long id, usuario updatedUsuario) {
        return usuarioRepo.findById(id).map(usuario -> {
            usuario.setUsername(updatedUsuario.getUsername());
            usuario.setPassword(updatedUsuario.getPassword());
            usuario.setDescription(updatedUsuario.getDescription());
            usuario.setEmail(updatedUsuario.getEmail());
            return usuarioRepo.save(usuario);
        }).orElse(null);
    }

    public void deleteUsuario(Long id) {
        usuarioRepo.deleteById(id);
    }
}