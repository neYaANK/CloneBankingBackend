package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    boolean existsByPhoneNumber(String phoneNumber);


}
