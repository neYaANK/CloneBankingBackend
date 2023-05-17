package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.Payment;
import me.neyaank.clonebankingbackend.payload.dto.PaymentDTO;

import java.util.Optional;
import java.util.Set;

public interface PaymentService {
    Optional<Payment> makeCardPayment(String sender, String receiver, double balance);

    Payment makeFromCreditPayment(Long sender_credit_id, String receiver, double balance);

    Optional<Payment> makeToCreditPayment(String sender, Long receiver_credit_id, double balance);

    Set<PaymentDTO> getIncomingPayments(Long card_id);

    Set<PaymentDTO> getOutgoingPayments(Long card_id);
}
