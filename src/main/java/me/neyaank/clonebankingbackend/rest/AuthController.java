package me.neyaank.clonebankingbackend.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import me.neyaank.clonebankingbackend.payload.requests.auth.CodeRequest;
import me.neyaank.clonebankingbackend.payload.requests.auth.LoginRequest;
import me.neyaank.clonebankingbackend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest.getPhoneNumber(), loginRequest.getPassword());
    }

    @PostMapping("/verify")
    @PreAuthorize("hasRole('NO_2FA')")
    public ResponseEntity<?> verifyCode(@NotEmpty @RequestBody CodeRequest code) {
        return authService.verify(code.getCode());
    }

}