package me.neyaank.clonebankingbackend.payload.requests.credit;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.CreditType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {
    @NotBlank
    private CreditType creditType;
    @NotBlank
    private double value;
    @NotBlank
    private String cardNumber;
}
