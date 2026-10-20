package no.eskildveren.policecars.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PoliceCar {

    @Id
    String id;
    String merke;
    String modell;
    String årsmodell;
    String regNr;
    String status;
    String oppdrag;

    public PoliceCar() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerke() {
        return merke;
    }

    public void setMerke(String merke) {
        this.merke = merke;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public String getÅrsmodell() {
        return årsmodell;
    }

    public void setÅrsmodell(String årsmodell) {
        this.årsmodell = årsmodell;
    }

    public String getRegNr() {
        return regNr;
    }

    public void setRegNr(String regNr) {
        this.regNr = regNr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOppdrag() {
        return oppdrag;
    }

    public void setOppdrag(String oppdrag) {
        this.oppdrag = oppdrag;
    }

    @Override
    public String toString() {
        return "Politibil: " + merke + ", " + modell + ", " + id + ";";
    }
}
