package rs.ac.uns.ftn.svt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.model.WorkDay;

import java.util.List;

public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {
    List<WorkDay> findByFacilityId(Long facilityId);
}
