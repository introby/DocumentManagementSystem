package by.intro.dms.model.response;

import by.intro.dms.model.PaginationInfo;
import by.intro.dms.model.office.dto.OfficeDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OfficesListResponse {

    private PaginationInfo paginationInfo;
    private List<OfficeDto> offices;

}
