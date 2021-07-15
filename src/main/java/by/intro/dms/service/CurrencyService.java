package by.intro.dms.service;

import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.Currency;

import java.util.Map;

public interface CurrencyService {
    Map<String, Currency> getRates();
    float convertCurrency(CurrencyEnum currencyEnum);
}
