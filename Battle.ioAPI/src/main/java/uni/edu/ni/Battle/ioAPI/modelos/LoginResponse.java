package uni.edu.ni.Battle.ioAPI.modelos;

public class LoginResponse {
    private Long id;
    private String token;
    private String nombre;
    private String correo;

    public LoginResponse(Long id, String token, String nombre, String correo) {
        this.id = id;
        this.token = token;
        this.nombre = nombre;
        this.correo = correo;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
