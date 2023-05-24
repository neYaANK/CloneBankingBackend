package me.neyaank.clonebankingbackend.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private String sender;
    private PaymentSource senderSource;
    private String receiver;
    private PaymentSource receiverSource;
    private double incoming;
    private double outgoing;
    private ECurrency outgoingECurrency;
    private ECurrency incomingECurrency;
    private double rate;
    private LocalDate issuedAt;

    public PaymentDTO(Payment payment) {
        incoming = payment.getIncomingValue();
        outgoing = payment.getOutgoingValue();
        incomingECurrency = payment.getIncomingECurrency();
        outgoingECurrency = payment.getOutgoingECurrency();
        rate = payment.getOutgoingToIncomingMod();
        issuedAt = payment.getIssuedAt();
        if (payment instanceof CardPayment) {
            senderSource = PaymentSource.CARD;
            receiverSource = PaymentSource.CARD;
            this.sender = ((CardPayment) payment).getSender().getCardNumber();
            this.receiver = ((CardPayment) payment).getReceiver().getCardNumber();
        } else if (payment instanceof ToCreditPayment) {
            senderSource = PaymentSource.CARD;
            receiverSource = PaymentSource.CREDIT;
            this.sender = ((ToCreditPayment) payment).getSender().getCardNumber();
            this.receiver = ((ToCreditPayment) payment).getReceiver().getId().toString();
        } else if (payment instanceof FromCreditPayment) {
            senderSource = PaymentSource.CREDIT;
            receiverSource = PaymentSource.CARD;
            this.sender = ((FromCreditPayment) payment).getSender().getId().toString();
            this.receiver = ((FromCreditPayment) payment).getReceiver().getCardNumber();
        }
    }
}
