package me.neyaank.clonebankingbackend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.ECurrency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponse {
    private double rate;
    private ECurrency base;
    private ECurrency to;
}
