package uni.edu.ni.Battle.ioAPI.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioUI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 25)
    @Column(name = "username", nullable = false, unique = true, length = 25)
    private String username;

    @Size(max = 40)
    @Column(name = "email", nullable = false, unique = true, length = 40)
    private String email;

    @Size(max = 100)
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Size(max = 200)
    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Min(1)
    @Max(100)
    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Min(0)
    @Column(name = "coins", nullable = false)
    private Integer coins = 0;

    @Min(0)
    @Column(name = "winned_matches", nullable = false)
    private Integer winned_matches = 0;

    @Min(0)
    @Column(name = "played_matches", nullable = false)
    private Integer played_matches = 0;

    @Min(0)
    @Column(name = "exp", nullable = false)
    private Integer exp = 0;

    @Min(1)
    @Column(name = "story_progress", nullable = false)
    private Integer storyProgress = 1;

    @PrePersist
    @PreUpdate
    private void aplicarValoresPorDefecto() {
        if (description == null || description.isBlank()) {
            description = "Jugador de Battle.io";
        }

        if (level == null || level < 1) {
            level = 1;
        }

        if (coins == null || coins < 0) {
            coins = 0;
        }

        if (winned_matches == null || winned_matches < 0) {
            winned_matches = 0;
        }

        if (played_matches == null || played_matches < 0) {
            played_matches = 0;
        }

        if (exp == null || exp < 0) {
            exp = 0;
        }

        if (storyProgress == null || storyProgress < 1) {
            storyProgress = 1;
        }
    }
}