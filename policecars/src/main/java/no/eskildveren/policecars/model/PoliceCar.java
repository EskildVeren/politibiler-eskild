package no.eskildveren.policecars.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PoliceCar {

    @Id
    private String id;
    private String merke;
    private String modell;

    @JsonProperty("årsmodell")
    private Integer aarsmodell;

    private String regNr;
    private String status;
    private String oppdrag;

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

    public Integer getAarsmodell() {
        return aarsmodell;
    }

    public void setAarsmodell(Integer årsmodell) {
        this.aarsmodell = årsmodell;
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
}
