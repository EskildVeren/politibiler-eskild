package no.eskildveren.policecars;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import no.eskildveren.policecars.model.PoliceCar;
import no.eskildveren.policecars.model.PoliceCarUpdate;

@ExtendWith(MockitoExtension.class)
public class PoliceCarServiceTest {

    @Mock
    PoliceCarRepository repository;

    @InjectMocks
    PoliceCarService service;

    PoliceCar car1 = new PoliceCar("9", "Toyota", "Corolla", 2002, "QR 1234", "I oppdrag", "Fanger skurker");
    PoliceCar car2 = new PoliceCar("4", "Toyota", "Corolla", 2004, "QR 1234", "Tilgjengelig", "Patruljerer");
    PoliceCar car3 = new PoliceCar("111", "Toyota", "Corolla", 2001, "QR 1234", "Tilgjengelig", "Patruljerer");
    PoliceCar car4 = new PoliceCar("33", "Toyota", "Corolla", 2003, "QR 1234", "I oppdrag", "Biljakt");

    PoliceCar availableCar1 = new PoliceCar("9", "Toyota", "Corolla", 2002, "QR 1234", "Tilgjengelig",
            "Fanger skurker");
    PoliceCar availableCar4 = new PoliceCar("33", "Toyota", "Corolla", 2003, "QR 1234", "Tilgjengelig", "Biljakt");

    List<PoliceCar> cars = List.of(car1, car2, car3, car4);
    List<PoliceCar> allAvailableCars = List.of(availableCar1, car2, car3, availableCar4);

    // ----------------- getPoliceCarById(String id) -----------------

    @Test
    void retrievesCarCorrectly() {
        when(repository.findById("1")).thenReturn(Optional.of(car1));
        PoliceCar retrieved = service.getPoliceCarById("1");
        assertThat(car1.equals(retrieved));
        verify(repository).findById("1");
    }

    @Test
    void correctlyHandlesNoCarMatchesId() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        PoliceCar retrieved = service.getPoliceCarById("1");
        assertThat(retrieved == null);
        verify(repository).findById("1");
    }

    // ----------------- getAvailablePoliceCars(String status) -----------------

    @Test
    void returnsSortedArrayOfCarsMatchingStatus() {
        when(repository.findAll()).thenReturn(allAvailableCars);
        List<PoliceCar> retirevedCars = service.getAvailablePoliceCars("Tilgjengelig");
        assertThat(retirevedCars).hasSize(4);
        assertThat(retirevedCars.get(0)).isEqualTo(car2);
        assertThat(retirevedCars.get(1)).isEqualTo(availableCar1);
        assertThat(retirevedCars.get(2)).isEqualTo(availableCar4);
        assertThat(retirevedCars.get(3)).isEqualTo(car3);
        verify(repository).findAll();
    }

    @Test
    void filtersOutCarsNotMatchngStatus() {
        when(repository.findAll()).thenReturn(cars);
        List<PoliceCar> retirevedCars = service.getAvailablePoliceCars("Tilgjengelig");
        assertThat(retirevedCars).hasSize(2);
        assertThat(retirevedCars.get(0)).isEqualTo(car2);
        assertThat(retirevedCars.get(1)).isEqualTo(car3);
        verify(repository).findAll();
    }

    @Test
    void returnsEmptyListWhenNoCarMatchesStatus() {
        when(repository.findAll()).thenReturn(List.of());
        List<PoliceCar> retirevedCars = service.getAvailablePoliceCars("No Matching Status");
        assertThat(retirevedCars).isNotNull();
        assertThat(retirevedCars).hasSize(0);
        verify(repository).findAll();
    }

    // -------- assignPoliceCarMission(String id, PoliceCarUpdate update) --------

    PoliceCarUpdate missionAndStatus = new PoliceCarUpdate("Fange skurker", "På biljakt");
    PoliceCarUpdate missionWithoutStatus = new PoliceCarUpdate("Fange skurker", null);
    PoliceCar car = new PoliceCar("1", "Toyota", "Corolla", 2002, "RG 1234", "Tilgjengelig", "Patruljerer");
    PoliceCar missionAlteredCar = new PoliceCar("1", "Toyota", "Corolla", 2002, "RG 1234", "I oppdrag",
            "Fange skurker");
    PoliceCar missionAndStatusAlteredCar = new PoliceCar("1", "Toyota", "Corolla", 2002, "RG 1234", "På biljakt",
            "Fange skurker");

    @Test
    void udpatesMissionAndStatus() throws Exception {
        when(repository.findById("1")).thenReturn(Optional.of(car));

        service.assignPoliceCarMission("1", missionAndStatus);

        verify(repository).findById("1");
        verify(repository).save(car);
        assertThat(car.getOppdrag()).isEqualTo("Fange skurker");
        assertThat(car.getStatus()).isEqualTo("På biljakt");
    }

    @Test
    void updatesToDefaultStatusWhenAssigningMissionWithoutStatus() throws Exception {
        when(repository.findById("1")).thenReturn(Optional.of(car));

        service.assignPoliceCarMission("1", missionWithoutStatus);

        verify(repository).findById("1");
        verify(repository).save(car);
        assertThat(car.getOppdrag()).isEqualTo("Fange skurker");
        assertThat(car.getStatus()).isEqualTo("I oppdrag");
    }

    @Test
    void returns404WhenIdNotFound() throws Exception {
        when(repository.findById("13")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.assignPoliceCarMission("13", new PoliceCarUpdate("Fange skurker", null)))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));

        verify(repository, never()).save(any());
    }

    // addPoliceCars(List<PoliceCar> policeCars)

    @Test
    void savesAllCars() {
        service.addPoliceCars(cars);
        verify(repository).saveAll(cars);
    }

    @Test
    void doesNotSaveIfCarsIsNull() {
        service.addPoliceCars(null);
        verify(repository, never()).saveAll(any());
    }
}
