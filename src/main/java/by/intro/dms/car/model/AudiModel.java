package by.intro.dms.car.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class AudiModel {

    @Column(name = "auto_name")
    private String name;
    @Column(name = "creation_date")
    private String creationDate;
    @Column(name = "count")
    private int count;
}
