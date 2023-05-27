package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.CardType;
import me.neyaank.clonebankingbackend.entity.ECardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {
    CardType findByName(ECardType cardType);
}
