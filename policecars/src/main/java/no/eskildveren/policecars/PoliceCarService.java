package no.eskildveren.policecars;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PoliceCarService {

    @Autowired
    PoliceCarRepository policeCarRepository;

    public PoliceCar getPoliceCarById(String id) {
        return policeCarRepository.findById(id).orElse(null);
    }

    public List<PoliceCar> getAvailablePoliceCars(String status) {
        return policeCarRepository
                .findAll()
                .stream()
                .filter((pc) -> pc.status().equals(status))
                .sorted((pc1, pc2) -> pc2.id().compareTo(pc1.id()))
                .toList();
    }

    public void assignPoliceCarMission(String id) {

    }

    public void addPoliceCars(List<PoliceCar> policeCars) {
        policeCarRepository.saveAll(policeCars);
    }
}
