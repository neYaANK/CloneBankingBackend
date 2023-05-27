package me.neyaank.clonebankingbackend.payload.requests.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String senderCardNumber;
    private String receiverCardNumber;
    private double balance;
}
