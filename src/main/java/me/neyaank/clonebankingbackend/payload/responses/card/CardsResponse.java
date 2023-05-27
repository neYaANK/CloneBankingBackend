package me.neyaank.clonebankingbackend.payload.responses.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.neyaank.clonebankingbackend.payload.dto.CardDTO;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class CardsResponse {
    private Set<CardDTO> cards;
}
