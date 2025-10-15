package rs.ac.uns.ftn.svt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.model.Rate;

public interface RateRepository extends JpaRepository<Rate, Long> {
}
