package by.intro.dms.controller;

import by.intro.dms.model.office.ImportStatus;
import by.intro.dms.model.office.OfficePage;
import by.intro.dms.model.response.OfficesListResponse;
import by.intro.dms.service.OfficeImportService;
import by.intro.dms.service.office.OfficeService;
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
            String.format("%s/import/import_%s.csv", OfficeImportService.tmp, LocalDateTime.now());

    private final OfficeService officeService;
    private final OfficeImportService officeImportService;

    public OfficeController(OfficeService officeService, OfficeImportService officeImportService) {
        this.officeService = officeService;
        this.officeImportService = officeImportService;
    }

    @GetMapping
    public OfficesListResponse getOffices(OfficePage officePage) {

        return officeService.getOffices(officePage);
    }

    @GetMapping("/import")
    public ResponseEntity<Object> importFromCsv(String file) {
        File csvFile = new File(FILE_PATH);
        try {
            Files.copy(new File(file).toPath(), csvFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UUID uuid = UUID.randomUUID();

        Runnable task = () -> {
            List<Object> beans = officeImportService.getBeansFromCsv(FILE_PATH, uuid);
            officeImportService.saveBeans(beans, uuid);
        };
        Thread thread = new Thread(task);
        thread.start();

        HttpHeaders headers = new HttpHeaders();
        headers.add("UUID", String.format("%s", uuid));

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportToCsv(OfficePage officePage) {

        UUID uuid = UUID.randomUUID();

        Runnable task = () -> {
            officeImportService.getCsv(uuid, officePage);
        };
        Thread thread = new Thread(task);
        thread.start();

        HttpHeaders headers = new HttpHeaders();
        headers.add("UUID", String.format("%s", uuid));

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/export/{id}/file")
    public ResponseEntity<Object> getFile(@PathVariable("id") UUID uuid) throws FileNotFoundException {
        ImportStatus status = officeImportService.getStatus(uuid);
        if (status == ImportStatus.SUCCESS) {
            String filePath = officeImportService.getFilePath(uuid);
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(value = "/import/{id}")
    public ResponseEntity<Object> importStatus(@PathVariable("id") UUID uuid) {
        ImportStatus importStatus = officeImportService.getStatus(uuid);

        HttpHeaders headers = new HttpHeaders();
        headers.add("status", String.format("%s", importStatus));

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping(value = "/export/{id}")
    public ResponseEntity<Object> exportStatus(@PathVariable("id") UUID uuid) {
        ImportStatus status = officeImportService.getStatus(uuid);
        if (status == ImportStatus.SUCCESS) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", String.format("%s/file", uuid));
            return ResponseEntity
                    .status(HttpStatus.SEE_OTHER)
                    .headers(headers)
                    .build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("status", String.format("%s", status));

        return ResponseEntity.ok().headers(headers).build();
    }
}
