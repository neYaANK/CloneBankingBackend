package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.ECurrency;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private String API = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    public double getExchangeRate(ECurrency from, ECurrency to) {
        WebClient client = WebClient.create();
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(API)
                .retrieve();
        String responseBody = responseSpec.bodyToMono(String.class).block();
        JSONArray response = new JSONArray(responseBody);
        double rateMod = 1;
        if (from == to) rateMod = 1;
        else if (from == ECurrency.UAH || to == ECurrency.UAH) {
            JSONObject rate = to == ECurrency.UAH ? getCurrencyRate(from, response) : getCurrencyRate(to, response);
            rateMod = rate.getDouble("rate");
            rateMod = from == ECurrency.UAH ? Math.pow(rateMod, -1) : rateMod;
        } else {
            double uahToCurrency = 1, currencyToUAH = 1;
            currencyToUAH = getCurrencyRate(from, response).getDouble("rate");
            uahToCurrency = Math.pow(getCurrencyRate(to, response).getDouble("rate"), -1);
            rateMod = currencyToUAH * uahToCurrency;
        }
        return rateMod;
    }

    private JSONObject getCurrencyRate(ECurrency ECurrency, JSONArray arr) {
        for (Object item : arr) {
            var i = (JSONObject) item;
            if (i.getString("cc").equals(ECurrency.name())) return i;
        }
        return null;
    }
}
