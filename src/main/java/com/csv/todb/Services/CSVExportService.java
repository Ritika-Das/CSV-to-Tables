// package com.csv.todb.Services;

// import java.io.FileWriter;
// import java.io.IOException;
// import java.util.List;

// import org.apache.commons.csv.CSVFormat;
// import org.apache.commons.csv.CSVPrinter;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.csv.todb.PersonData.Address;
// import com.csv.todb.PersonData.AddressRepository;
// import com.csv.todb.PersonData.People;
// import com.csv.todb.PersonData.PeopleDataRepository;

// @Service
// public class CSVExportService {
// @Autowired
// PeopleDataRepository peopleRepository;

// @Autowired
// AddressRepository addressRepository;

// public void exportDataToCSV(String fileName) throws IOException {
// List<People> peopleList = peopleRepository.findAll();
// List<Address> addressList = addressRepository.findAll();

// FileWriter fileWriter = new FileWriter(fileName);

// CSVPrinter csvPrinter = new CSVPrinter(fileWriter,
// CSVFormat.DEFAULT.withHeader("First Name", "Last Name", "Email", "Age",
// "Address"));

// for (People person : peopleList) {
// csvPrinter.printRecord(person.getFirst_name(), person.getLast_name(),
// person.getEmail(), person.getEmail(),
// person.getAddress().getAddress());
// }

// csvPrinter.flush();
// csvPrinter.close();
// }
// }