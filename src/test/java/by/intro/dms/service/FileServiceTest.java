package by.intro.dms.service;

import by.intro.dms.IntegrationTestBase;
import by.intro.dms.exception.NoEntityException;
import by.intro.dms.model.file.File;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileServiceTest extends IntegrationTestBase {

    @Autowired
    private FileService fileService;

    @Test
    void testGetById() throws NoEntityException {
        ObjectId id = addFile();
        File file = fileService.findById(id);
        assertEquals("Test content", file.getContent());
    }

    ObjectId addFile() {
        File file = new File();
        file.setCreationDate(LocalDateTime.now());
        file.setContent("Test content");
        File addedFile = fileService.add(file);
        return addedFile.getId();
    }
}