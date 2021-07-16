package by.intro.dms.model.valute;

import by.intro.dms.model.Currency;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Valutes {

    private Map<String, Currency> properties = new HashMap<>();

    @JsonAnySetter
    public void add(String key, Currency currency) {
        properties.put(key, currency);
    }
}
