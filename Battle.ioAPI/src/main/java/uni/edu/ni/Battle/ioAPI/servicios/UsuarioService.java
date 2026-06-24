package uni.edu.ni.Battle.ioAPI.servicios;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uni.edu.ni.Battle.ioAPI.modelos.ActualizarParRequest;
import uni.edu.ni.Battle.ioAPI.modelos.ActualizarStatsRequest;
import uni.edu.ni.Battle.ioAPI.modelos.ActualizarUsuarioRequest;
import uni.edu.ni.Battle.ioAPI.modelos.UsuarioUI;
import uni.edu.ni.Battle.ioAPI.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioUI registrar(UsuarioUI usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // 🔹 hash
        return repository.save(usuario);
    }

    public UsuarioUI actualizarPar(Long id, ActualizarParRequest request) {
        UsuarioUI existente = obtenerUsuarioExistente(id);

        existente.setUsername(request.getUsername());
        existente.setEmail(request.getEmail());
        existente.setDescription(request.getDescription());

        return repository.save(existente);
    }

    public UsuarioUI actualizarUsuario(Long id, ActualizarUsuarioRequest request) {
        UsuarioUI existente = obtenerUsuarioExistente(id);

        existente.setUsername(request.getUsername());
        existente.setEmail(request.getEmail());
        existente.setPassword(passwordEncoder.encode(request.getPassword()));
        existente.setDescription(request.getDescription());

        return repository.save(existente);
    }

    public UsuarioUI actualizarStats(Long id, ActualizarStatsRequest request) {
        UsuarioUI existente = obtenerUsuarioExistente(id);

        existente.setLevel(request.getLevel());
        existente.setCoins(request.getCoins());
        existente.setWinned_matches(request.getWinned_matches());
        existente.setPlayed_matches(request.getPlayed_matches());
        existente.setStoryProgress(request.getStoryProgress());

        return repository.save(existente);
    }

    private UsuarioUI obtenerUsuarioExistente(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public UsuarioUI obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<UsuarioUI> listar() {
        return repository.findAll();
    }

    public UsuarioUI login(String email, String password) {
        UsuarioUI usuario = repository.findByEmail(email);
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario;
        }
        return null;
    }
}
