package me.neyaank.clonebankingbackend.payload.requests.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.ECardType;
import me.neyaank.clonebankingbackend.entity.ECurrency;
import me.neyaank.clonebankingbackend.entity.EPaymentSystem;

@Getter
@Setter
@AllArgsConstructor
public class CreateCardRequest {
    ECurrency ECurrency;
    ECardType type;
    EPaymentSystem paymentSystem;
}
