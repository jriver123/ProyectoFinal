package uni.edu.ni.Battle.ioAPI.controlador;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.edu.ni.Battle.ioAPI.modelos.ActualizarParRequest;
import uni.edu.ni.Battle.ioAPI.modelos.ActualizarStatsRequest;
import uni.edu.ni.Battle.ioAPI.modelos.ActualizarUsuarioRequest;
import uni.edu.ni.Battle.ioAPI.modelos.LoginRequest;
import uni.edu.ni.Battle.ioAPI.modelos.LoginResponse;
import uni.edu.ni.Battle.ioAPI.modelos.UsuarioUI;
import uni.edu.ni.Battle.ioAPI.servicios.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UsuarioUI usuario = service.login(request.getEmail(), request.getPassword());
        if (usuario != null) {
            // Generar token (ejemplo simple, puedes usar JWT)
            String token = "fake-jwt-token-" + usuario.getId();

            LoginResponse response = new LoginResponse(
                    usuario.getId(),
                    token,
                    usuario.getUsername(),
                    usuario.getEmail()
            );

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
            }
        }






    @PostMapping
    public UsuarioUI registrar(@RequestBody UsuarioUI usuario) {
        return service.registrar(usuario);
    }

    @PutMapping("/{id}/actualizar-par")
    public UsuarioUI actualizarPar(@PathVariable Long id, @RequestBody ActualizarParRequest request) {
        return service.actualizarPar(id, request);
    }

    @PutMapping("/{id}/actualizar-usuario")
    public UsuarioUI actualizarUsuario(@PathVariable Long id, @RequestBody ActualizarUsuarioRequest request) {
        return service.actualizarUsuario(id, request);
    }

    @PutMapping("/{id}/actualizar-stats")
    public UsuarioUI actualizarStats(@PathVariable Long id, @RequestBody ActualizarStatsRequest request) {
        return service.actualizarStats(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/{id}")
    public UsuarioUI obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping
    public List<UsuarioUI> listar() {
        return service.listar();
    }
}
