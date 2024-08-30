package rs.ac.uns.ftn.svt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.svt.model.Exercise;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    int countByUserId(Long userId);
    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.user.id = :userId AND e.facility.id = :facilityId")
    int countByUserIdAndFacilityId(Long userId, Long facilityId);

    List<Exercise> findByUserId(Long userId);
}
