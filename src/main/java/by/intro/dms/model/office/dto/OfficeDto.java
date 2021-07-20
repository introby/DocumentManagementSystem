package by.intro.dms.model.office.dto;

import by.intro.dms.model.office.Contact;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OfficeDto {

    private Long id;
    private String name;
    private String city;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime workingTimeFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime workingTimeTo;
    private List<Contact> contactList = new ArrayList<>();
    private String metadata;
}
