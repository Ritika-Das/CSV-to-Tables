package com.csv.todb.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csv.todb.Service.CsvService;

import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api")
public class CsvController {

    private final CsvService csvService;

    // constructor with autowired CsvService
    @Autowired
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/load-csv")
    public ResponseEntity<String> loadCsvFiles() {
        csvService.loadCsvFiles();
        return ResponseEntity.ok("CSV files loaded successfully");
    }

    @GetMapping("/export-csv/{fileName}")
    public ResponseEntity<Resource> exportDataToCsv(@PathVariable String fileName) {
        Resource csvResource = csvService.exportDataToCsv(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(csvResource);
    }
}
