package be.vdab.cinefest.films;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Media;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql("/films.sql")
@AutoConfigureMockMvc
class FilmControllerTest {
    private static final String FILMS_TABLE = "films";
    private final MockMvc mockMvc;
    private final JdbcClient jdbcClient;
    private Path TEST_RESOURCES = Path.of("src/test/resources");

    FilmControllerTest(MockMvc mockMvc, JdbcClient jdbcClient) {
        this.mockMvc = mockMvc;
        this.jdbcClient = jdbcClient;
    }
    @Test
    void findTotaalVrijePlaatsenVindHetTotaalAantalVrijePlaatsen() throws Exception{
        var vrijePlaatsen = jdbcClient.sql("select sum(vrijePlaatsen) from films")
                .query(Long.class)
                .single();
        mockMvc.perform(get("/films/totaalvrijeplaatsen"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").value(vrijePlaatsen));
    }
    private long idVanTest1Film(){
        return jdbcClient.sql("select id from films where titel = 'test1'")
                .query(Long.class)
                .single();
    }
    @Test
    void findFilmByIdMetBestaandeIdWerkt() throws Exception{
        var id = idVanTest1Film();
        mockMvc.perform(get("/films/{id}", id))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(id),
                        jsonPath("titel").value("test1"));
    }
    @Test
    void findFilmByIdMetOnbestaandeThrowsNietGevondenException() throws Exception{
        var id = Long.MAX_VALUE;
        mockMvc.perform(get("/films/{id}", id))
                .andExpect(
                        status().isNotFound()
                );
    }
    @Test
    void findAlleFilmsVindtZelfdeAantal() throws Exception{
        mockMvc.perform(get("/films"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("length()")
                                .value(JdbcTestUtils.countRowsInTable(jdbcClient, FILMS_TABLE))
                );
    }
    @Test
    void findByJaarVindtHetzelfdeAantal() throws Exception{
        mockMvc.perform(get("/films")
                .param("jaar", "1998"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("length()").value(JdbcTestUtils.countRowsInTableWhere(jdbcClient, FILMS_TABLE, "jaar = 1998"))
                );
    }
    @Test
    void deleteFilmVerwijdertDeFilmById() throws Exception{
        var id = idVanTest1Film();
        mockMvc.perform(delete("/films/{id}", id))
                .andExpectAll(status().isOk());
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, FILMS_TABLE, "id = " + id)).isZero();


    }
    @Test
    void createFilmToevoegNieuweFilm() throws Exception{
        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteFilm.json"));
        var responseBody = mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, FILMS_TABLE, "titel= 'test3' and id=" + responseBody)).isOne();

    }
    @ParameterizedTest
    @ValueSource(strings={"filmLegeTitel.json", "filmVerkeerdJaar.json","filmZonderJaar.json","filmZonderTitel.json"})
    void createMetVerkeerdeDataGeefsBadRequest(String bestandsNaam) throws Exception{
        var jsonData = Files.readString(TEST_RESOURCES.resolve(bestandsNaam));
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isBadRequest());
    }
    @Test
    void patchWijzigtDeTitelVanDeFilm() throws Exception{
        var id= idVanTest1Film();
        mockMvc.perform(patch("/films/{id}/titel", id)
                .contentType(MediaType.TEXT_PLAIN)
                .content("gewijzigd"))
                .andExpect(status().isOk());
        assertThat(JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, FILMS_TABLE, "titel= 'gewijzigd' and id=" + id)).isOne();
    }
    @ParameterizedTest
    @ValueSource(strings={"", " "})
    void patchMetVerkeerdeTitelMislukt(String verkeerdeTitel) throws Exception{
        mockMvc.perform(patch("/films/{id}/titel", idVanTest1Film())
                .contentType(MediaType.TEXT_PLAIN)
                .content(verkeerdeTitel))
                .andExpect(status().isBadRequest());
    }
    @Test
    void patchVanOnbestandeFilmMislukt() throws Exception{
        mockMvc.perform(patch("/films/{id}/titel", Long.MAX_VALUE)
                .contentType(MediaType.TEXT_PLAIN)
                .content("gewijzigd")).andExpect(status().isNotFound());
    }

}
