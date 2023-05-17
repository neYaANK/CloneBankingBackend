package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.CreditType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditTypeRepository extends JpaRepository<CreditType, Long> {
}
