package by.intro.dms.model.dto;

import by.intro.dms.converter.LocalDateStringConverter;
import by.intro.dms.converter.StringLocalDateConverter;
import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.DocStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentDto {
    private Long documentId;
    private String documentName;
    @JsonSerialize(converter = LocalDateStringConverter.class)
    @JsonDeserialize(converter = StringLocalDateConverter.class)
    private LocalDate createdAt;
    private String supplier;
    private String consumer;
    @JsonSerialize(converter = LocalDateStringConverter.class)
    @JsonDeserialize(converter = StringLocalDateConverter.class)
    private LocalDate contractTerm;
    private DocStatus docStatus;
    private String description;
    private Float price;
    private CurrencyEnum currencyEnum;
}
