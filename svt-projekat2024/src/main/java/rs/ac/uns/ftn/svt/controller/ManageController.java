package rs.ac.uns.ftn.svt.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> createManage(@RequestBody ManageDTO manageDTO) {
        System.out.println("User ID: " + manageDTO.getUserId());
        System.out.println("Facility ID: " + manageDTO.getFacilityId());

        User user = userService.findById(manageDTO.getUserId());
        user.setUserType("MANAGER");
        Facility facility = facilityService.findById(manageDTO.getFacilityId());

        if (user == null) {
            System.out.println("User not found with ID: " + manageDTO.getUserId());
            return ResponseEntity.badRequest().body("Invalid user ID.");
        }

        if (facility == null) {
            System.out.println("Facility not found with ID: " + manageDTO.getFacilityId());
            return ResponseEntity.badRequest().body("Invalid facility ID.");
        }

        Manages manages = new Manages();
        manages.setUser(user);
        manages.setFacility(facility);

        manages.setStartDate(LocalDate.parse(manageDTO.getStartDate()));
        if (manageDTO.getEndDate() != null) {
            manages.setEndDate(LocalDate.parse(manageDTO.getEndDate()));
        }
        System.out.println(user.getUserType());
        userService.updateUser(user.getEmail(), userService.toUserUpdateDTO(user));

        managesService.save(manages);
        return ResponseEntity.ok("Manage record created successfully.");
    }

}
