package be.vdab.cinefest.films;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("films/{id}")
    Film findFilmById(@PathVariable long id){
        return filmService.findFilmById(id)
                .orElseThrow(()->new FilmNietGevondenException(id));
    }
}
