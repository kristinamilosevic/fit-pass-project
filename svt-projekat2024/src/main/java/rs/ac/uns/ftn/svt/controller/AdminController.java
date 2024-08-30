package rs.ac.uns.ftn.svt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.model.AccountRequest;
import rs.ac.uns.ftn.svt.model.RequestStatus;
import rs.ac.uns.ftn.svt.model.User;
import rs.ac.uns.ftn.svt.service.AccountRequestService;
import rs.ac.uns.ftn.svt.service.UserService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AccountRequestService accountRequestService;

    @Autowired
    private UserService userService;

    @PutMapping("/approve/{id}")
    public void approveAccountRequest(@PathVariable Long id) {
        AccountRequest accountRequest = accountRequestService.getAccountRequestById(id);
        if (accountRequest != null) {
            accountRequest.setStatus(RequestStatus.ACCEPTED);
            accountRequestService.updateAccountRequest(id, accountRequest);

            // Kreirajte korisnika ako zahtev bude odobren
            User user = new User();
            user.setEmail(accountRequest.getEmail());
            user.setAddress(accountRequest.getAddress());
            user.setCreatedAt(LocalDate.now());
            user.setPassword("defaultPassword"); // Podesite default password, ili generišite slučajni
            user.setName("Default Name"); // Podesite default ime
            user.setSurname("Default Surname"); // Podesite default prezime
            user.setPhoneNumber("000-000-0000"); // Podesite default broj telefona
            user.setBirthday(LocalDate.now()); // Podesite default datum rođenja
            user.setCity("Default City"); // Podesite default grad
            user.setZipCode("00000"); // Podesite default poštanski broj
            userService.createUser(user);
        }
    }

    @PutMapping("/reject/{id}")
    public void rejectAccountRequest(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String rejectionReason = requestBody.get("rejectionReason");
        AccountRequest accountRequest = accountRequestService.getAccountRequestById(id);
        if (accountRequest != null) {
            accountRequest.setStatus(RequestStatus.REJECTED);
            accountRequest.setRejectionReason(rejectionReason);
            accountRequestService.updateAccountRequest(id, accountRequest);
        }
    }
}
