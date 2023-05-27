package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.ECurrency;

public interface CurrencyService {
    public double getExchangeRate(ECurrency from, ECurrency to);

}
