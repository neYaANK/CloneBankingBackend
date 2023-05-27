package me.neyaank.clonebankingbackend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.payload.dto.CreditDTO;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditsResponse {
    private Set<CreditDTO> credits;
}
