package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.ERole;
import me.neyaank.clonebankingbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
