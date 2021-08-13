package by.intro.dms.car.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class CarDto {

    @Column(name = "name")
    private String name;
    @Column(name = "creation_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    @Column(name = "count")
    private int count;
}
