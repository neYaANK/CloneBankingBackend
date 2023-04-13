package me.neyaank.clonebankingbackend.security.services;

import me.neyaank.clonebankingbackend.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);
}
