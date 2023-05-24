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
public class ToCreditPayment extends Payment {
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Card sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Credit receiver;

    public ToCreditPayment(Card sender, Credit receiver, double outgoingValue, double incomingValue, ECurrency outgoingECurrency, ECurrency incomingECurrency, double outgoingToIncomingMod) {
        super(outgoingValue, incomingValue, outgoingECurrency, incomingECurrency, outgoingToIncomingMod);
        this.sender = sender;
        this.receiver = receiver;
    }
}
