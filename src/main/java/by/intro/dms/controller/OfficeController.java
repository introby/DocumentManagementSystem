package by.intro.dms.controller;

import by.intro.dms.model.office.OfficePage;
import by.intro.dms.model.response.OfficesListResponse;
import by.intro.dms.service.CsvService;
import by.intro.dms.service.office.OfficeService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/offices", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfficeController {

    private static final String FILE_PATH =
            String.format("src/main/resources/import/%s.csv", RandomStringUtils.randomAlphanumeric(12));

    private final OfficeService officeService;
    private final CsvService csvService;

    public OfficeController(OfficeService officeService, CsvService csvService) {
        this.officeService = officeService;
        this.csvService = csvService;
    }

//    @PostMapping
//    public OfficesListResponse getOffices(@RequestBody OfficeRequest officeRequest) {
//
//        return officeService.getOffices(officeRequest);
//    }

    @GetMapping
    public OfficesListResponse getOffices(OfficePage officePage) {

        return officeService.getOffices(officePage);
    }

    @GetMapping("/import")
    public UUID importToCSV(String file) {
        File csvFile = new File(FILE_PATH);
        try {
            Files.copy(new File(file).toPath(), csvFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UUID uuid = UUID.randomUUID();

        Runnable task = () -> {
            List<Object> beans = csvService.getBeansFromCsv(FILE_PATH, uuid);
            csvService.saveBeans(beans);
        };
        Thread thread = new Thread(task);
        thread.start();

        return uuid;
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
