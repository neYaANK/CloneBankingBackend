package me.neyaank.clonebankingbackend.rest;

import me.neyaank.clonebankingbackend.entity.ECurrency;
import me.neyaank.clonebankingbackend.payload.responses.ExchangeRateResponse;
import me.neyaank.clonebankingbackend.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class UtilityController {
    @Autowired
    CurrencyService currencyService;

    @GetMapping("/exchange/{сurrency}")
    public ResponseEntity<ExchangeRateResponse> getExchangeRate(@PathVariable ECurrency сurrency) {
        return ResponseEntity.ok(new ExchangeRateResponse(currencyService.getExchangeRate(сurrency, ECurrency.UAH), сurrency, ECurrency.UAH));
    }
}
