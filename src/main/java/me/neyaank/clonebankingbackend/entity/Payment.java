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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "outgoingCurrency_id", referencedColumnName = "id")
    protected Currency outgoingCurrency;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "incomingCurrency_id", referencedColumnName = "id")
    private Currency incomingCurrency;
    protected double outgoingToIncomingMod;
    protected LocalDate issuedAt;

    public Payment(double outgoingValue, double incomingValue, Currency outgoingECurrency, Currency incomingECurrency, double outgoingToIncomingMod) {
        issuedAt = LocalDate.now();
        this.outgoingValue = outgoingValue;
        this.incomingValue = incomingValue;
        this.outgoingCurrency = outgoingECurrency;
        this.incomingCurrency = incomingECurrency;
        this.outgoingToIncomingMod = outgoingToIncomingMod;
    }
}
