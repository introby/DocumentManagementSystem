package by.intro.dms.mapper;

import by.intro.dms.model.office.Office;
import by.intro.dms.model.office.dto.OfficeDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfficeMapper {

    OfficeDto officeToOfficeDto(Office office);

    List<OfficeDto> toDtoList(List<Office> officeList);
}
