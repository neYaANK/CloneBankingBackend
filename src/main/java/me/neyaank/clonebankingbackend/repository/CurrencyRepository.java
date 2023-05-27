package me.neyaank.clonebankingbackend.repository;

import me.neyaank.clonebankingbackend.entity.Currency;
import me.neyaank.clonebankingbackend.entity.ECurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByName(ECurrency currency);
}
