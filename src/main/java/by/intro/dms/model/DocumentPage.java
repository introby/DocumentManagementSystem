package by.intro.dms.model;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class DocumentPage {
    private int pageNumber = 0;
    private int pageSize = 6;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "documentName";

}
