package rs.ac.uns.ftn.svt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.model.Exercise;
import rs.ac.uns.ftn.svt.model.Facility;
import rs.ac.uns.ftn.svt.model.User;
import rs.ac.uns.ftn.svt.model.WorkDay;
import rs.ac.uns.ftn.svt.repository.ExerciseRepository;
import rs.ac.uns.ftn.svt.repository.FacilityRepository;
import rs.ac.uns.ftn.svt.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository,
                           UserRepository userRepository,
                           FacilityRepository facilityRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
    }

    public Exercise createExercise(Exercise exercise) {
        // Učitavanje korisnika i objekta iz baze
        User user = exercise.getUser();
        if (user != null && user.getId() != null) {
            user = userRepository.findById(exercise.getUser().getId()).orElse(null);
        }

        Facility facility = exercise.getFacility();
        if (facility != null && facility.getId() != null) {
            facility = facilityRepository.findById(exercise.getFacility().getId()).orElse(null);
        }

        // Postavljanje korisnika i objekta u vežbu
        exercise.setUser(user);
        exercise.setFacility(facility);

        // Čuvanje vežbe u bazi
        Exercise savedExercise = exerciseRepository.save(exercise);

        // Uklanjanje viška podataka o korisniku i objektu iz odgovora
        if (savedExercise.getUser() != null) {
            savedExercise.getUser().setExercises(null);
        }
        if (savedExercise.getFacility() != null) {
            savedExercise.getFacility().setExercises(null);
        }

        return savedExercise;
    }


    public boolean isReservationValid(String exerciseJsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode exerciseJson = objectMapper.readTree(exerciseJsonString);

            // Dohvatamo dan u sedmici na osnovu datuma
            String fromTime = exerciseJson.get("fromTime").asText();
            LocalDate date = LocalDate.parse(fromTime.substring(0, 10));
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            System.out.println("Day of week: " + dayOfWeek);

            // Dohvatamo radno vreme objekta za taj dan
            JsonNode facilityNode = exerciseJson.get("facility");
            Long facilityId = facilityNode.get("id").asLong();
            Facility facility = facilityRepository.findById(facilityId).orElse(null);
            if (facility == null) {
                // Ako objekat ne postoji, ne možemo rezervisati termin
                System.out.println("Facility not found: " + facilityId);
                return false;
            }

            // Pronalazimo odgovarajući radni dan za taj dan
            WorkDay workDay = null;
            for (WorkDay wd : facility.getWorkDays()) {
                if (wd.getDay().toString().equalsIgnoreCase(dayOfWeek.toString())) {
                    workDay = wd;
                    break;
                }
            }

            if (workDay == null) {
                System.out.println("Facility is closed on: " + dayOfWeek);
                return false;
            }

            LocalTime startTime = LocalTime.parse(fromTime.substring(11));
            LocalTime endTime = LocalTime.parse(exerciseJson.get("untilTime").asText().substring(11));
            System.out.println("Start time: " + startTime + ", End time: " + endTime);
            System.out.println("Facility working hours: " + workDay.getFromTime() + " to " + workDay.getUntilTime());

            boolean isValid = !startTime.isBefore(workDay.getFromTime()) && !endTime.isAfter(workDay.getUntilTime());
            System.out.println("Is reservation valid: " + isValid);
            return isValid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countByUserId(Long userId) {
        return exerciseRepository.countByUserId(userId);
    }

    public int countByUserIdAndFacilityId(Long userId, Long facilityId) {
        return exerciseRepository.countByUserIdAndFacilityId(userId, facilityId);
    }

    public List<Facility> findVisitedFacilitiesByUserId(Long userId) {
        // Pronađi sve vežbe koje je korisnik uradio
        List<Exercise> exercises = exerciseRepository.findByUserId(userId);

        // Izvuci jedinstvene Facility objekte iz tih vežbi
        return exercises.stream()
                .map(Exercise::getFacility) // Dobija Facility iz svakog Exercise
                .distinct() // Uklanja duplikate
                .collect(Collectors.toList()); // Pretvara u listu
    }
}
