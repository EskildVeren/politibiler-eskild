package no.eskildveren.policecars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliceCarRepository extends JpaRepository<PoliceCar, String> {

}
