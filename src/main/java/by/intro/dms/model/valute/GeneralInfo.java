package by.intro.dms.model.valute;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeneralInfo {

    @JsonProperty("Valute")
    private Valutes valutes;
}
