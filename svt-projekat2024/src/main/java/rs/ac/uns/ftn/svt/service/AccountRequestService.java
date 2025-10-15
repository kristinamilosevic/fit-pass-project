package rs.ac.uns.ftn.svt.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.model.AccountRequest;
import rs.ac.uns.ftn.svt.model.RequestStatus;
import rs.ac.uns.ftn.svt.model.User;
import rs.ac.uns.ftn.svt.repository.AccountRequestRepository;
import rs.ac.uns.ftn.svt.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountRequestService {

    @Autowired
    private AccountRequestRepository accountRequestRepository;

    public List<AccountRequest> getAllAccountRequests() {
        return accountRequestRepository.findAll();
    }

    public AccountRequest getAccountRequestById(Long id) {
        return accountRequestRepository.findById(id).orElse(null);
    }

    public AccountRequest createAccountRequest(AccountRequest accountRequest) {
        return accountRequestRepository.save(accountRequest);
    }

    public void updateAccountRequest(Long id, AccountRequest updatedAccountRequest) {
        AccountRequest existingAccountRequest = accountRequestRepository.findById(id).orElse(null);
        if (existingAccountRequest != null) {
            existingAccountRequest.setEmail(updatedAccountRequest.getEmail());
            existingAccountRequest.setCreatedAt(updatedAccountRequest.getCreatedAt());
            existingAccountRequest.setAddress(updatedAccountRequest.getAddress());
            existingAccountRequest.setStatus(updatedAccountRequest.getStatus());
            existingAccountRequest.setRejectionReason(updatedAccountRequest.getRejectionReason());
            accountRequestRepository.save(existingAccountRequest);
        }
    }

    public void deleteAccountRequest(Long id) {
        accountRequestRepository.deleteById(id);
    }
}
