package rs.ac.uns.ftn.svt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.dto.FacilityDTO;
import rs.ac.uns.ftn.svt.model.Discipline;
import rs.ac.uns.ftn.svt.model.Facility;
import rs.ac.uns.ftn.svt.model.WorkDay;
import rs.ac.uns.ftn.svt.repository.DisciplineRepository;
import rs.ac.uns.ftn.svt.repository.FacilityRepository;
import rs.ac.uns.ftn.svt.repository.WorkDayRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;
    @Autowired
    private WorkDayRepository workDayRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }
    public List<Facility> searchFacilities(String city, String discipline, Double minRating, Double maxRating, Boolean hasWorkDays) {
        return facilityRepository.searchFacilities(city, discipline, minRating, maxRating, hasWorkDays);
    }
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Optional<Facility> getFacilityById(Long id) {
        return facilityRepository.findById(id);
    }

    public Facility createFacility(FacilityDTO facilityDTO) {
        Facility facility = new Facility();
        // Postavite osnovne podatke o objektu 'facility'

        facility.setName(facilityDTO.getName());
        facility.setDescription(facilityDTO.getDescription());
        facility.setAddress(facilityDTO.getAddress());
        facility.setCity(facilityDTO.getCity());
        facility.setCreatedAt(LocalDate.from(LocalDateTime.now()));

        Facility savedFacility = facilityRepository.save(facility);

        // Postavite radne dane
        for (FacilityDTO.WorkDayDTO workDayDTO : facilityDTO.getWorkDays()) {
            WorkDay workDay = new WorkDay();
            workDay.setValidFrom(workDayDTO.getValidFrom());
            workDay.setDay(workDayDTO.getDay());

            // Osigurajte da je 'fromTime' ranije od 'untilTime'
            if (workDayDTO.getFromTime().isAfter(workDayDTO.getUntilTime())) {
                throw new IllegalArgumentException("fromTime mora biti pre untilTime");
            }

            workDay.setFromTime(workDayDTO.getFromTime());
            workDay.setUntilTime(workDayDTO.getUntilTime());
            workDay.setFacility(savedFacility);

            workDayRepository.save(workDay);
        }

        // Postavite discipline
        for (FacilityDTO.DisciplineDTO disciplineDTO : facilityDTO.getDisciplines()) {
            Discipline discipline = new Discipline();
            discipline.setName(disciplineDTO.getName());
            discipline.setFacility(savedFacility);

            disciplineRepository.save(discipline);
        }

        return savedFacility;
    }

    public Facility updateFacility(Long id, Facility updatedFacility) {
        Facility facility = facilityRepository.findById(id).orElse(null);
        if (facility == null) {
            return null; // ili možete baciti izuzetak
        }

        facility.setName(updatedFacility.getName());
        facility.setDescription(updatedFacility.getDescription());
        facility.setAddress(updatedFacility.getAddress());
        facility.setCity(updatedFacility.getCity());
        facility.setTotalRating(updatedFacility.getTotalRating());
        facility.setActive(updatedFacility.isActive());

        return facilityRepository.save(facility);
    }

    public void deleteFacility(Long id) {
        facilityRepository.deleteById(id);
    }



}
