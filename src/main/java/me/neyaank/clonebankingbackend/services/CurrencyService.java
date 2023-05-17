package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.Currency;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CurrencyService {
    private String API = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    public double getExchangeRate(Currency from, Currency to) {
        WebClient client = WebClient.create();
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(API)
                .retrieve();
        String responseBody = responseSpec.bodyToMono(String.class).block();
        JSONArray response = new JSONArray(responseBody);
        double rateMod = 1;
        if (from == to) rateMod = 1;
        else if (from == Currency.UAH || to == Currency.UAH) {
            JSONObject rate = to == Currency.UAH ? getCurrencyRate(from, response) : getCurrencyRate(to, response);
            rateMod = rate.getDouble("rate");
            rateMod = from == Currency.UAH ? Math.pow(rateMod, -1) : rateMod;
        } else {
            double uahToCurrency = 1, currencyToUAH = 1;
            currencyToUAH = getCurrencyRate(from, response).getDouble("rate");
            uahToCurrency = Math.pow(getCurrencyRate(to, response).getDouble("rate"), -1);
            rateMod = currencyToUAH * uahToCurrency;
        }
        return rateMod;
    }

    private JSONObject getCurrencyRate(Currency currency, JSONArray arr) {
        for (Object item : arr) {
            var i = (JSONObject) item;
            if (i.getString("cc").equals(currency.name())) return i;
        }
        return null;
    }
}
