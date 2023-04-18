package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

}
