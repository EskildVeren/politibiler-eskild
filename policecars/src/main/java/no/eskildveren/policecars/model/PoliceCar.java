package no.eskildveren.policecars.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record PoliceCar(
                @Id String id,
                String merke,
                String modell,
                String årsmodell,
                String regNr,
                String status,
                String oppdrag) {
}
