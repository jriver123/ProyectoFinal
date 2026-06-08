package uni.edu.ni.Battle.ioAPI.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Column(name ="username", nullable=false, unique=true,length = 25)
    private String username;

    @Column(name = "email",nullable=false, unique=true,length = 40)
    private String email;

    @Column(name = "password",nullable=false,length = 70)
    private String password;
    @Column(name = "description",nullable = false,length = 200)
    private String description;
    @Min(1)
    @Max(100)
    @Column(name = "level", nullable = false,length = 3)
    private Integer level;
    @Min(0)
    @Column(name = "coins", nullable = false)
    private Integer coins;
    @Min(0)
    @Column(name = "winned_matches", nullable = false)
    private Integer winned_matches;
    @Min(0)
    @Column(name = "played_matches", nullable = false)
    private Integer played_matches;
    @Min(1)
    @Column(name = "story_progress", nullable = false)
    private Integer storyProgress;

}

