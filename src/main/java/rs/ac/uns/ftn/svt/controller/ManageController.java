package rs.ac.uns.ftn.svt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.dto.ManageDTO;
import rs.ac.uns.ftn.svt.dto.UserUpdateDTO;
import rs.ac.uns.ftn.svt.model.Manages;
import rs.ac.uns.ftn.svt.model.User;
import rs.ac.uns.ftn.svt.model.Facility;
import rs.ac.uns.ftn.svt.service.ManagesService;
import rs.ac.uns.ftn.svt.service.UserService;
import rs.ac.uns.ftn.svt.service.FacilityService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/manages")
public class ManageController {

    @Autowired
    private ManagesService managesService;

    @Autowired
    private UserService userService;

    @Autowired
    private FacilityService facilityService;

    @PostMapping
    public ResponseEntity<String> assignManagerAndUpdate(@RequestBody ManageDTO dto) {

        User user = userService.findById(dto.getUserId());
        Facility facility = facilityService.findById(dto.getFacilityId());

        if (user == null || facility == null) {
            return ResponseEntity.badRequest().body("Invalid user or facility ID.");
        }

        Manages manages = new Manages();
        manages.setUser(user);
        manages.setFacility(facility);
        manages.setStartDate(LocalDate.parse(dto.getStartDate()));
        manages.setEndDate(LocalDate.parse(dto.getEndDate()));
        facility.setActive(true);

        try {
            facilityService.save(facility);
            managesService.save(manages);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Manager assigned and facility updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error assigning manager and updating facility.");
        }
    }
}
