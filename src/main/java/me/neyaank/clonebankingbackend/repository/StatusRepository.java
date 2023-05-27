package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.EStatus;
import me.neyaank.clonebankingbackend.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByName(EStatus status);
}
