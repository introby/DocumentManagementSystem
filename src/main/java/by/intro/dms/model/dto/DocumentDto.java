package by.intro.dms.model.dto;

import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.DocStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentDto {
    private Long id;
    private String name;
    private LocalDate createdAt;
    private String supplier;
    private String consumer;
    private LocalDate contractTerm;
    private DocStatus docStatus;
    private String description;
    private Float price;
    private CurrencyEnum currencyEnum;
}
