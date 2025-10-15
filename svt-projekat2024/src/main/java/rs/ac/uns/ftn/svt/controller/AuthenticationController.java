package rs.ac.uns.ftn.svt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.svt.dao.UserDao;
import rs.ac.uns.ftn.svt.dto.AuthentitacionRequest;
import rs.ac.uns.ftn.svt.security.ActiveTokens;
import rs.ac.uns.ftn.svt.security.TokenList;
import rs.ac.uns.ftn.svt.security.TokenUtils;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final TokenUtils tokenUtils;
    private final TokenList tokenList;
    private final ActiveTokens activeTokens;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate (
            @RequestBody AuthentitacionRequest request,
            HttpServletRequest httpRequest
    ) {
        final String email = request.getEmail();

        System.out.println("Email: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());

        if (activeTokens.isTokenListedForUser(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already logged in.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );
        final UserDetails user = userDao.findUserByEmail(email);
        if(user != null){
            String token = tokenUtils.generateToken(user);
            activeTokens.addTokenForUser(email, token);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(400).body("Some error!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwtToken;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid token");
        }

        jwtToken = authHeader.substring(7);
        tokenList.listToken(jwtToken);
        return ResponseEntity.ok("Logged out successfully");
    }

}

