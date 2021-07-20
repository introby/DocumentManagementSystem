package by.intro.dms.service;

import by.intro.dms.model.office.Contact;
import by.intro.dms.model.office.ContactType;
import by.intro.dms.model.office.Office;
import by.intro.dms.repository.office.ContactRepository;
import by.intro.dms.repository.office.OfficeRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalTime;
import java.util.*;

@Service
public class CsvService {

    private Map<UUID, String> status = new HashMap<>();

    private final OfficeRepository officeRepository;
    private final ContactRepository contactRepository;

    public CsvService(OfficeRepository officeRepository, ContactRepository contactRepository) {
        this.officeRepository = officeRepository;
        this.contactRepository = contactRepository;
    }

    public List<List<String>> parseCsv(String csvFile) {
        List<List<String>> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    public List<Object> getBeansFromCsv(String file, UUID uuid) {
        status.put(uuid, "pending");
        List<Office> offices = new ArrayList<>();
        List<Contact> contacts = new ArrayList<>();
        List<List<String>> csv = parseCsv(file);
        csv.stream().skip(1).forEach(list -> {
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

        List<Object> result = new ArrayList<>();
        result.add(offices);
        result.add(contacts);
        return result;
    }

    public void saveBeans(List<Object> beans) {
        List<Office> offices = (List<Office>) beans.get(0);
        List<Contact> contacts = (List<Contact>) beans.get(1);

        offices.forEach(officeRepository::save);
        contacts.forEach(contactRepository::save);
    }

    public String getStatus(UUID uuid) {
        return status.getOrDefault(uuid, "ID not found");
    }
}
