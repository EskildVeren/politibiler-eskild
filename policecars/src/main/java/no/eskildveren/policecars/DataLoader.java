package no.eskildveren.policecars;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import no.eskildveren.policecars.model.PoliceCarResponse;

/**
 * On Application startup, this component will check whether the database has
 * been seeded or not. If it has not been seeded, the component will fetch data
 * from the dummy API enpoint, and save the data to the database
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final PoliceCarRepository repository;
    private final RestClient restClient;

    public DataLoader(PoliceCarRepository repository, RestClient.Builder builder, @Value("${api.url}") String URL) {
        this.repository = repository;
        this.restClient = builder.baseUrl(URL).build();
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() != 0) {
            return;
        }

        PoliceCarResponse policeCars = restClient.get().uri("/politibiler.json").retrieve().body(
                PoliceCarResponse.class);

        if (policeCars != null) {
            policeCars.biler().stream().forEach((p) -> System.out.println(p));
            repository.saveAll(policeCars.biler());
        }
    }
}
