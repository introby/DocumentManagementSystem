package by.intro.dms.model.office;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
//    private String type;
    private ContactType type;
    private String value;

    @ManyToOne
    @JsonBackReference
    private Office office;
}
