package uni.edu.ni.Battle.ioAPI.modelos;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password",nullable = false, length = 12)
    private String password;

    @Column(name= "description",nullable = false, length = 200)
    private String description;

    @Column(name = "email",nullable = false, unique = true, length = 40)
    private String email;
}
