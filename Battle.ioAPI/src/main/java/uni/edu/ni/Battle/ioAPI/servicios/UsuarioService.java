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

    private static final int EXP_POR_NIVEL = 100;
    private static final int NIVEL_MAXIMO = 100;

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioUI registrar(UsuarioRequest request) {
        validarRegistro(request);

        UsuarioUI usuario = new UsuarioUI();

        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        usuario.setDescription(valorTexto(request.getDescription(), "Jugador de Battle.io"));
        usuario.setLevel(valorEnteroMinimo(request.getLevel(), 1));
        usuario.setCoins(valorEnteroMinimo(request.getCoins(), 0));
        usuario.setWinned_matches(valorEnteroMinimo(request.getWinnedMatches(), 0));
        usuario.setPlayed_matches(valorEnteroMinimo(request.getPlayedMatches(), 0));
        usuario.setExp(valorEnteroMinimo(request.getExp(), 0));
        usuario.setStoryProgress(valorEnteroMinimo(request.getStoryProgress(), 1));

        return repository.save(usuario);
    }

    public UsuarioUI actualizar(Long id, UsuarioRequest request) {
        UsuarioUI existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            existente.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            existente.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getDescription() != null) {
            existente.setDescription(valorTexto(request.getDescription(), "Jugador de Battle.io"));
        }

        if (request.getLevel() != null) {
            existente.setLevel(limitarNivel(request.getLevel()));
        }

        if (request.getCoins() != null) {
            existente.setCoins(Math.max(0, request.getCoins()));
        }

        if (request.getWinnedMatches() != null) {
            existente.setWinned_matches(Math.max(0, request.getWinnedMatches()));
        }

        if (request.getPlayedMatches() != null) {
            existente.setPlayed_matches(Math.max(0, request.getPlayedMatches()));
        }

        if (request.getExp() != null) {
            existente.setExp(Math.max(0, request.getExp()));
        }

        if (request.getStoryProgress() != null) {
            existente.setStoryProgress(Math.max(1, request.getStoryProgress()));
        }

        return repository.save(existente);
    }

    public UsuarioUI actualizarProgreso(Long id, UsuarioRequest request) {
        UsuarioUI existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getLevel() != null) {
            existente.setLevel(limitarNivel(request.getLevel()));
        }

        if (request.getCoins() != null) {
            existente.setCoins(Math.max(0, request.getCoins()));
        }

        if (request.getWinnedMatches() != null) {
            existente.setWinned_matches(Math.max(0, request.getWinnedMatches()));
        }

        if (request.getPlayedMatches() != null) {
            existente.setPlayed_matches(Math.max(0, request.getPlayedMatches()));
        }

        if (request.getExp() != null) {
            existente.setExp(Math.max(0, request.getExp()));
        }

        if (request.getStoryProgress() != null) {
            existente.setStoryProgress(Math.max(1, request.getStoryProgress()));
        }

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        repository.deleteById(id);
    }

    public UsuarioUI obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<UsuarioUI> listar() {
        return repository.findAll();
    }

    public UsuarioUI login(String email, String password) {
        if (email == null || password == null) {
            return null;
        }

        return repository.findByEmail(email)
                .filter(usuario -> passwordEncoder.matches(password, usuario.getPassword()))
                .orElse(null);
    }

    public UsuarioUI registrarEstadisticasPartida(Long id, RegistroPartidaRequest request) {
        UsuarioUI existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int monedasGanadas = valorEnteroMinimo(request.getMonedasGanadas(), 0);
        int victorias = valorEnteroMinimo(request.getVictorias(), 0);
        int partidasJugadas = valorEnteroMinimo(request.getPartidasJugadas(), 1);
        int expGanada = valorEnteroMinimo(request.getExpGanada(), 0);

        existente.setCoins(valorEnteroMinimo(existente.getCoins(), 0) + monedasGanadas);
        existente.setWinned_matches(valorEnteroMinimo(existente.getWinned_matches(), 0) + victorias);
        existente.setPlayed_matches(valorEnteroMinimo(existente.getPlayed_matches(), 0) + partidasJugadas);

        sumarExperienciaYSubirNivel(existente, expGanada);

        return repository.save(existente);
    }

    private void sumarExperienciaYSubirNivel(UsuarioUI usuario, int expGanada) {
        int nivelActual = limitarNivel(valorEnteroMinimo(usuario.getLevel(), 1));
        int expActual = valorEnteroMinimo(usuario.getExp(), 0) + Math.max(0, expGanada);

        while (expActual >= EXP_POR_NIVEL && nivelActual < NIVEL_MAXIMO) {
            expActual -= EXP_POR_NIVEL;
            nivelActual++;
        }

        usuario.setLevel(nivelActual);
        usuario.setExp(expActual);
    }

    private void validarRegistro(UsuarioRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Los datos del usuario son obligatorios");
        }

        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (repository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
    }

    private String valorTexto(String valor, String valorDefecto) {
        if (valor == null || valor.isBlank()) {
            return valorDefecto;
        }

        return valor;
    }

    private Integer valorEnteroMinimo(Integer valor, int minimo) {
        if (valor == null || valor < minimo) {
            return minimo;
        }

        return valor;
    }

    private Integer limitarNivel(Integer nivel) {
        int valor = valorEnteroMinimo(nivel, 1);

        if (valor > NIVEL_MAXIMO) {
            return NIVEL_MAXIMO;
        }

        return valor;
    }
}