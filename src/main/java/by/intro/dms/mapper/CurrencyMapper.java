package by.intro.dms.mapper;

import by.intro.dms.model.Currency;
import by.intro.dms.model.dto.CurrencyDto;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto currencyToCurrencyDto(Currency currency);

    List<CurrencyDto> toDtoList(List<Currency> currencyList);

    Map<String, CurrencyDto> toDtoMap(Map<String, Currency> currencyMap);
}
