package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.EPaymentSystem;
import me.neyaank.clonebankingbackend.entity.PaymentSystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSystemRepository extends JpaRepository<PaymentSystem, Long> {
    PaymentSystem findByName(EPaymentSystem paymentSystem);
}
