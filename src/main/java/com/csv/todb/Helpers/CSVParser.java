package com.csv.todb.Helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.csv.todb.PersonData.Address;
import com.csv.todb.PersonData.AddressRepository;
import com.csv.todb.PersonData.People;

@Component
public class CSVParser {
    @Value("${csv.file.location}")
    private String csvFileLocation;

    public List<People> parseCSV(String csvFileLocation, AddressRepository addressRepository) {
        List<People> people = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFileLocation))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] data = line.split(",");

                if (data.length == 8) {
                    Long id = Long.parseLong(data[0].trim());
                    String firstName = data[1].trim();
                    String lastName = data[2].trim();
                    String email = data[3].trim();
                    String addressStr = data[6].trim();

                    Address address = new Address(addressStr);
                    address = addressRepository.save(address);

                    people.add(new People(id, firstName, lastName, email, address));

                    System.out.println("PersonData added: " + id + ", " + firstName + ", " + lastName + ", " + email);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return people;
    }
}
