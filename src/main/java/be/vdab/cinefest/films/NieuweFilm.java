package be.vdab.cinefest.films;

import jakarta.validation.constraints.*;

record NieuweFilm(@NotBlank String titel, @NotNull @Positive int jaar) {

}
