package me.neyaank.clonebankingbackend.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.Currency;
import me.neyaank.clonebankingbackend.entity.Payment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private double incoming;
    private double outgoing;
    private Currency outgoingCurrency;
    private Currency incomingCurrency;
    private double rate;

    public PaymentDTO(Payment payment) {
        incoming = payment.getIncomingValue();
        outgoing = payment.getOutgoingValue();
        incomingCurrency = payment.getIncomingCurrency();
        outgoingCurrency = payment.getOutgoingCurrency();
        rate = payment.getOutgoingToIncomingMod();
    }
}
