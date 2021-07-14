package by.intro.dms.service;

import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.feign.Currency;
import by.intro.dms.model.feign.GeneralInfo;
import by.intro.dms.service.feign.CurrencyRateClient;
import by.intro.dms.service.feign.CurrencyRateClientBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FeignCurrencyServiceImpl implements CurrencyService {

    private final CurrencyRateClient currencyRateClient;

    public FeignCurrencyServiceImpl(CurrencyRateClientBuilder currencyRateClientBuilder) {
        this.currencyRateClient = currencyRateClientBuilder.currencyRateClient;
    }

    @Override
    public Map<String, Currency> getRates() {
        Map<String, Currency> map = new HashMap<>();
        GeneralInfo generalInfo = currencyRateClient.findAll();
        Currency byn = generalInfo.getValute().getByn();
        map.put("BYN", byn);
        Currency usd = generalInfo.getValute().getUsd();
        map.put("USD", usd);
        Currency eur = generalInfo.getValute().getEur();
        map.put("EUR", eur);

        return map;
    }

    @Override
    public float convertCurrency(CurrencyEnum currencyEnum) {
        Map<String, Currency> rates = getRates();
        return rates.get(CurrencyEnum.EUR.toString()).getValue() / rates.get(currencyEnum.toString()).getValue();
    }


}
