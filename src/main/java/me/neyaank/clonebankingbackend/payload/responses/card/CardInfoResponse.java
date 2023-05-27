package me.neyaank.clonebankingbackend.payload.responses.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.Card;
import me.neyaank.clonebankingbackend.entity.ECardType;
import me.neyaank.clonebankingbackend.entity.ECurrency;
import me.neyaank.clonebankingbackend.entity.EPaymentSystem;

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
    private ECurrency currency;
    private EPaymentSystem paymentSystem;
    private ECardType type;

    public CardInfoResponse(Card card) {
        this.id = card.getId();
        this.cardNumber = card.getCardNumber();
        this.expireDate = card.getExpireDate();
        this.cv2 = card.getCv2();
        this.balance = card.getBalance();
        this.currency = card.getCurrency().getName();
        this.paymentSystem = card.getPaymentSystem().getName();
        this.type = card.getType().getName();
    }
}
