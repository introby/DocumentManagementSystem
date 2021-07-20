package by.intro.dms.model.request;

import by.intro.dms.model.office.Office;
import by.intro.dms.model.office.OfficePage;
import lombok.Data;

@Data
public class OfficeRequest {

    private OfficePage officePage;
    private Office office;

}
