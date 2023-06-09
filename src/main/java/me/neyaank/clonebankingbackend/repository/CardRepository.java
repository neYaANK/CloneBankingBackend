package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findCardByCardNumber(String cardNumber);

    Optional<Card> findCardById(Long id);

    boolean existsByCardNumber(String cardNumber);
}
