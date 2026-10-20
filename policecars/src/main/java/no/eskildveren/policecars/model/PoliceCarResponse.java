package no.eskildveren.policecars.model;

import java.util.List;

/**
 * This class matches the retrieved object from the dummy API used for the
 * system
 */
public record PoliceCarResponse(List<PoliceCar> biler) {

}
