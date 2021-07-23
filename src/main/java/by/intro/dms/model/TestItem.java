package by.intro.dms.model;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@ToString
public class TestItem {

    @Id
    private ObjectId id;
    private String name;
    private String content;
}
