package be.vdab.cinefest.films;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class FilmTest {
    @Test
    void reserveerVermindertDeVrijePlaatsen(){
        var film = new Film(0,"test", 2024, 2, BigDecimal.ONE);
        film.reserveer(2);
        assertThat(film.getVrijePlaatsen()).isEqualTo(0);

    }
    @Test
    void reserveerZonderVrijePlaatsenThrowsException() {
        var film = new Film(0, "test", 2024, 0, BigDecimal.ONE);

        assertThatExceptionOfType(OnvoldoendeVrijePlaatsenException.class).isThrownBy(()->film.reserveer(1));
    }
}
