package rs.ac.uns.ftn.svt.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.svt.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {"facility", "comment", "rate"})
    List<Review> findByUserId(Long userId);

    List<Review> findByFacilityId(Long facilityId);

    List<Review> findByFacilityIdAndIsActive(Long facilityId, boolean b);
}
