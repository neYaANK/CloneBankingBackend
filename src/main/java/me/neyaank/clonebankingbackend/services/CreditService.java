package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.Credit;
import me.neyaank.clonebankingbackend.entity.CreditType;
import me.neyaank.clonebankingbackend.entity.Payment;
import me.neyaank.clonebankingbackend.payload.dto.CreditDTO;

import java.util.Set;

public interface CreditService {
    Set<CreditDTO> getCredits(Long id);

    Credit promptCredit(String cardNumber, CreditType creditType, double balance);

    Payment makeCreditPayment(String cardNumber, Long credit_id, double balance);

    boolean isOwner(Long credit_id, Long id);

}
