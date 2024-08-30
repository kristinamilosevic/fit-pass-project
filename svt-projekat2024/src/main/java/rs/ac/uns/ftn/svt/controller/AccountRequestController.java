package rs.ac.uns.ftn.svt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.model.AccountRequest;
import rs.ac.uns.ftn.svt.model.RequestStatus;
import rs.ac.uns.ftn.svt.service.AccountRequestService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/account-requests")
public class AccountRequestController {
    @Autowired
    private AccountRequestService accountRequestService;

    @GetMapping
    public List<AccountRequest> getAllAccountRequests() {
        return accountRequestService.getAllAccountRequests();
    }

    @GetMapping("/{id}")
    public AccountRequest getAccountRequestById(@PathVariable Long id) {
        return accountRequestService.getAccountRequestById(id);
    }

    @PostMapping
    public AccountRequest createAccountRequest(@RequestBody AccountRequest accountRequest) {
        accountRequest.setCreatedAt(LocalDate.now());
        accountRequest.setStatus(RequestStatus.PENDING);
        return accountRequestService.createAccountRequest(accountRequest);
    }

    @PutMapping("/{id}")
    public void updateAccountRequest(@PathVariable Long id, @RequestBody AccountRequest updatedAccountRequest) {
        accountRequestService.updateAccountRequest(id, updatedAccountRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteAccountRequest(@PathVariable Long id) {
        accountRequestService.deleteAccountRequest(id);
    }
}
