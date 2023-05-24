package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CardPayment extends Payment {
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Card sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Card receiver;

    public CardPayment(Card sender, Card receiver, double outgoingValue, double incomingValue, ECurrency outgoingECurrency, ECurrency incomingECurrency, double outgoingToIncomingMod) {
        super(outgoingValue, incomingValue, outgoingECurrency, incomingECurrency, outgoingToIncomingMod);
        this.sender = sender;
        this.receiver = receiver;
    }
}
