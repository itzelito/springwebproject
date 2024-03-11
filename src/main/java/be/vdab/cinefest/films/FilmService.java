package be.vdab.cinefest.films;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
class FilmService {
    private final FilmRepository filmRepository;

    FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    long findTotaalVrijePlaatsen() {
        return filmRepository.findTotaalVrijePlaatsen();
    }
    Optional<Film> findFilmById(long id){
        return  filmRepository.findFilmById(id);
    }
    List<Film> findAll(){
        return filmRepository.findAll();
    }
    List<Film> findByJaar(int jaar){
        return filmRepository.findByJaar(jaar);
    }
    @Transactional
    void deleteFilm(long id){
        filmRepository.deleteFilm(id);
    }
    @Transactional
    long create(NieuweFilm nieuweFilm){
        var vrijePlaatsen = 0;
        var aankoopPrijs = BigDecimal.ZERO;
        var film =new Film(0, nieuweFilm.titel(), nieuweFilm.jaar(), vrijePlaatsen, aankoopPrijs);
        return filmRepository.create(film);
    }



}
