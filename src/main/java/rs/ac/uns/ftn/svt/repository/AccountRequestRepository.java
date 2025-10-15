package rs.ac.uns.ftn.svt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.model.AccountRequest;

public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {
}
