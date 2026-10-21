package no.eskildveren.policecars;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import no.eskildveren.policecars.model.PoliceCar;
import no.eskildveren.policecars.model.PoliceCarUpdate;

@WebMvcTest(PoliceCarController.class)
public class PoliceCarControllerTest {

        @Autowired
        MockMvc mvc;

        @MockitoBean
        private PoliceCarService service;

        private static final String updateWithStatus = """
                        {
                            "oppdrag": "Trafikkulykke",
                            "status": "I oppdrag"
                        }
                        """;

        private static final String updateWithoutStatus = """
                        {
                            "oppdrag": "Trafikkulykke"
                        }
                        """;

        private static final String updateWithStatusNull = """
                        {
                            "oppdrag": "Trafikkulykke",
                            "status": null
                        }
                        """;

        private static final String updateWithMissionNull = """
                        {
                            "oppdrag": null,
                            "status": null
                        }
                        """;

        private static final String updateWitoutMission = """
                                {
                                    "status": "I oppdrag"
                                }
                        """;

        private static final String emptyUpdate = "";

        @Test
        void returnsCarWithStatusTilgjengelig() throws Exception {
                PoliceCar car = new PoliceCar("1", "Ford", "Focus", 2018, "DR 1234", "Tilgjengelig", "Patruljerer");
                when(service.getPoliceCarsMatchingStatus("Tilgjengelig")).thenReturn(List.of(car));

                mvc.perform(get("/politibiler/").param("status", "Tilgjengelig"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1))
                                .andExpect(jsonPath("$[0].id").value("1"))
                                .andExpect(jsonPath("$[0].merke").value("Ford"))
                                .andExpect(jsonPath("$[0].modell").value("Focus"))
                                .andExpect(jsonPath("$[0].årsmodell").value(2018))
                                .andExpect(jsonPath("$[0].regNr").value("DR 1234"))
                                .andExpect(jsonPath("$[0].status").value("Tilgjengelig"))
                                .andExpect(jsonPath("$[0].oppdrag").value("Patruljerer"));

                verify(service).getPoliceCarsMatchingStatus("Tilgjengelig");
        }

        @Test
        void returnsOkWhenNoCarHasStatusTilgjengelig() throws Exception {
                when(service.getPoliceCarsMatchingStatus("Tilgjengelig")).thenReturn(List.of());

                mvc.perform(get("/politibiler/").param("status", "Tilgjengelig"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(0));

                verify(service).getPoliceCarsMatchingStatus("Tilgjengelig");
        }

        @Test
        void returns403ForUnauthorizedStatus() throws Exception {
                mvc.perform(get("/politibiler/").param("status", "i Oppdrag"))
                                .andExpect(status().isForbidden())
                                .andExpect(content().string(""));

                verifyNoInteractions(service);
        }

        @Test
        void returnsValidPoliceCar() throws Exception {
                PoliceCar car = new PoliceCar("1", "Ford", "Focus", 2018, "DR 1234", "Tilgjengelig", "Patruljerer");
                when(service.getPoliceCarById("1")).thenReturn(car);

                mvc.perform(get("/politibiler/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value("1"))
                                .andExpect(jsonPath("$.merke").value("Ford"))
                                .andExpect(jsonPath("$.modell").value("Focus"))
                                .andExpect(jsonPath("$.årsmodell").value(2018))
                                .andExpect(jsonPath("$.regNr").value("DR 1234"))
                                .andExpect(jsonPath("$.status").value("Tilgjengelig"))
                                .andExpect(jsonPath("$.oppdrag").value("Patruljerer"));

                verify(service).getPoliceCarById("1");
        }

        @Test
        void returns404WhenNoCarMatchesId() throws Exception {
                when(service.getPoliceCarById("1")).thenReturn(null);

                mvc.perform(get("/politibiler/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string(""));

                verify(service).getPoliceCarById("1");
        }

        @Test
        void updatesExistingPoliceCar() throws Exception {
                PoliceCarUpdate update = new PoliceCarUpdate("Trafikkulykke", "I oppdrag");

                // Test a "normal" request
                mvc.perform(patch("/politibiler/1")
                                .content(updateWithStatus)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
                verify(service).assignPoliceCarMission("1", update);

                // Test a request with status missing
                mvc.perform(patch("/politibiler/1")
                                .content(updateWithoutStatus)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
                verify(service).assignPoliceCarMission("1", update);

                // Test a request with status null
                mvc.perform(patch("/politibiler/1")
                                .content(updateWithStatusNull)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                verify(service).assignPoliceCarMission("1", update);
        }

        @Test
        void throwErrorWhenInvalidMission() throws Exception {

                // Test request without mission field
                mvc.perform(patch("/politibiler/1").content(
                                updateWitoutMission).contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                // Test request mission null
                mvc.perform(patch("/politibiler/1").content(
                                updateWithMissionNull).contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                // Test request with empty object
                mvc.perform(patch("/politibiler/1").content(
                                emptyUpdate).contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verify(service, never()).assignPoliceCarMission(any(), any());
        }
}
