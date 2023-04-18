package me.neyaank.clonebankingbackend.payload.responses.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.Card;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
//Will have more detailed info that CardDTO
public class CardInfoResponse {
    private Long id;
    private String cardNumber;
    private LocalDate expireDate;

    public CardInfoResponse(Card card) {
        this.id = card.getId();
        this.cardNumber = card.getCardNumber();
        this.expireDate = card.getExpireDate();
    }
}
