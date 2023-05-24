package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.Card;
import me.neyaank.clonebankingbackend.entity.ECardType;
import me.neyaank.clonebankingbackend.entity.ECurrency;
import me.neyaank.clonebankingbackend.entity.EPaymentSystem;
import me.neyaank.clonebankingbackend.payload.dto.CardDTO;

import java.util.Set;

public interface CardService {
    boolean isOwner(String card, Long id);

    boolean isOwner(Long card_id, Long id);

    Set<CardDTO> getCardDTOsByUserId(Long id);

    Card createCard(ECurrency ECurrency, ECardType cardType, EPaymentSystem paymentSystem, Long userId);
}
