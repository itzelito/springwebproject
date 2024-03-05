package be.vdab.cinefest.films;

import org.springframework.jdbc.core.simple.JdbcClient;
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




}
