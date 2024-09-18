package rs.ac.uns.ftn.svt.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.svt.model.Facility;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    @Query("SELECT f FROM Facility f WHERE " +
            "(:city IS NULL OR f.city = :city) " +
            "AND (:discipline IS NULL OR EXISTS (SELECT 1 FROM Discipline d WHERE d.facility.id = f.id AND d.name = :discipline)) " +
            "AND (:minRating IS NULL OR f.totalRating >= :minRating) " +
            "AND (:maxRating IS NULL OR f.totalRating <= :maxRating) " +
            "AND (:hasWorkDays IS NULL OR EXISTS (SELECT 1 FROM WorkDay wd WHERE wd.facility.id = f.id))")
    List<Facility> searchFacilities(@Param("city") String city,
                                    @Param("discipline") String discipline,
                                    @Param("minRating") Double minRating,
                                    @Param("maxRating") Double maxRating,
                                    @Param("hasWorkDays") Boolean hasWorkDays);


    List<Facility> findByActiveFalse();

    List<Facility> findByActiveTrue();

    List<Facility> findByCity(String city);
    @Query("SELECT f FROM Facility f ORDER BY f.totalRating DESC")
    List<Facility> findTopRatedFacilities(PageRequest pageRequest);

    @Query("SELECT f FROM Facility f WHERE f.id IN (SELECT e.facility.id FROM Exercise e WHERE e.user.id = :userId)")
    List<Facility> findFacilitiesByUserId(@Param("userId") Long userId);

    @Query("SELECT f FROM Facility f WHERE f.id NOT IN " +
            "(SELECT e.facility.id FROM Exercise e WHERE e.user.id = :userId)")
    List<Facility> findUnvisitedFacilitiesByUserId(@Param("userId") Long userId);
}
