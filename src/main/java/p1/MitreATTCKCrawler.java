package p1;
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
public class MitreATTCKCrawler {
    private  String pathString = "Excel/enterprise-attack-v13.1-techniques.xlsx";
    private  List<String> mitreTechinique = new ArrayList<>();
    public String getPathString() {
        return pathString;
    }
    public List<String> getMitreTechinique() {
        return mitreTechinique;
    }
    
    public  void dataCrawler(String pathString) throws Exception {
        // Open the Excel file
        FileInputStream file = new FileInputStream(new File(pathString));
        
        // Create a workbook instance from the XLSX file
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        
        // Get the first sheet in the workbook
        Sheet sheet = workbook.getSheetAt(0);
        
        // Iterate over each row in the sheet
        for (Row row : sheet) {
            // Get the cell in the "ID" column for this row
            Cell cell = row.getCell(0); // assuming ID is the first column
            
            // If the cell is not empty and has a string value
            if (cell != null && !cell.getStringCellValue().equals("ID")) {
                // Get the value of the cell
                mitreTechinique.add(cell.getStringCellValue());
                
            }
        }
        // Close the workbook and the file input stream
        workbook.close();
        file.close();
    }
    public float coverageRate(List<String> mitreTechiniques, List<String> atomicTechinique) {
        int count = 0;
        for (String mitre : mitreTechiniques) {
            for (String atomic : atomicTechinique) {
                if (mitre.equals(atomic)) {
                    count++;
                }
            }
        }
        return (float) count / mitreTechiniques.size();
    }
}