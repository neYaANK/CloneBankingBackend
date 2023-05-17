package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

//inheritance
//additional tables
//hardcode
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected double outgoingValue;
    protected double incomingValue;
    @Enumerated(EnumType.STRING)
    protected Currency outgoingCurrency;
    @Enumerated(EnumType.STRING)
    protected Currency incomingCurrency;
    protected double outgoingToIncomingMod;
    protected LocalDate issuedAt;

    public Payment(double outgoingValue, double incomingValue, Currency outgoingCurrency, Currency incomingCurrency, double outgoingToIncomingMod) {
        issuedAt = LocalDate.now();
        this.outgoingValue = outgoingValue;
        this.incomingValue = incomingValue;
        this.outgoingCurrency = outgoingCurrency;
        this.incomingCurrency = incomingCurrency;
        this.outgoingToIncomingMod = outgoingToIncomingMod;
    }
}
