package uni.edu.ni.Battle.ioAPI.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class UsuarioRequest {

    private String username;
    private String email;
    private String password;
    private String description;

    @JsonAlias({"nivel"})
    private Integer level;

    @JsonAlias({"monedas"})
    private Integer coins;

    @JsonAlias({"winned_matches", "winnedMatches", "partidasGanadas"})
    private Integer winnedMatches;

    @JsonAlias({"played_matches", "playedMatches", "partidasJugadas"})
    private Integer playedMatches;

    @JsonAlias({"story_progress", "storyProgress", "progresoHistoria"})
    private Integer storyProgress;

    @JsonAlias({"xp"})
    private Integer exp;
}