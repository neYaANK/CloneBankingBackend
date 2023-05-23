package me.neyaank.clonebankingbackend.services;

import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity authenticate(String phoneNumber, String password);

    ResponseEntity verify(String code);
}
