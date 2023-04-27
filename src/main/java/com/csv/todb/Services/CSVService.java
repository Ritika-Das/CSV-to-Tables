package com.csv.todb.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csv.todb.Helpers.CSVParser;
import com.csv.todb.PersonData.AddressRepository;
import com.csv.todb.PersonData.People;
import com.csv.todb.PersonData.PeopleDataRepository;

import jakarta.annotation.PostConstruct;

@Service
public class CSVService {
    private static final Logger logger = LoggerFactory.getLogger(CSVService.class);

    @Autowired
    private CSVParser csvParser;

    @Autowired
    private PeopleDataRepository peopleRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Value("${csv.file.location}")
    private String csvFileLocation;

    public void save() {
        System.out.println("Saving Person data to the database.");
        addressRepository.deleteAllAddresses();
        List<People> people = csvParser.parseCSV(csvFileLocation, addressRepository);
        peopleRepository.saveAll(people);
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing CSVService and saving data to the database.");
        save();
    }
}