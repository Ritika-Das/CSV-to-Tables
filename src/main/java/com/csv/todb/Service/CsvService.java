package com.csv.todb.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csv.todb.Entity.Address;
import com.csv.todb.Entity.Age;
import com.csv.todb.Entity.Person;
import com.csv.todb.Repository.AddressRepository;
import com.csv.todb.Repository.AgeRepository;
import com.csv.todb.Repository.PersonRepository;

import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class CsvService {

    @Value("${csv.directory.path}")
    private String csvDirectoryPath;

    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final AgeRepository ageRepository;

    // constructor with autowired repositories
    @Autowired
    public CsvService(PersonRepository personRepository, AddressRepository addressRepository,
            AgeRepository ageRepository) {
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
        this.ageRepository = ageRepository;
    }

    // Implement loading CSV files from the directory, updating records, and
    // inserting records into the people, address, and age tables
    public void loadCsvFiles() {
        File directory = new File(csvDirectoryPath);
        File[] csvFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if (csvFiles != null) {
            for (File csvFile : csvFiles) {
                try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
                    String[] data;

                    // Skip header line
                    csvReader.readNext();

                    while ((data = csvReader.readNext()) != null) {
                        String firstName = data[1];
                        String lastName = data[2];
                        String email = data[3];
                        int age = calculateAge(data[7]);
                        String address = data[6].trim();

                        Person person = personRepository
                                .findByFirstNameAndLastNameAndEmail(firstName, lastName, email)
                                .orElseGet(() -> {
                                    Person newPerson = new Person();
                                    newPerson.setFirstName(firstName);
                                    newPerson.setLastName(lastName);
                                    newPerson.setEmail(email);
                                    newPerson.setAge(age);
                                    newPerson.setFileName(csvFile.getName());
                                    newPerson.setCreatedTime(LocalDateTime.now());
                                    newPerson.setUpdatedTime(LocalDateTime.now());

                                    Address addressStr = new Address();
                                    addressStr.setAddress(address);
                                    newPerson.setAddress(addressStr);

                                    return newPerson;
                                });

                        person.setUpdatedTime(LocalDateTime.now());
                        personRepository.save(person);

                        if (age >= 25 && age <= 45) {
                            Age ageRecord = new Age();
                            ageRecord.setFirstName(firstName);
                            ageRecord.setLastName(lastName);
                            ageRecord.setEmail(email);
                            ageRecord.setAge(age);
                            ageRepository.save(ageRecord);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error reading CSV file: " + csvFile.getAbsolutePath(), e);
                } catch (CsvValidationException e) {
                    throw new RuntimeException("CSV validation error in file: " + csvFile.getAbsolutePath(), e);
                }
            }
        }
    }

    private int calculateAge(String dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dob = LocalDate.parse(dateOfBirth, formatter);
        return Period.between(dob, LocalDate.now()).getYears();
    }

    // Implement exporting records from the address and people tables to a CSV file
    public Resource exportDataToCsv(String fileName) {
        File csvFile = new File(fileName);
        try (FileWriter writer = new FileWriter(csvFile)) {
            List<Person> people = personRepository.findAll();

            writer.write(
                    "ID,File Name,First Name,Last Name,Email,Age,Created Time,Updated Time,Address ID,Street,City,State,Zip\n");
            for (Person person : people) {
                Address address = person.getAddress();
                writer.write(String.format("%d,%s,%s,%s,%s,%d,%s,%s,%d,%s,%s,%s,%s\n",
                        person.getId(), person.getFileName(), person.getFirstName(), person.getLastName(),
                        person.getEmail(),
                        person.getAge(), person.getCreatedTime(), person.getUpdatedTime(),
                        address.getAddress()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to CSV file: " + fileName, e);
        }

        return new FileSystemResource(csvFile);
    }
}