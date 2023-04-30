package me.neyaank.clonebankingbackend.payload.requests.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.CardType;
import me.neyaank.clonebankingbackend.entity.Currency;
import me.neyaank.clonebankingbackend.entity.PaymentSystem;

@Getter
@Setter
@AllArgsConstructor
public class CreateCardRequest {
    Currency currency;
    CardType type;
    PaymentSystem paymentSystem;
}
