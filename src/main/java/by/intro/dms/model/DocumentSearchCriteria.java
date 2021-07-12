package by.intro.dms.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class DocumentSearchCriteria {
    private String documentName;
    private String consumer;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;
    private List<DocStatus> docStatus;
    private Currency currency = Currency.EUR;
}
