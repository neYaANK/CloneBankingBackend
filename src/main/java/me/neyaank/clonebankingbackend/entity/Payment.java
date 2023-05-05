package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Card sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Card receiver;
    private double outgoingValue;
    private double incomingValue;
    @Enumerated(EnumType.STRING)
    private Currency outgoingCurrency;
    @Enumerated(EnumType.STRING)
    private Currency incomingCurrency;
    private double outgoingToIncomingMod;

    public Payment(Card sender, Card receiver, double outgoingValue, double incomingValue, Currency outgoingCurrency, Currency incomingCurrency, double outgoingToIncomingMod) {
        this.sender = sender;
        this.receiver = receiver;
        this.outgoingValue = outgoingValue;
        this.incomingValue = incomingValue;
        this.outgoingCurrency = outgoingCurrency;
        this.incomingCurrency = incomingCurrency;
        this.outgoingToIncomingMod = outgoingToIncomingMod;
    }
}
