package me.neyaank.clonebankingbackend.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.Credit;
import me.neyaank.clonebankingbackend.entity.CreditType;
import me.neyaank.clonebankingbackend.entity.Status;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditDTO {
    private Long id;
    private CreditType creditType;
    private double balance;
    private double baseBalance;
    private LocalDate issuedAt;
    private Status creditStatus;

    public CreditDTO(Credit credit) {
        this.id = credit.getId();
        this.creditType = credit.getCreditType();
        this.balance = credit.getBalance();
        this.baseBalance = credit.getBaseBalance();
        this.issuedAt = credit.getIssuedAt();
        this.creditStatus = this.getCreditStatus();
    }
}
