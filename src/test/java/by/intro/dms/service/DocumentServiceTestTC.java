package by.intro.dms.service;

import by.intro.dms.IntegrationTestBase;
import by.intro.dms.model.CurrencyEnum;
import by.intro.dms.model.DocStatus;
import by.intro.dms.model.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentServiceTestTC extends IntegrationTestBase {

    public static final Long ID = 9L;
    public static final String SUPPLIER = "Test Supplier";

    @Autowired
    private DocumentService documentService;

    @Test
    void testGetById() {
        Document document = documentService.findById(ID);
        assertEquals("Draco Malfoy", document.getConsumer());
    }

    @Test
    void testGetBySupplier() {
        documentService.saveDocument(insertDocument());
        Document document = documentService.findBySupplier(SUPPLIER);
        assertEquals(15f, document.getPrice());
    }

    private Document insertDocument() {
        return documentService.saveDocument(new Document(
                "Test Doc",
                LocalDate.of(2020, 10, 1),
                "Test Supplier",
                "Test Consumer",
                LocalDate.of(2022, 8, 12),
                DocStatus.NEW,
                "test desc",
                15f,
                CurrencyEnum.USD));
    }
}