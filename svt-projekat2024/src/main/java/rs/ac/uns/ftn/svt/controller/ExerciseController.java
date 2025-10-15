package rs.ac.uns.ftn.svt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.model.Exercise;
import rs.ac.uns.ftn.svt.model.Facility;
import rs.ac.uns.ftn.svt.model.User;
import rs.ac.uns.ftn.svt.model.WorkDay;
import rs.ac.uns.ftn.svt.repository.FacilityRepository;
import rs.ac.uns.ftn.svt.repository.UserRepository;
import rs.ac.uns.ftn.svt.service.ExerciseService;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    @Autowired
    public ExerciseController(ExerciseService exerciseService, FacilityRepository facilityRepository, UserRepository userRepository) {
        this.exerciseService = exerciseService;
        this.facilityRepository = facilityRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> createExercise(@RequestBody Map<String, Object> exerciseData) {
        // Preuzimanje podataka iz mape
        Integer facilityId = (Integer) exerciseData.get("facilityId");
        Integer userId = (Integer) exerciseData.get("userId");
        String startTimeStr = (String) exerciseData.get("startTime");
        String endTimeStr = (String) exerciseData.get("endTime");

        // Pronađi odgovarajući objekat Facility prema facilityId
        Facility facility = facilityRepository.findById(Long.valueOf(facilityId))
                .orElseThrow(() -> new NoSuchElementException("Facility not found"));

        // Pronađi odgovarajući objekat User prema userId
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Parsiranje datuma
        OffsetDateTime startTime = OffsetDateTime.parse(startTimeStr);
        OffsetDateTime endTime = OffsetDateTime.parse(endTimeStr);

        // Provera radnog vremena
        DayOfWeek dayOfWeek = startTime.getDayOfWeek();
        Optional<WorkDay> workDayOptional = facility.getWorkDays().stream()
                .filter(workDay -> workDay.getDay() == dayOfWeek) // Koristite getDay() za upoređivanje
                .findFirst();

        if (workDayOptional.isPresent()) {
            WorkDay workDay = workDayOptional.get();
            if (startTime.toLocalTime().isBefore(workDay.getFromTime()) ||
                    endTime.toLocalTime().isAfter(workDay.getUntilTime())) {
                return new ResponseEntity<>("Facility is closed during the selected time.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Workday data not found for the selected day.", HttpStatus.BAD_REQUEST);
        }

        // Kreiranje Exercise objekta
        Exercise exercise = new Exercise();
        exercise.setFromTime(startTime.toLocalDateTime());
        exercise.setUntilTime(endTime.toLocalDateTime());
        exercise.setFacility(facility);
        exercise.setUser(user);

        // Čuvanje u bazi
        Exercise createdExercise = exerciseService.createExercise(exercise);

        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<?> countExercisesByUserId(@PathVariable Long userId) {
        try {
            int count = exerciseService.countByUserId(userId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error counting exercises: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/count/{userId}/{facilityId}")
    public ResponseEntity<?> countExercisesByUserIdAndFacilityId(@PathVariable Long userId, @PathVariable Long facilityId) {
        try {
            int count = exerciseService.countByUserIdAndFacilityId(userId, facilityId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error counting exercises: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/visited-facilities/{userEmail}")
    public ResponseEntity<?> getVisitedFacilitiesWithCounts(@PathVariable String userEmail) {
        try {
            // Pronađi korisnika po emailu
            Optional<User> userOptional = userRepository.findByEmail(userEmail);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            User user = userOptional.get();

            // Pronađi sve unique facility-je koje je korisnik posetio
            List<Facility> visitedFacilities = exerciseService.findVisitedFacilitiesByUserId(user.getId());

            // Kreiraj listu objekata sa informacijama o facility-ju i broju poseta
            List<Map<String, Object>> facilityVisitCounts = new ArrayList<>();
            for (Facility facility : visitedFacilities) {
                int visitCount = exerciseService.countByUserIdAndFacilityId(user.getId(), facility.getId());
                Map<String, Object> facilityInfo = Map.of(
                        "facility", facility,
                        "visitCount", visitCount
                );
                facilityVisitCounts.add(facilityInfo);
            }

            return new ResponseEntity<>(facilityVisitCounts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving visited facilities: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
