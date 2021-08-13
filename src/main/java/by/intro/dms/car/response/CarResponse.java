package by.intro.dms.car.response;

import by.intro.dms.car.model.CarDto;
import by.intro.dms.model.PaginationInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CarResponse {

    private PaginationInfo paginationInfo;
    private List<CarDto> autos;
}
