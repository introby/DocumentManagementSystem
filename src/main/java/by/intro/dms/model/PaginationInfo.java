package by.intro.dms.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationInfo {

    int totalElements;
    int totalPages;
    int pageNumber;
    int pageSize;


}
