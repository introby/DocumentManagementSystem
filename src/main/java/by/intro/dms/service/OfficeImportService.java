package by.intro.dms.service;

import by.intro.dms.model.office.*;
import by.intro.dms.model.response.OfficesListResponse;
import by.intro.dms.repository.office.ContactRepository;
import by.intro.dms.repository.office.OfficeRepository;
import by.intro.dms.service.office.OfficeService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OfficeImportService {

    public static Path tmp;

    static {
        try {
            tmp = Files.createTempDirectory("dms_");
            new File(tmp + "/import").mkdirs();
            new File(tmp + "/export").mkdirs();
        } catch (IOException e) {
            throw new RuntimeException("Temp directory not created", e);
        }
    }

    private Map<UUID, ImportStatus> status = new HashMap<>();
    private Map<UUID, String> outputCsvPath = new HashMap<>();

    private static final String EXPORT_FILE_PATH =
            String.format("%s/export/export_%s.csv", tmp, LocalDateTime.now());

    private final OfficeRepository officeRepository;
    private final ContactRepository contactRepository;
    private final OfficeService officeService;

    public OfficeImportService(OfficeRepository officeRepository,
                               ContactRepository contactRepository,
                               OfficeService officeService) {
        this.officeRepository = officeRepository;
        this.contactRepository = contactRepository;
        this.officeService = officeService;
    }

    public List<List<String>> parseCsv(String csvFile, UUID uuid) {
        List<List<String>> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (IOException | CsvValidationException e) {
            status.put(uuid, ImportStatus.FAILED);
            throw new RuntimeException(e);
        }
        return records;
    }

    public List<Object> getBeansFromCsv(String file, UUID uuid) {
        status.put(uuid, ImportStatus.PENDING);
        List<Office> offices = new ArrayList<>();
        List<Contact> contacts = new ArrayList<>();
        List<List<String>> csv = parseCsv(file, uuid);

        try {
            csv
                    .forEach(list -> {
                        Office office = new Office();
                        office.setName(list.get(0));
                        office.setCity(list.get(1));
                        office.setWorkingTimeFrom(LocalTime.parse(list.get(2)));
                        office.setWorkingTimeTo(LocalTime.parse(list.get(3)));
                        office.setMetadata(list.get(4));
                        String contactsString = list.get(5);
                        if (!contactsString.equals("")) {
                            String[] contactsArray = contactsString.split(";");
                            Arrays.stream(contactsArray).forEach(c -> {
                                Contact contact = new Contact();
                                String[] contactArray = c.split(":");
                                contact.setType(ContactType.valueOf(contactArray[0].toUpperCase()));
                                contact.setValue(contactArray[1]);
                                contact.setOffice(office);
                                contacts.add(contact);
                            });
                        }

                        offices.add(office);
                    });
        } catch (Exception e) {
            status.put(uuid, ImportStatus.FAILED);
            throw new RuntimeException(e);
        }

        List<Object> result = new ArrayList<>();
        result.add(offices);
        result.add(contacts);
        return result;
    }

    @Transactional
    public void saveBeans(List<Object> beans, UUID uuid) {
        List<Office> offices = (List<Office>) beans.get(0);
        List<Contact> contacts = (List<Contact>) beans.get(1);

        try {
            offices.forEach(officeRepository::save);
            contacts.forEach(contactRepository::save);
            status.put(uuid, ImportStatus.SUCCESS);
        } catch (Exception e) {
            status.put(uuid, ImportStatus.FAILED);
            throw new RuntimeException(e);
        }

    }

    public void getCsv(UUID uuid, OfficePage officePage) {
        status.put(uuid, ImportStatus.PENDING);
        OfficesListResponse offices = officeService.getOffices(officePage);

        File csvOutputFile = new File(EXPORT_FILE_PATH);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            offices.getOffices()
                    .stream()
                    .map(officeDto -> String.join(",",
                            officeDto.getName(),
                            officeDto.getCity(),
                            officeDto.getWorkingTimeFrom().toString(),
                            officeDto.getWorkingTimeTo().toString(),
                            officeDto.getMetadata(),
                            officeDto.getContactList().stream()
                                    .map(c -> String.format("%s:%s:%d;",
                                            c.getType(),
                                            c.getValue(),
                                            c.getOffice().getId()))
                                    .collect(Collectors.joining())
                    ))
                    .forEach(pw::println);
        } catch (IOException e) {
            status.put(uuid, ImportStatus.FAILED);
            throw new RuntimeException(e);
        }

        outputCsvPath.put(uuid, csvOutputFile.getPath());
        status.put(uuid, ImportStatus.SUCCESS);
    }

    public ImportStatus getStatus(UUID uuid) {
        return status.getOrDefault(uuid, ImportStatus.UNKNOWN);
    }

    public String getFilePath(UUID uuid) {
        return outputCsvPath.get(uuid);
    }
}
