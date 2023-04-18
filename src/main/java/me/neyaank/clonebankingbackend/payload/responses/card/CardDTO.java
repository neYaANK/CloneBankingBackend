package me.neyaank.clonebankingbackend.payload.responses.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.Card;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardDTO {
    private Long id;
    private String cardNumber;
    private LocalDate expireDate;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardNumber = card.getCardNumber();
        this.expireDate = card.getExpireDate();
    }
}
