package uni.edu.ni.Battle.ioAPI.controlador;

import uni.edu.ni.Battle.ioAPI.modelos.usuario;
import uni.edu.ni.Battle.ioAPI.servicios.usuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/usuarios")
public class usuarioController {
    private final usuarioService usuarioService;

    public usuarioController(usuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public usuario getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    @PostMapping
    public usuario createUsuario(@RequestBody usuario usuario) {
        return usuarioService.createUsuario(usuario);
    }

    @PutMapping("/{id}")
    public usuario updateUsuario(@PathVariable Long id, @RequestBody usuario updatedUsuario) {
        return usuarioService.updateUsuario(id, updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
    }


}
