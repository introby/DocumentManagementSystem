package by.intro.dms.service;

import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.feign.Currency;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class SimpleCurrencyServiceImpl implements CurrencyService {

    @Override
    public Map<String, Currency> getRates() {
        HashMap<String, Currency> map = new HashMap<>();

        JsonNode rateNode = null;
        try {
            URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
            rateNode = new ObjectMapper().readTree(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CurrencyEnum currencyEnum : CurrencyEnum.values()) {
            float value = rateNode.findValue(currencyEnum.name()).get("Value").floatValue();
            int nominal = rateNode.findValue(currencyEnum.name()).get("Nominal").intValue();
            String name = rateNode.findValue(currencyEnum.name()).get("Name").textValue();
            Currency currency = new Currency();
            currency.setName(name);
            currency.setCharCode(currencyEnum.name());
            currency.setNominal(nominal);
            currency.setValue(value);
            map.put(currencyEnum.name(), currency);
        }

        return map;
    }

    @Override
    public float convertCurrency(CurrencyEnum currencyEnum) {
        Map<String, Currency> rates = getRates();
        return rates.get(CurrencyEnum.EUR.toString()).getValue() / rates.get(currencyEnum.toString()).getValue();
    }
}