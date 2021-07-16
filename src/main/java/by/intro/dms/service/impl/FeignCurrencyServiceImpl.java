package by.intro.dms.service.impl;

import by.intro.dms.mapper.CurrencyMapper;
import by.intro.dms.model.Currency;
import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.dto.CurrencyDto;
import by.intro.dms.service.CurrencyService;
import by.intro.dms.service.feign.CurrencyRateClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FeignCurrencyServiceImpl implements CurrencyService {

    private final CurrencyRateClient currencyRateClient;
    private final CurrencyMapper currencyMapper;

    public FeignCurrencyServiceImpl(CurrencyRateClient currencyRateClient,
                                    CurrencyMapper currencyMapper) {
        this.currencyRateClient = currencyRateClient;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public Map<String, Currency> getRates() {
        Map<String, Currency> map = new HashMap<>();

        Object generalInfo = currencyRateClient.findAll();
        final ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        JsonNode jsonNode = mapper.valueToTree(generalInfo);
        JsonNode internalNode = jsonNode.path("Valute");

        internalNode.fieldNames().forEachRemaining((String fieldName) -> {

            try {
                map.put(fieldName, mapper.readerFor(new TypeReference<Currency>() {})
                        .readValue(internalNode.get(fieldName)));
            } catch (IOException e) {
                throw new RuntimeException(e.toString());
            }

        });

        return map;
    }

    @Override
    public float convertCurrency(CurrencyEnum currencyEnum) {
        Map<String, Currency> ratesMap = getRates();
        Map<String, CurrencyDto> rates = currencyMapper.toDtoMap(ratesMap);
        return rates.get(CurrencyEnum.EUR.toString()).getValue() / rates.get(currencyEnum.toString()).getValue();
    }
}
