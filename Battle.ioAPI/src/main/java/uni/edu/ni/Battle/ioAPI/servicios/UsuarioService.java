package uni.edu.ni.Battle.ioAPI.servicios;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uni.edu.ni.Battle.ioAPI.modelos.RegistroPartidaRequest;
import uni.edu.ni.Battle.ioAPI.modelos.UsuarioRequest;
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

    public UsuarioUI registrar(UsuarioRequest request) {
        UsuarioUI usuario = new UsuarioUI();
        aplicarCamposUsuario(usuario, request, false);
        inicializarEstadisticas(usuario);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // 🔹 hash
        return repository.save(usuario);
    }

    public UsuarioUI actualizar(Long id, UsuarioRequest request) {
        UsuarioUI existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        aplicarCamposUsuario(existente, request, true);

        if (request.getPassword() != null) {
            existente.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return repository.save(existente);
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

    public UsuarioUI registrarEstadisticasPartida(Long id, RegistroPartidaRequest request) {
        UsuarioUI existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int monedasGanadas = request.getMonedasGanadas() == null ? 0 : request.getMonedasGanadas();
        int victorias = request.getVictorias() == null ? 0 : request.getVictorias();
        int partidasJugadas = request.getPartidasJugadas() == null ? 1 : request.getPartidasJugadas();
        int expGanada = request.getExpGanada() == null ? 0 : request.getExpGanada();

        if (monedasGanadas < 0 || victorias < 0 || partidasJugadas < 0 || expGanada < 0) {
            throw new IllegalArgumentException("Las estadísticas no pueden ser negativas");
        }

        existente.setCoins(valorOActual(existente.getCoins(), 0) + monedasGanadas);
        existente.setWinned_matches(valorOActual(existente.getWinned_matches(), 0) + victorias);
        existente.setPlayed_matches(valorOActual(existente.getPlayed_matches(), 0) + partidasJugadas);
        existente.setExp(valorOActual(existente.getExp(), 0) + expGanada);

        return repository.save(existente);
    }

    private void inicializarEstadisticas(UsuarioUI usuario) {
        usuario.setCoins(valorOActual(usuario.getCoins(), 0));
        usuario.setWinned_matches(valorOActual(usuario.getWinned_matches(), 0));
        usuario.setPlayed_matches(valorOActual(usuario.getPlayed_matches(), 0));
        usuario.setExp(valorOActual(usuario.getExp(), 0));
    }

    private void aplicarCamposUsuario(UsuarioUI destino, UsuarioRequest request, boolean conservarSiNulo) {
        destino.setUsername(valorOActual(request.getUsername(), destino.getUsername(), conservarSiNulo));
        destino.setEmail(valorOActual(request.getEmail(), destino.getEmail(), conservarSiNulo));
        destino.setDescription(valorOActual(request.getDescription(), destino.getDescription(), conservarSiNulo));
        destino.setLevel(valorOActual(request.getLevel(), destino.getLevel(), conservarSiNulo));
        destino.setCoins(valorOActual(request.getCoins(), destino.getCoins(), conservarSiNulo));
        destino.setWinned_matches(valorOActual(request.getWinnedMatches(), destino.getWinned_matches(), conservarSiNulo));
        destino.setPlayed_matches(valorOActual(request.getPlayedMatches(), destino.getPlayed_matches(), conservarSiNulo));
        destino.setExp(valorOActual(request.getExp(), destino.getExp(), conservarSiNulo));
        destino.setStoryProgress(valorOActual(request.getStoryProgress(), destino.getStoryProgress(), conservarSiNulo));

        if (!conservarSiNulo) {
            destino.setPassword(request.getPassword());
        }
    }

    private <T> T valorOActual(T nuevoValor, T valorActual, boolean conservarSiNulo) {
        if (!conservarSiNulo) {
            return nuevoValor;
        }
        return nuevoValor != null ? nuevoValor : valorActual;
    }

    private Integer valorOActual(Integer nuevoValor, Integer valorActual) {
        return valorOActual(nuevoValor, valorActual, true);
    }
}