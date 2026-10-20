package no.eskildveren.policecars;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import no.eskildveren.policecars.model.PoliceCar;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

public class DataLoaderTest {

    String URL = "https://erikrolspo.github.io/politibiler.json";

    private static final String JSON = """
            {
                "biler": [
                    {
                        "id": "1",
                        "merke": "Ford",
                        "modell": "Focus",
                        "årsmodell": 2018,
                        "regNr": "DR 96669",
                        "status": "På vei til hendelse",
                        "oppdrag": "Trafikkulykke"
                    }
                ]
            }
            """;

    private PoliceCarRepository repository;
    private DataLoader loader;
    private MockRestServiceServer server;

    @BeforeEach
    void setup() {
        repository = mock(PoliceCarRepository.class);
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        loader = new DataLoader(repository, builder, "https://erikrolspo.github.io");
    }

    @Test
    void seedsDatabaseWhenEmpty() throws Exception {
        when(repository.count()).thenReturn(0L);
        server.expect(requestTo(URL)).andRespond(withSuccess(JSON, MediaType.APPLICATION_JSON));

        loader.run();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<PoliceCar>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());

        List<PoliceCar> savedCars = captor.getValue();
        assertThat(savedCars).hasSize(1);
        assertThat(savedCars.get(0).getId()).isEqualTo("1");
        assertThat(savedCars.get(0).getMerke()).isEqualTo("Ford");
        assertThat(savedCars.get(0).getModell()).isEqualTo("Focus");
        assertThat(savedCars.get(0).getRegNr()).isEqualTo("DR 96669");
        assertThat(savedCars.get(0).getAarsmodell()).isEqualTo(2018);
        assertThat(savedCars.get(0).getStatus()).isEqualTo("På vei til hendelse");
        assertThat(savedCars.get(0).getOppdrag()).isEqualTo("Trafikkulykke");
        server.verify();
    }

    @Test
    void skipsSeedingWhenDatabaseHasData() throws Exception {
        when(repository.count()).thenReturn(20L);

        loader.run();

        verify(repository, never()).saveAll(any());
        verify(repository, never()).save(any());
        server.verify();
    }
}
