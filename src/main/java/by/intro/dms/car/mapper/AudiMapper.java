package by.intro.dms.car.mapper;

import by.intro.dms.car.model.AudiModel;
import by.intro.dms.car.model.dto.AudiDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AudiMapper {

    public List<AudiDto> toAudiDtoList (List<AudiModel> audiModelList) {

        return audiModelList.stream()
                .map(audiModel -> {
                    AudiDto audiDto = new AudiDto();
                    audiDto.setName(audiModel.getName());
                    audiDto.setCount(audiModel.getCount());
                    LocalDate date = LocalDate.parse(audiModel.getCreationDate());
                    LocalTime time = LocalTime.of(0, 0, 0);
                    audiDto.setCreationDate(LocalDateTime.of(date, time));
                    return audiDto;
                })
                .collect(Collectors.toList());
    }
}
