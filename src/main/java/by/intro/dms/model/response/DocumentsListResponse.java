package by.intro.dms.model.response;

import by.intro.dms.model.PaginationInfo;
import by.intro.dms.model.dto.DocumentDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DocumentsListResponse {

    private PaginationInfo paginationInfo;
    private List<DocumentDto> documents;
}
