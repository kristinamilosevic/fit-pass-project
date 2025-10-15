package rs.ac.uns.ftn.svt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.dto.FacilityDTO;
import rs.ac.uns.ftn.svt.dto.UserDTO;
import rs.ac.uns.ftn.svt.model.Facility;
import rs.ac.uns.ftn.svt.model.User;
import rs.ac.uns.ftn.svt.model.WorkDay;
import rs.ac.uns.ftn.svt.model.es.FacilityDocument;
import rs.ac.uns.ftn.svt.service.FacilityService;
import rs.ac.uns.ftn.svt.service.UserService;
import rs.ac.uns.ftn.svt.service.WorkDayService;
import rs.ac.uns.ftn.svt.service.es.FacilityESService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;
    private final WorkDayService workDayService;
    private final UserService userService;
    private final FacilityESService facilityESService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/es/reindex")
    public ResponseEntity<String> reindexAllFacilities() {
        facilityESService.reindexAllFacilities();
        return ResponseEntity.ok("All facilities reindexed to Elasticsearch with original formatting!");
    }

    @GetMapping("/es/search")
    public ResponseEntity<List<FacilityDocument>> searchES(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description
    ) {
        List<FacilityDocument> results = facilityESService.searchByNameOrDescription(name, description);
        return ResponseEntity.ok(results);
    }

    @GetMapping
    public ResponseEntity<List<Facility>> getActiveFacilities() {
        return ResponseEntity.ok(facilityService.getActiveFacilities());
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

    @GetMapping("/{id}/workdays")
    public ResponseEntity<List<WorkDay>> getWorkDaysByFacilityId(@PathVariable Long id) {
        return ResponseEntity.ok(workDayService.getWorkDaysByFacilityId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Facility> createFacility(@RequestBody FacilityDTO facilityDTO) {
        Facility facility = facilityService.createFacility(facilityDTO);
        facilityESService.indexFacility(facility);
        return new ResponseEntity<>(facility, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<FacilityDTO> updateFacility(@PathVariable Long id, @RequestBody FacilityDTO updatedFacilityDTO) {
        FacilityDTO facilityDTO = facilityService.updateFacility(id, updatedFacilityDTO);
        if (facilityDTO != null) {
            facilityESService.indexFacility(facilityService.findById(id));
            return ResponseEntity.ok(facilityDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityDTO> getFacilityById(@PathVariable Long id) {
        Facility facility = facilityService.findById(id);
        if (facility != null) {
            return ResponseEntity.ok(facilityService.convertToDTO(facility));
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
        facilityESService.deleteFacilityFromIndex(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Facility>> getInactiveFacilities() {
        return ResponseEntity.ok(facilityService.getInactiveFacilities());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getSurname()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @DeleteMapping("/deactivate/{facilityId}")
    public ResponseEntity<Void> deactivateFacility(@PathVariable Long facilityId) {
        try {
            facilityService.deactivateFacilityAndDeleteManages(facilityId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/my-facilities")
    public ResponseEntity<List<Facility>> getFacilitiesForLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.ok(facilityService.findFacilitiesByCity(user.getCity()));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<FacilityDTO>> getTopRatedFacilities() {
        return ResponseEntity.ok(
                facilityService.getTopRatedFacilities(3).stream()
                        .map(facilityService::convertToDTO)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/by-exercise/{userId}")
    public ResponseEntity<List<FacilityDTO>> getFacilitiesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                facilityService.findFacilitiesByUserId(userId).stream()
                        .map(facilityService::convertToDTO)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/unvisited/{userId}")
    public ResponseEntity<List<FacilityDTO>> getUnvisitedFacilitiesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                facilityService.findUnvisitedFacilitiesByUserId(userId).stream()
                        .map(facilityService::convertToDTO)
                        .collect(Collectors.toList())
        );
    }
}
