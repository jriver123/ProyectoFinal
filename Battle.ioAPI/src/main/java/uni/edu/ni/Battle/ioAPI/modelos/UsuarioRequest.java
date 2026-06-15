package uni.edu.ni.Battle.ioAPI.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class UsuarioRequest {

    private String username;
    private String email;
    private String password;
    private String description;
    private Integer level;
    private Integer coins;

    @JsonAlias({"winned_matches", "winnedMatches"})
    private Integer winnedMatches;

    @JsonAlias({"played_matches", "playedMatches"})
    private Integer playedMatches;

    @JsonAlias({"story_progress", "storyProgress"})
    private Integer storyProgress;

    private Integer exp;
}

