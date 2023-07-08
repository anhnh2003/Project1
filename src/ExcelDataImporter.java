package com.simplilearn.mavenproject;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelDataImporter {

    public static List<String> importDataFromExcel(String filePath, int columnIndex) throws IOException {
        List<String> techniqueIds = new ArrayList<>();

        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (cell != null) {
                String techniqueId = cell.getStringCellValue();
                techniqueIds.add(techniqueId);
            }
        }

        workbook.close();
        file.close();

        return techniqueIds;
    }
}
