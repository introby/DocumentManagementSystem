package by.intro.dms.car.service;

import by.intro.dms.car.mapper.AudiMapper;
import by.intro.dms.car.model.AudiModel;
import by.intro.dms.car.model.CarPage;
import by.intro.dms.car.model.dto.CarDto;
import by.intro.dms.car.model.dto.SeatDto;
import by.intro.dms.car.model.dto.VWDto;
import by.intro.dms.car.request.CarRequest;
import by.intro.dms.car.response.CarResponse;
import by.intro.dms.model.tables.Audi;
import by.intro.dms.model.tables.Seat;
import by.intro.dms.model.tables.Vw;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static by.intro.dms.service.PaginationUtils.buildPaginationInfo;

@Service
public class CarService {

    private final DSLContext dsl;
    private final AudiMapper audiMapper;

    public CarService(DSLContext dsl, AudiMapper audiMapper) {
        this.dsl = dsl;
        this.audiMapper = audiMapper;
    }

    public CarResponse findAll(CarRequest request) {
        CarPage carPage = Optional.of(request.getCarPage()).orElse(new CarPage());

        int pageNumber = carPage.getPageNumber();
        int pageSize = carPage.getPageSize();


        List<CarDto> vwDtoList = dsl.select(Vw.VW.NAME, Vw.VW.CREATION_DATE, Vw.VW.COUNT)
                .from(Vw.VW)
                .fetch()
                .into(VWDto.class);
        List<CarDto> carDtoList = vwDtoList;

        List<AudiModel> audiModelList = dsl.select(Audi.AUDI.AUTO_NAME, Audi.AUDI.CREATION_DATE, Audi.AUDI.COUNT)
                .from(Audi.AUDI)
                .fetch()
                .into(AudiModel.class);

        carDtoList.addAll(audiMapper.toAudiDtoList(audiModelList));

        List<CarDto> seatDtoList = dsl.select(Seat.SEAT.NAME, Seat.SEAT.CREATION_DATE, Seat.SEAT.COUNT)
                .from(Seat.SEAT)
                .fetch()
                .into(SeatDto.class);
        carDtoList.addAll(seatDtoList);

        int totalElements = carDtoList.size();
        int totalPages = (int) Math.ceil(1.0 * totalElements / pageSize);

        return CarResponse.builder()
                .paginationInfo(buildPaginationInfo(totalElements, totalPages, pageNumber, pageSize))
                .cars(carDtoList.subList(pageNumber * pageSize, pageNumber * pageSize + pageSize))
                .build();
    }
}
