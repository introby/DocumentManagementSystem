package by.intro.dms.mapper;

import by.intro.dms.model.Document;
import by.intro.dms.model.dto.DocumentDto;
import org.mapstruct.Mapper;

@Mapper
public interface documentMapper {

    DocumentDto documentToDocumentDto(Document document);

    Document documentDtoToDocument(DocumentDto dto);
}
