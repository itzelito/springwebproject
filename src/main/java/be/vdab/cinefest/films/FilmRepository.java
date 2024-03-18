package be.vdab.cinefest.films;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class FilmRepository {
    private final JdbcClient jdbcClient;
     FilmRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    long findTotaalVrijePlaatsen(){
        var sql= """
                select sum(vrijePlaatsen) as totaalVrijePlaatsen
                from films
                """;
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }
    Optional<Film> findFilmById(long id){
         var sql = """
                 select id, titel, jaar, vrijePlaatsen, aankoopprijs
                 from films
                 where id = ?
                 """;
         return jdbcClient.sql(sql)
                 .param(id)
                 .query(Film.class)
                 .optional();
    }
    List<Film> findAll(){
         var sql= """
                 select id, titel, jaar, vrijePlaatsen, aankoopprijs
                 from films
                 order by titel 
                 """;
         return jdbcClient.sql(sql)
                 .query(Film.class)
                 .list();
    }
    List<Film> findByJaar(int jaar){
         var sql= """
                 select id, titel, jaar, vrijePlaatsen, aankoopprijs
                 from films 
                 where jaar = ? 
                 order by titel
                 """;
         return jdbcClient.sql(sql)
                 .param(jaar)
                 .query(Film.class)
                 .list();


    }
    void deleteFilm(long id){
         var sql = """
                 delete from films
                 where id = ? 
                 """;
         jdbcClient.sql(sql)
                 .param(id)
                 .update(); 
    }
    long create(Film film){
         var sql= """
                 insert into films (titel, jaar, vrijePlaatsen, aankoopprijs)
                 values (?, ?, ?, ?) 
                 """;
         var keyholder = new GeneratedKeyHolder();
         jdbcClient.sql(sql)
                 .params(film.getTitel(), film.getJaar(), film.getVrijePlaatsen(), film.getAankoopprijs())
                 .update(keyholder);
         return keyholder.getKey().longValue();
    }
    void updateFilmTitel(Long id, String titel){
         var sql= """
                 update films
                 set titel = ?
                 where id = ?
                 """;
        if(jdbcClient.sql(sql).params(titel, id).update() == 0){
            throw new FilmNietGevondenException(id);
        }
    }
    Optional<Film> findAndLockById(long id){
         var sql= """
                 select id, titel, jaar, vrijePlaatsen, aankoopprijs
                 from films 
                 where id = ?
                 for update 
                 """;
         return jdbcClient.sql(sql)
                 .param(id)
                 .query(Film.class)
                 .optional();
    }
    void updateVrijePlaatsen (long id, int vrijePlaatsen){
         var sql= """
                 update films
                 set vrijePlaatsen = ? 
                 where id = ?
                 """;
         if(jdbcClient.sql(sql).params(vrijePlaatsen, id).update() == 0){
             throw new FilmNietGevondenException(id);
         }

    }
    




}
