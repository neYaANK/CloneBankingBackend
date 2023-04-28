package me.neyaank.clonebankingbackend.payload.responses.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.Card;
import me.neyaank.clonebankingbackend.entity.CardType;
import me.neyaank.clonebankingbackend.entity.Currency;
import me.neyaank.clonebankingbackend.entity.PaymentSystem;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardDTO {
    private Long id;
    private String cardNumber;
    private LocalDate expireDate;
    private double balance;
    private Currency currency;
    private PaymentSystem paymentSystem;
    private CardType type;


    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardNumber = card.getCardNumber();
        this.expireDate = card.getExpireDate();
        this.balance = card.getBalance();
        this.currency = card.getCurrency();
        this.type = card.getType();
        this.paymentSystem = card.getPaymentSystem();
    }
}
