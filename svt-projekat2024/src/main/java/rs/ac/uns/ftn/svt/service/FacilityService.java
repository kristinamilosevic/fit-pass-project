package rs.ac.uns.ftn.svt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.dto.FacilityDTO;
import rs.ac.uns.ftn.svt.model.*;
import rs.ac.uns.ftn.svt.repository.DisciplineRepository;
import rs.ac.uns.ftn.svt.repository.FacilityRepository;
import rs.ac.uns.ftn.svt.repository.ReviewRepository;
import rs.ac.uns.ftn.svt.repository.WorkDayRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;
    @Autowired
    private WorkDayRepository workDayRepository;
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private ReviewRepository reviewRepository;

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

        for (FacilityDTO.WorkDayDTO workDayDTO : facilityDTO.getWorkDays()) {
            WorkDay workDay = new WorkDay();
            workDay.setValidFrom(workDayDTO.getValidFrom());
            workDay.setDay(workDayDTO.getDay());

            if (workDayDTO.getFromTime().isAfter(workDayDTO.getUntilTime())) {
                throw new IllegalArgumentException("fromTime mora biti pre untilTime");
            }

            workDay.setFromTime(workDayDTO.getFromTime());
            workDay.setUntilTime(workDayDTO.getUntilTime());
            workDay.setFacility(savedFacility);

            workDayRepository.save(workDay);
        }

        for (FacilityDTO.DisciplineDTO disciplineDTO : facilityDTO.getDisciplines()) {
            Discipline discipline = new Discipline();
            discipline.setName(disciplineDTO.getName());
            discipline.setFacility(savedFacility);

            disciplineRepository.save(discipline);
        }

        return savedFacility;
    }

    public FacilityDTO updateFacility(Long id, FacilityDTO updatedFacilityDTO) {
        Facility facility = facilityRepository.findById(id).orElse(null);
        if (facility == null) {
            return null;
        }

        facility.setName(updatedFacilityDTO.getName());
        facility.setDescription(updatedFacilityDTO.getDescription());
        facility.setAddress(updatedFacilityDTO.getAddress());
        facility.setCity(updatedFacilityDTO.getCity());


        List<WorkDay> existingWorkDays = workDayRepository.findByFacilityId(facility.getId());
        workDayRepository.deleteAll(existingWorkDays);

        for (FacilityDTO.WorkDayDTO workDayDTO : updatedFacilityDTO.getWorkDays()) {
            WorkDay workDay = new WorkDay();
            workDay.setValidFrom(workDayDTO.getValidFrom());
            workDay.setDay(workDayDTO.getDay());
            workDay.setFromTime(workDayDTO.getFromTime());
            workDay.setUntilTime(workDayDTO.getUntilTime());
            workDay.setFacility(facility);
            workDayRepository.save(workDay);
        }


        List<Discipline> existingDisciplines = disciplineRepository.findByFacilityId(facility.getId());
        disciplineRepository.deleteAll(existingDisciplines);

        for (FacilityDTO.DisciplineDTO disciplineDTO : updatedFacilityDTO.getDisciplines()) {
            Discipline discipline = new Discipline();
            discipline.setName(disciplineDTO.getName());
            discipline.setFacility(facility);
            disciplineRepository.save(discipline);
        }

        facilityRepository.save(facility);

        return mapToDTO(facility);
    }

    private FacilityDTO mapToDTO(Facility facility) {
        FacilityDTO dto = new FacilityDTO();
        dto.setId(facility.getId());
        dto.setName(facility.getName());
        dto.setDescription(facility.getDescription());
        dto.setAddress(facility.getAddress());
        dto.setCity(facility.getCity());
        if (facility.getCreatedAt() != null) {
            dto.setCreatedAt(facility.getCreatedAt().atStartOfDay());
        }


        List<FacilityDTO.WorkDayDTO> workDayDTOs = facility.getWorkDays().stream()
                .map(workDay -> new FacilityDTO.WorkDayDTO(workDay.getValidFrom(), workDay.getDay(), workDay.getFromTime(), workDay.getUntilTime()))
                .collect(Collectors.toList());
        dto.setWorkDays(workDayDTOs);

        List<FacilityDTO.DisciplineDTO> disciplineDTOs = facility.getDisciplines().stream()
                .map(discipline -> new FacilityDTO.DisciplineDTO(discipline.getName()))
                .collect(Collectors.toList());
        dto.setDisciplines(disciplineDTOs);

        return dto;
    }

    public FacilityDTO convertToDTO(Facility facility) {
        return new FacilityDTO(
                facility.getId(),
                facility.getName(),
                facility.getAddress(),
                facility.getCity(),
                facility.getCreatedAt().atStartOfDay(),
                facility.getDescription(),
                facility.getWorkDays().stream().map(this::convertToWorkDayDTO).collect(Collectors.toList()),
                facility.getDisciplines().stream().map(this::convertToDisciplineDTO).collect(Collectors.toList())
        );
    }

    private FacilityDTO.WorkDayDTO convertToWorkDayDTO(WorkDay workDay) {
        return new FacilityDTO.WorkDayDTO(
                workDay.getValidFrom(),
                workDay.getDay(),
                workDay.getFromTime(),
                workDay.getUntilTime()
        );
    }

    private FacilityDTO.DisciplineDTO convertToDisciplineDTO(Discipline discipline) {
        return new FacilityDTO.DisciplineDTO(
                discipline.getName()
        );
    }

    public void deleteFacility(Long id) {
        facilityRepository.deleteById(id);
    }

    public Facility findById(Long id) {
        return facilityRepository.findById(id).orElse(null);
    }

    public void updateTotalRating(Long facilityId) {
        List<Review> reviews = reviewRepository.findByFacilityIdAndIsActive(facilityId, true);

        if (reviews.isEmpty()) {
            return;
        }

        double totalRating = reviews.stream()
                .mapToDouble(review -> {
                    Rate rate = review.getRate();
                    return rate != null ? (rate.getEquipment() + rate.getStaff() + rate.getHygiene() + rate.getSpace()) / 4.0 : 0;
                })
                .average()
                .orElse(0);

        BigDecimal roundedRating = new BigDecimal(totalRating)
                .setScale(2, RoundingMode.HALF_UP);

        Facility facility = facilityRepository.findById(facilityId).orElseThrow();
        facility.setTotalRating(roundedRating.doubleValue());
        facilityRepository.save(facility);
    }
}
