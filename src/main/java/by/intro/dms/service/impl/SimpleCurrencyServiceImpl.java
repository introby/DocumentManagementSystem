package by.intro.dms.service.impl;

import by.intro.dms.mapper.CurrencyMapper;
import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.Currency;
import by.intro.dms.model.dto.CurrencyDto;
import by.intro.dms.service.CurrencyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class SimpleCurrencyServiceImpl implements CurrencyService {

    private final CurrencyMapper currencyMapper;

    public SimpleCurrencyServiceImpl(CurrencyMapper currencyMapper) {
        this.currencyMapper = currencyMapper;
    }

    @Override
    public Map<String, Currency> getRates() {
        HashMap<String, Currency> map = new HashMap<>();

        JsonNode rateNode;
        try {
            URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
            rateNode = new ObjectMapper().readTree(url);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }

        for (CurrencyEnum currencyEnum : CurrencyEnum.values()) {
            float value = rateNode.findValue(currencyEnum.name()).get("Value").floatValue();
            int nominal = rateNode.findValue(currencyEnum.name()).get("Nominal").intValue();
            String name = rateNode.findValue(currencyEnum.name()).get("Name").textValue();
            String id = rateNode.findValue(currencyEnum.name()).get("ID").textValue();
            int numCode = rateNode.findValue(currencyEnum.name()).get("NumCode").intValue();
            float previous = rateNode.findValue(currencyEnum.name()).get("Previous").floatValue();
            Currency currency = new Currency();
            currency.setName(name);
            currency.setCharCode(currencyEnum.name());
            currency.setNominal(nominal);
            currency.setValue(value);
            currency.setId(id);
            currency.setNumCode(numCode);
            currency.setPrevious(previous);
            map.put(currencyEnum.name(), currency);
        }

        return map;
    }

    @Override
    public float convertCurrency(CurrencyEnum currencyEnum) {
        Map<String, Currency> ratesMap = getRates();
        Map<String, CurrencyDto> rates = currencyMapper.toDtoMap(ratesMap);
        return rates.get(CurrencyEnum.EUR.toString()).getValue() / rates.get(currencyEnum.toString()).getValue();
    }
}