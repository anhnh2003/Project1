package com.simplilearn.mavenproject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AtomicExcelExporter {
	
	private AtomicExcelExporter() {
		throw new IllegalStateException("Utility class");
	}
	
    // Export a list of data to an Excel file with the given filename
    public static void exportToExcel(List<List<Object>> data, String filename) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        int rowNum = 0;

        // Add data to sheet
        for (List<Object> row : data) {
            Row currentRow = sheet.createRow(rowNum++);
            for (int i = 0; i < row.size(); i++) {
                Object value = row.get(i);
                Cell cell = currentRow.createCell(i);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue());
                } else if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                }
            }
        }
        // Autosize columns
        for (int i = 0; i < data.get(0).size(); i++) {
            sheet.autoSizeColumn(i);
        }
        // Write to file
        FileOutputStream outputStream = new FileOutputStream(filename);
        workbook.write(outputStream);
        workbook.close();
    }
}
