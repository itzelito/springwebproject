package be.vdab.cinefest.films;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("films")
class FilmController {
    private final FilmService filmService;

    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    @GetMapping("totaalvrijeplaatsen")
    long findTotaalVrijePlaatsen(){
        return filmService.findTotaalVrijePlaatsen();
    }
    private record IdTitelJaarVrijePlaatsen (long id, String titel, int jaar, int vrijePlaatsen){
        IdTitelJaarVrijePlaatsen(Film film){
            this(film.getId(), film.getTitel(), film.getJaar(), film.getVrijePlaatsen());
        }
    }
    @GetMapping("{id}")
    IdTitelJaarVrijePlaatsen findFilmById(@PathVariable long id){
        return filmService.findFilmById(id)
                .map(film -> new IdTitelJaarVrijePlaatsen(film))
                .orElseThrow(()->new FilmNietGevondenException(id));
    }
    @GetMapping
    Stream<IdTitelJaarVrijePlaatsen> findAll(){
        return filmService.findAll()
                .stream()
                .map(film -> new IdTitelJaarVrijePlaatsen(film));
    }
    @GetMapping(params = "jaar")
    Stream<IdTitelJaarVrijePlaatsen> findByJaar(int jaar){
        return filmService.findByJaar(jaar)
                .stream()
                .map(film-> new IdTitelJaarVrijePlaatsen(film));
    }
    @DeleteMapping("{id}")
    void deleteFilm(@PathVariable long id){
        filmService.deleteFilm(id);
    }
    @PostMapping
    long create(@RequestBody @Valid NieuweFilm nieuweFilm){
        return filmService.create(nieuweFilm);
    }
    @PatchMapping("{id}/titel")
    void updateTitel(@PathVariable long id, @RequestBody @NotBlank String titel){
        filmService.updateTitel(id, titel);
    }
    @PostMapping("{id}/reservaties")
    long reserveer(@PathVariable long id, @RequestBody @Valid NieuweReservatie nieuweReservatie){
        return filmService.reserveer(id, nieuweReservatie);
    }

}
