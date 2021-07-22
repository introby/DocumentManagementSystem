package by.intro.dms.model.file.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
public class FileDto {

    private ObjectId id;
    private LocalDateTime creationDate;
    private String content;
}
