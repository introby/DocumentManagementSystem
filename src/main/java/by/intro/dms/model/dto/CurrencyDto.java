package by.intro.dms.model.dto;

import lombok.Data;

@Data
public class CurrencyDto {

    private String charCode;
    private int nominal;
    private String name;
    private float value;
}
