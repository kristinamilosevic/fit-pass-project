package rs.ac.uns.ftn.svt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.model.Discipline;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
}
