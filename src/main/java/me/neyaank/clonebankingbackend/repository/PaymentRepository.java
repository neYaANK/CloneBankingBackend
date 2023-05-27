package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
