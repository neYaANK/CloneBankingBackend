package me.neyaank.clonebankingbackend.rest.auth;

import jakarta.validation.Valid;
import me.neyaank.clonebankingbackend.payload.JwtResponse;
import me.neyaank.clonebankingbackend.payload.LoginRequest;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.security.jwt.JwtUtils;
import me.neyaank.clonebankingbackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var userOptional = userRepository.findById(userDetails.getId());
        if (!userOptional.isPresent())
            return ResponseEntity.notFound().build();
        var user = userOptional.get();
        return ResponseEntity.ok(new JwtResponse(jwt,
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPhoneNumber()));
    }
//For testing purposes will leave it commented here

//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Phone number is already taken!"));
//        }
////
////        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
////            return ResponseEntity
////                    .badRequest()
////                    .body(new MessageResponse("Error: Email is already in use!"));
////        }
//
//        // Create new user's account
//        User user = new User(signUpRequest.getName(),
//                signUpRequest.getSurname(),
//                signUpRequest.getPhoneNumber(),
//                encoder.encode(signUpRequest.getPassword()));
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }
}