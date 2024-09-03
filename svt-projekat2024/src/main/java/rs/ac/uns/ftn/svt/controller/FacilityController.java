package rs.ac.uns.ftn.svt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.dto.FacilityDTO;
import rs.ac.uns.ftn.svt.model.Facility;
import rs.ac.uns.ftn.svt.model.WorkDay;
import rs.ac.uns.ftn.svt.service.FacilityService;
import rs.ac.uns.ftn.svt.service.WorkDayService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/facilities")
public class FacilityController {

    private final FacilityService facilityService;
    private final WorkDayService workDayService;

    @Autowired
    public FacilityController(FacilityService facilityService, WorkDayService workDayService, WorkDayService workDayService1) {
        this.facilityService = facilityService;
        this.workDayService = workDayService1;
    }

    @GetMapping
    public ResponseEntity<List<Facility>> getAllFacilities() {
        List<Facility> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok(facilities);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Facility>> searchFacilities(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String discipline,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(required = false) Boolean hasWorkDays) {

        List<Facility> facilities = facilityService.searchFacilities(city, discipline, minRating, maxRating, hasWorkDays);
        return ResponseEntity.ok(facilities);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Facility> getFacilityById(@PathVariable Long id) {
//        Optional<Facility> facilityOptional = facilityService.getFacilityById(id);
//        return facilityOptional.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping("/{id}/workdays")
    public ResponseEntity<List<WorkDay>> getWorkDaysByFacilityId(@PathVariable Long id) {
        List<WorkDay> workDays = workDayService.getWorkDaysByFacilityId(id);
        return ResponseEntity.ok(workDays);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Facility> createFacility(@RequestBody FacilityDTO facilityDTO) {
        Facility facility = facilityService.createFacility(facilityDTO);
        return new ResponseEntity<>(facility, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<FacilityDTO> updateFacility(@PathVariable Long id, @RequestBody FacilityDTO updatedFacilityDTO) {
        FacilityDTO facilityDTO = facilityService.updateFacility(id, updatedFacilityDTO);
        if (facilityDTO != null) {
            return ResponseEntity.ok(facilityDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityDTO> getFacilityById(@PathVariable Long id) {
        Facility facility = facilityService.findById(id);
        if (facility != null) {
            FacilityDTO facilityDTO = facilityService.convertToDTO(facility);
            return ResponseEntity.ok(facilityDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }

}
