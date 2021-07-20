package by.intro.dms.model.office;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class OfficePage {
    private int pageNumber = 0;
    private int pageSize = 6;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "name";

}
