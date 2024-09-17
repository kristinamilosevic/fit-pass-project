package rs.ac.uns.ftn.svt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svt.model.Manages;

public interface ManagesRepository extends JpaRepository<Manages, Long> {
    @Modifying
    @Transactional
    void deleteByFacilityId(Long facilityId);
}
