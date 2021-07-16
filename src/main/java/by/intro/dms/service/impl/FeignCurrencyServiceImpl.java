package by.intro.dms.service.impl;

import by.intro.dms.mapper.CurrencyMapper;
import by.intro.dms.model.Currency;
import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.dto.CurrencyDto;
import by.intro.dms.model.valute.GeneralInfo;
import by.intro.dms.model.valute.Valutes;
import by.intro.dms.service.CurrencyService;
import by.intro.dms.service.feign.CurrencyRateClient;
import org.springframework.stereotype.Service;

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

        GeneralInfo generalInfo = currencyRateClient.findAll();
        Valutes valutes = generalInfo.getValutes();

        return valutes.getProperties();
    }

    @Override
    public float convertCurrency(CurrencyEnum currencyEnum) {
        Map<String, Currency> ratesMap = getRates();
        Map<String, CurrencyDto> rates = currencyMapper.toDtoMap(ratesMap);
        return rates.get(CurrencyEnum.EUR.toString()).getValue() / rates.get(currencyEnum.toString()).getValue();
    }
}
