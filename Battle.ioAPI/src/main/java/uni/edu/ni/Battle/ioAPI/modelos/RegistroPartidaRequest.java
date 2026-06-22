package uni.edu.ni.Battle.ioAPI.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RegistroPartidaRequest {

    @Min(0)
    @JsonAlias({"coins_earned", "monedasGanadas", "monedas_ganadas"})
    private Integer monedasGanadas;

    @Min(0)
    @JsonAlias({"winned_matches", "wins", "victories", "victorias", "partidasGanadas"})
    private Integer victorias;

    @Min(0)
    @JsonAlias({"played_matches", "matches_played", "partidasJugadas", "partidas_jugadas"})
    private Integer partidasJugadas;

    @Min(0)
    @JsonAlias({"exp_earned", "expGanada", "exp_ganada", "xpGanada"})
    private Integer expGanada;
}