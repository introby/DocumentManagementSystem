package by.intro.dms.mapper;

import by.intro.dms.model.file.File;
import by.intro.dms.model.file.dto.FileDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileDto fileToFileDto(File file);
    File dtoToFile(FileDto fileDto);

    List<FileDto> toDtoList(List<File> fileList);
}
