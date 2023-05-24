package me.neyaank.clonebankingbackend.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.Card;
import me.neyaank.clonebankingbackend.entity.ECardType;
import me.neyaank.clonebankingbackend.entity.ECurrency;
import me.neyaank.clonebankingbackend.entity.EPaymentSystem;

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
    private ECurrency currency;
    private EPaymentSystem paymentSystem;
    private ECardType type;


    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardNumber = card.getCardNumber();
        this.expireDate = card.getExpireDate();
        this.balance = card.getBalance();
        this.currency = card.getCurrency().getName();
        this.type = card.getType().getName();
        this.paymentSystem = card.getPaymentSystem().getName();
    }
}
