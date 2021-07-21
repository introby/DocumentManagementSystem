package by.intro.dms.controller;

import by.intro.dms.model.office.OfficePage;
import by.intro.dms.model.response.OfficesListResponse;
import by.intro.dms.service.CsvService;
import by.intro.dms.service.office.OfficeService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/offices", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfficeController {

    private static final String FILE_PATH =
            String.format("src/main/resources/import/import_%s.csv", LocalDateTime.now());

    private final OfficeService officeService;
    private final CsvService csvService;

    public OfficeController(OfficeService officeService, CsvService csvService) {
        this.officeService = officeService;
        this.csvService = csvService;
    }

    @GetMapping
    public OfficesListResponse getOffices(OfficePage officePage) {

        return officeService.getOffices(officePage);
    }

    @GetMapping("/import")
    public UUID importFromCsv(String file) {
        File csvFile = new File(FILE_PATH);
        try {
            Files.copy(new File(file).toPath(), csvFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UUID uuid = UUID.randomUUID();

        Runnable task = () -> {
            List<Object> beans = csvService.getBeansFromCsv(FILE_PATH, uuid);
            csvService.saveBeans(beans, uuid);
        };
        Thread thread = new Thread(task);
        thread.start();

        return uuid;
    }

    @GetMapping("/export")
    public UUID exportToCsv(OfficePage officePage) {

        UUID uuid = UUID.randomUUID();

        Runnable task = () -> {
            csvService.getCsv(uuid, officePage);
        };
        Thread thread = new Thread(task);
        thread.start();

        return uuid;
    }

    @GetMapping("/export/{id}/file")
    public ResponseEntity<Object> getFile(@PathVariable("id") UUID uuid) throws FileNotFoundException {
        String status = csvService.getStatus(uuid);
        if (status.equals("success")) {
            String filePath = csvService.getFilePath(uuid);
            File csv = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(csv));
            long length = csv.length();
            boolean delete = csv.delete();
            if (delete) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", csv.getName()));
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(length)
                        .contentType(MediaType.parseMediaType("text/csv"))
                        .body(resource);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(value = "/import/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String importStatus(@PathVariable("id") UUID uuid) {
        return csvService.getStatus(uuid);
    }

    @GetMapping(value = "/export/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String exportStatus(@PathVariable("id") UUID uuid) {
        return csvService.getStatus(uuid);
    }
}
