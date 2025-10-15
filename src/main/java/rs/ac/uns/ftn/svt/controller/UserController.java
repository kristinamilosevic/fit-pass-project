package rs.ac.uns.ftn.svt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.dto.UserTypeResponse;
import rs.ac.uns.ftn.svt.dto.UserUpdateDTO;
import rs.ac.uns.ftn.svt.model.User;
import rs.ac.uns.ftn.svt.repository.UserRepository;
import rs.ac.uns.ftn.svt.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user-type")
    public ResponseEntity<UserTypeResponse> getUserTypeByEmail(@RequestParam String email) {
        String userType = userService.findUserTypeByEmail(email);
        if (userType != null) {
            return ResponseEntity.ok(new UserTypeResponse(userType));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getUsersWithUserType() {
        List<User> users = userService.findByUserType("User");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/idByEmail")
    public ResponseEntity<Long> getUserIdByEmail(@RequestParam String email) {
        Long userId = userService.findUserIdByEmail(email);
        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request, @RequestParam("email") String email) {
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        String confirmNewPassword = request.get("confirmNewPassword");

        if (currentPassword == null || newPassword == null || confirmNewPassword == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing currentPassword, newPassword, or confirmNewPassword in request body");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password and confirm new password do not match");
        }

        // Pronalazak korisnika pomoću email-a
        System.out.println("Searching for user with email: " + email);
        User user = userRepository.findByEmail(email).orElse(null);
        System.out.println("user with email: " + user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password successfully changed");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, @RequestParam("email") String email) {
        String response = userService.updateUser(email, userUpdateDTO);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

}
