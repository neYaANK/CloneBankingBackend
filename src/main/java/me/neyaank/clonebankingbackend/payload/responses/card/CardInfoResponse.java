package me.neyaank.clonebankingbackend.payload.responses.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.Card;
import me.neyaank.clonebankingbackend.entity.CardType;
import me.neyaank.clonebankingbackend.entity.Currency;
import me.neyaank.clonebankingbackend.entity.PaymentSystem;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
//most detailed response
public class CardInfoResponse {
    private Long id;
    private String cardNumber;
    private LocalDate expireDate;
    private String cv2;
    private double balance;
    private Currency currency;
    private PaymentSystem paymentSystem;
    private CardType type;

    public CardInfoResponse(Card card) {
        this.id = card.getId();
        this.cardNumber = card.getCardNumber();
        this.expireDate = card.getExpireDate();
        this.cv2 = card.getCv2();
        this.balance = card.getBalance();
        this.currency = card.getCurrency();
        this.paymentSystem = card.getPaymentSystem();
        this.type = card.getType();
    }
}
