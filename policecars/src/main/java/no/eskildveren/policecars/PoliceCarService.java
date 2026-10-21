package no.eskildveren.policecars;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import no.eskildveren.policecars.model.PoliceCar;
import no.eskildveren.policecars.model.PoliceCarUpdate;

@Service
public class PoliceCarService {

    @Autowired
    PoliceCarRepository repository;

    public PoliceCar getPoliceCarById(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<PoliceCar> getAvailablePoliceCars(String status) {
        return repository
                .findAll()
                .stream()
                .filter((pc) -> pc.getStatus().equals(status))
                .sorted((pc1, pc2) -> Integer.valueOf(pc1.getId()) - Integer.valueOf(pc2.getId()))
                .toList();
    }

    public void assignPoliceCarMission(String id, PoliceCarUpdate update) {
        PoliceCar car = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        car.setOppdrag(update.oppdrag());

        // Update to default status if no status is found
        if (update.status() == null)
            car.setStatus("I oppdrag");
        else
            car.setStatus(update.status());

        repository.save(car);
    }

    public void addPoliceCars(List<PoliceCar> policeCars) {
        if (policeCars != null)
            repository.saveAll(policeCars);
    }
}
