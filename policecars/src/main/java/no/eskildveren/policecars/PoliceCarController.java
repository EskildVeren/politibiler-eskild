package no.eskildveren.policecars;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.eskildveren.policecars.model.PoliceCar;

/**
 * PoliceCarController
 * 
 * Norwegian names are used for the URL resources because the original data is
 * in Norwegian
 * 
 */
@RestController
@RequestMapping("/politibiler")
public class PoliceCarController {

    @Autowired
    PoliceCarService service;

    /**
     * Due to the specifications of the task, only the cars with status
     * "Tilgjengelig" is available from this http endpoint
     * 
     * @param status the status of the police car
     * @return all policecars matching status
     */
    @GetMapping("/")
    public ResponseEntity<List<PoliceCar>> getAvailiblePoliceCars() {// @RequestParam(required = true) String status) {
        // if (status != "Tilgjengelig")
        // return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // return new ResponseEntity<>(service.getAvailablePoliceCars(status),
        // HttpStatus.OK);
        return new ResponseEntity<>(service.getAvailablePoliceCars("Tilgjengelig"), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PoliceCar> getPoliceCarById(@PathVariable String id) {
        PoliceCar policeCar = service.getPoliceCarById(id);
        if (policeCar == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(policeCar, HttpStatus.OK);
    }

    @PutMapping("/{id}/oppdrag")
    public ResponseEntity<PoliceCar> assignMission() {
        return null;
    }
}
