package by.intro.dms.service;

import by.intro.dms.model.Document;
import by.intro.dms.repository.DocumentRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class DocumentServiceTest {

    @MockBean
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void findByIdTest() {

        Long documentId = 2L;
        Document doc = new Document();
        doc.setDocumentId(documentId);

        Optional<Document> optionalDocument = Optional.of(doc);

        Mockito.when(documentRepository.findById(documentId)).thenReturn(optionalDocument);

        Document result = documentService.findById(documentId);

        Assert.assertEquals(result.getDocumentId(), documentId);
    }
}