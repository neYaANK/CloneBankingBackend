package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByToken(JwtAuthenticationToken token);
}
