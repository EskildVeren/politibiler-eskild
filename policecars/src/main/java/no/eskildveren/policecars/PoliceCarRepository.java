package no.eskildveren.policecars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.eskildveren.policecars.model.PoliceCar;

@Repository
public interface PoliceCarRepository extends JpaRepository<PoliceCar, String> {

}
