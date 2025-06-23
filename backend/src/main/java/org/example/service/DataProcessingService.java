package org.example.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class DataProcessingService {

    @Value("${app.data.processing.location}")
    private String dataProcessingLocation;

    public File processLatestExcelToCsv() throws IOException {
        // Get the latest .xlsx file from the directory
        try (Stream<Path> files = Files.list(Paths.get(dataProcessingLocation))) {
            Path latestExcelFile = files
                    .filter(p -> p.toString().endsWith(".xlsx"))
                    .max(Comparator.comparingLong(p -> p.toFile().lastModified()))
                    .orElseThrow(() -> new FileNotFoundException("No Excel file found in processing directory"));

            return convertExcelToCsv(latestExcelFile.toFile());
        }
    }

    private File convertExcelToCsv(File excelFile) throws IOException {
        Workbook workbook = WorkbookFactory.create(excelFile);
        Sheet sheet = workbook.getSheetAt(0);

        String csvFileName = excelFile.getName().replace(".xlsx", ".csv");
        Path csvPath = Paths.get(dataProcessingLocation, csvFileName);

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            for (Row row : sheet) {
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            if (i == 5 && row.getRowNum() > 0) {
                                csvPrinter.print(cell.getNumericCellValue() + 10); // Add 10 to score
                            } else {
                                csvPrinter.print(cell.getNumericCellValue());
                            }
                            break;
                        case BOOLEAN:
                            csvPrinter.print(cell.getBooleanCellValue());
                            break;
                        default:
                            csvPrinter.print(cell.getStringCellValue());
                    }
                }
                csvPrinter.println();
            }
        }

        workbook.close();
        return csvPath.toFile();
    }
}
