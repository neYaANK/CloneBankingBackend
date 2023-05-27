package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.exception.InvalidCodeException;
import me.neyaank.clonebankingbackend.payload.responses.JwtResponse;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static me.neyaank.clonebankingbackend.security.utils.Util.generateSecureCode;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    TwilioSMSService smsService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;

    @Override
    public ResponseEntity authenticate(String phoneNumber, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(phoneNumber, password));
        var user = userRepository.findByPhoneNumber(phoneNumber).get();
        List<String> roles = user.getRoles().stream()
                .map(item -> item.getName().name())
                .collect(Collectors.toList());
        smsService.sendCode(user.getPhoneNumber(), generateSecureCode(9999, 4));
        String jwt = jwtUtils.generateJwtToken(authentication, false);
        return ResponseEntity.ok(new JwtResponse(jwt,
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPhoneNumber(), roles));
    }

    @Override
    public ResponseEntity verify(String code) {
        var auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var user = userService.findUserByToken(auth).get();

        if (!smsService.verifyCode(user.getPhoneNumber(), code))
            throw new InvalidCodeException(code + " is invalid");
        String jwt = jwtUtils.generateJwtToken(SecurityContextHolder.getContext().getAuthentication(), true);
        List<String> roles = user.getRoles().stream()
                .map(item -> item.getName().name())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPhoneNumber(), roles));
    }
}
