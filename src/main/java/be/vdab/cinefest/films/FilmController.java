package be.vdab.cinefest.films;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
class FilmController {
    private final FilmService filmService;

    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    @GetMapping("films/totaalvrijeplaatsen")
    long findTotaalVrijePlaatsen(){
        return filmService.findTotaalVrijePlaatsen();
    }
    private record IdTitelJaarVrijePlaatsen (long id, String titel, int jaar, int vrijePlaatsen){
        IdTitelJaarVrijePlaatsen(Film film){
            this(film.getId(), film.getTitel(), film.getJaar(), film.getVrijePlaatsen());
        }
    }
    @GetMapping("films/{id}")
    IdTitelJaarVrijePlaatsen findFilmById(@PathVariable long id){
        return filmService.findFilmById(id)
                .map(film -> new IdTitelJaarVrijePlaatsen(film))
                .orElseThrow(()->new FilmNietGevondenException(id));
    }
    @GetMapping("films")
    Stream<IdTitelJaarVrijePlaatsen> findAll(){
        return filmService.findAll()
                .stream()
                .map(film -> new IdTitelJaarVrijePlaatsen(film));
    }
    @GetMapping(value ="films", params = "jaar")
    Stream<IdTitelJaarVrijePlaatsen> findByJaar(int jaar){
        return filmService.findByJaar(jaar)
                .stream()
                .map(film-> new IdTitelJaarVrijePlaatsen(film));
    }
    @DeleteMapping("films/{id}")
    void deleteFilm(@PathVariable long id){
        filmService.deleteFilm(id);
    }
    @PostMapping("films")
    long create(@RequestBody @Valid NieuweFilm nieuweFilm){
        return filmService.create(nieuweFilm);
    }
    @PatchMapping("films/{id}/titel")
    void updateTitel(@PathVariable long id, @RequestBody @NotBlank String titel){
        filmService.updateTitel(id, titel);
    }

}
