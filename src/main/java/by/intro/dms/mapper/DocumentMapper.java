package by.intro.dms.mapper;

import by.intro.dms.model.Document;
import by.intro.dms.model.dto.DocumentDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentDto documentToDocumentDto(Document document);
    Document documentDtoToDocument(DocumentDto dto);

    List<DocumentDto> documentListToDocumentDtoList(List<Document> documentList);
    List<Document> documentDtoListToDocumentList(List<DocumentDto> documentDtoList);
}
