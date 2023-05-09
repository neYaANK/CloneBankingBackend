package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.Card;
import me.neyaank.clonebankingbackend.entity.CardType;
import me.neyaank.clonebankingbackend.entity.Currency;
import me.neyaank.clonebankingbackend.entity.PaymentSystem;
import me.neyaank.clonebankingbackend.payload.dto.CardDTO;

import java.util.Set;

public interface CardService {
    boolean isOwner(String card, Long id);

    boolean isOwner(Long card_id, Long id);

    Set<CardDTO> getCardDTOsByUserId(Long id);

    Card createCard(Currency currency, CardType cardType, PaymentSystem paymentSystem, Long userId);
}
