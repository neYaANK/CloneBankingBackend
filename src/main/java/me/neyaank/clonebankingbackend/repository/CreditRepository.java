package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}
