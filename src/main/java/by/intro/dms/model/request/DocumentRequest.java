package by.intro.dms.model.request;

import by.intro.dms.model.DocumentPage;
import by.intro.dms.model.DocumentSearchCriteria;
import lombok.Data;

@Data
public class DocumentRequest {

    private DocumentPage documentPage;
    private DocumentSearchCriteria documentSearchCriteria;

}
