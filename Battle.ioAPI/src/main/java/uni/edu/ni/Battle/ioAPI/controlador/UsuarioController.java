package uni.edu.ni.Battle.ioAPI.controlador;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.edu.ni.Battle.ioAPI.modelos.LoginRequest;
import uni.edu.ni.Battle.ioAPI.modelos.LoginResponse;
import uni.edu.ni.Battle.ioAPI.modelos.RegistroPartidaRequest;
import uni.edu.ni.Battle.ioAPI.modelos.UsuarioRequest;
import uni.edu.ni.Battle.ioAPI.modelos.UsuarioUI;
import uni.edu.ni.Battle.ioAPI.servicios.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UsuarioUI usuario = service.login(request.getEmail(), request.getPassword());

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }

        String token = "fake-jwt-token-" + usuario.getId();

        LoginResponse response = new LoginResponse(
                usuario.getId(),
                token,
                usuario.getUsername(),
                usuario.getEmail()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody UsuarioRequest usuario) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.registrar(usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarDesdeRutaRegistro(@RequestBody UsuarioRequest usuario) {
        return registrar(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioRequest usuario
    ) {
        try {
            return ResponseEntity.ok(service.actualizar(id, usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/progreso")
    public ResponseEntity<?> actualizarProgreso(
            @PathVariable Long id,
            @RequestBody UsuarioRequest usuario
    ) {
        try {
            return ResponseEntity.ok(service.actualizarProgreso(id, usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estadisticas")
    public ResponseEntity<?> registrarEstadisticasPartida(
            @PathVariable Long id,
            @Valid @RequestBody RegistroPartidaRequest request
    ) {
        try {
            return ResponseEntity.ok(service.registrarEstadisticasPartida(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<?> obtenerDetalles(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public List<UsuarioUI> listar() {
        return service.listar();
    }
}