package uni.edu.ni.Battle.ioAPI.modelos;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RegistroPartidaRequest {

    @Min(0)
    private Integer monedasGanadas;

    @Min(0)
    private Integer victorias;

    @Min(0)
    private Integer partidasJugadas;

    @Min(0)
    private Integer expGanada;
}

