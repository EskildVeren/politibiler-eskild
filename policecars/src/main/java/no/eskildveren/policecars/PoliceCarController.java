package no.eskildveren.policecars;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.eskildveren.policecars.model.PoliceCar;
import no.eskildveren.policecars.model.PoliceCarUpdate;

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
     * Locally it will be reachable from
     * http://localhost:8080/politibiler/?status=Tilgjengelig
     * 
     * @param status the status of the police car
     * @return all policecars matching status
     */
    @GetMapping("/")
    public ResponseEntity<List<PoliceCar>> getPoliceCarsMatchingStatus(@RequestParam(required = true) String status) {
        if (!"Tilgjengelig".equals(status))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(service.getPoliceCarsMatchingStatus(status),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoliceCar> getPoliceCarById(@PathVariable String id) {
        PoliceCar policeCar = service.getPoliceCarById(id);
        if (policeCar == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(policeCar, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> assignMission(@PathVariable String id,
            @RequestBody PoliceCarUpdate update) {
        System.out.println("OPPDRAG: " + update.oppdrag());
        if (update.oppdrag() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        service.assignPoliceCarMission(id, update);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
