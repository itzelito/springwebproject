package be.vdab.cinefest.medewerkers;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql("/medewerkers.sql")
@AutoConfigureMockMvc
class MedewerkerControllerTest {
    private final static String MEDEWERKERS_TABLE = "medewerkers";
    private final MockMvc mockMvc;
    private final JdbcClient jdbcClient;

    public MedewerkerControllerTest(MockMvc mockMvc, JdbcClient jdbcClient) {
        this.mockMvc = mockMvc;
        this.jdbcClient = jdbcClient;
    }
    @Test
    void findMetStukVoornaamEnStukFamilienaamVindtHetJuiste() throws Exception{
        mockMvc.perform(get("/medewerkers").param("stukVoornaam", "1")
                .param("stukFamilienaam", "1"))
                .andExpectAll(
                        status().isOk(), jsonPath("length()")
                                .value(JdbcTestUtils.countRowsInTableWhere(
                                jdbcClient, MEDEWERKERS_TABLE, """
                                       voornaam like '%1%' and 
                                       familienaam like '%1%'
                                       """
                        ))
                );
    }

}

