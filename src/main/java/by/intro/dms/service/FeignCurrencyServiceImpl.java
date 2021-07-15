package by.intro.dms.service;

import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.Currency;
import by.intro.dms.service.feign.CurrencyRateClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FeignCurrencyServiceImpl implements CurrencyService {

    private final CurrencyRateClient currencyRateClient;

    public FeignCurrencyServiceImpl(CurrencyRateClient currencyRateClient) {
        this.currencyRateClient = currencyRateClient;
    }

    @Override
    public Map<String, Currency> getRates() {
        Map<String, Currency> map = new HashMap<>();
        Map<?, ?> generalInfo = currencyRateClient.findAll();
        Map<String, Map<?, ?>> valute = (Map<String, Map<?, ?>>) generalInfo.get("Valute");

        final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        final Currency usd = mapper.convertValue(valute.get("USD"), Currency.class);
        final Currency byn = mapper.convertValue(valute.get("BYN"), Currency.class);
        final Currency eur = mapper.convertValue(valute.get("EUR"), Currency.class);

        map.put("BYN", byn);
        map.put("USD", usd);
        map.put("EUR", eur);

        return map;
    }

    @Override
    public float convertCurrency(CurrencyEnum currencyEnum) {
        Map<String, Currency> rates = getRates();
        return rates.get(CurrencyEnum.EUR.toString()).getValue() / rates.get(currencyEnum.toString()).getValue();
    }


}
