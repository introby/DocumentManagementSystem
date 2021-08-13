package by.intro.dms.car.service;

import by.intro.dms.car.model.CarDto;
import by.intro.dms.car.model.CarPage;
import by.intro.dms.car.request.CarRequest;
import by.intro.dms.car.response.CarResponse;
import by.intro.dms.model.tables.Audi;
import by.intro.dms.model.tables.Seat;
import by.intro.dms.model.tables.Vw;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static by.intro.dms.service.PaginationUtils.buildPaginationInfo;

@Service
public class CarService {

    private final DSLContext dsl;

    public CarService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public CarResponse findAll(CarRequest request) {
        CarPage carPage = request.getCarPage();
        if (Objects.isNull(carPage)) {
            carPage = new CarPage();
        }
        int pageNumber = carPage.getPageNumber();
        int pageSize = carPage.getPageSize();
        int totalElements = findCountByExpression(request);
        int totalPages = (int) Math.ceil(1.0 * totalElements / pageSize);

        List<CarDto> carDtoList = dsl.select(Audi.AUDI.NAME, Audi.AUDI.CREATION_DATE, Audi.AUDI.COUNT)
                .from(Audi.AUDI)
                .unionAll(dsl.select(Vw.VW.NAME, Vw.VW.CREATION_DATE, Vw.VW.COUNT)
                        .from(Vw.VW))
                .unionAll(dsl.select(Seat.SEAT.NAME, Seat.SEAT.CREATION_DATE, Seat.SEAT.COUNT)
                        .from(Seat.SEAT))
//                .orderBy(Audi.AUDI.ID)
                .limit(pageSize).offset(pageSize * pageNumber)
                .fetch()
                .into(CarDto.class);

        return CarResponse.builder()
                .paginationInfo(buildPaginationInfo(totalElements, totalPages, pageNumber, pageSize))
                .autos(carDtoList)
                .build();
    }

    private int findCountByExpression(CarRequest request) {
        return dsl.fetchCount(dsl.select()
                .from(Audi.AUDI)
                .unionAll(dsl.select()
                        .from(Vw.VW)
                        .unionAll(dsl.select()
                                .from(Seat.SEAT)))
        );
    }

}
