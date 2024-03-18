package be.vdab.cinefest.films;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class ReservatieRepository {
    private final JdbcClient jdbcClient;

    public ReservatieRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    long createReservatie (Reservatie reservatie){
        var sql = """
                insert into reservaties (filmId, emailAdres, plaatsen, besteld)
                values (?, ?, ?, ?)
                """;
        var keyholder= new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(reservatie.getFilmId(), reservatie.getEmailAdres(), reservatie.getPlaatsen(), reservatie.getBesteld())
                .update(keyholder);
        return(keyholder.getKey().longValue());
    }
    List<ReservatieMetFilm> findByEmailAdres(String emailAdres) {
        var sql = """
                select reservaties.id, titel, emailAdres, plaatsen, besteld
                from reservaties inner join films on reservaties.filmId = films.id
                where emailAdres = ?
                order by id desc
                """;
        return jdbcClient.sql(sql)
                .param(emailAdres)
                .query(ReservatieMetFilm.class)
                .list();
    }

}
