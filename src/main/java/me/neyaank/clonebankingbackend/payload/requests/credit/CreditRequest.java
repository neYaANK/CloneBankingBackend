package me.neyaank.clonebankingbackend.payload.requests.credit;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {
    private Long creditTypeId;
    private double value;
    @NotBlank
    private String cardNumber;
}
