package me.neyaank.clonebankingbackend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.payload.dto.PaymentDTO;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentsResponse {
    public String cardNumber;
    private Set<PaymentDTO> incomingPayments;
    private Set<PaymentDTO> outgoingPayments;
}
